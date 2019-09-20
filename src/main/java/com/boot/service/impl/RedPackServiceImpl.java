package com.boot.service.impl;

import com.boot.annotation.RedisLock;
import com.boot.domain.RedPack;
import com.boot.domain.RedPackRob;
import com.boot.exception.RedPackException;
import com.boot.listener.RedPackRobEvent;
import com.boot.mapper.RedPackMapper;
import com.boot.meta.GlobalMeta;
import com.boot.service.RedPackService;
import com.boot.utils.MoneyUtils;
import com.boot.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Random;

@Service
public class RedPackServiceImpl implements RedPackService {

    @Resource
    private RedPackMapper redPackMapper;

    @Autowired
    ApplicationContext context;

    @Resource
    private JedisPool jedisPool;

    private static final Random random = new Random();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(RedPack redPack) {
        if (redPackMapper.insert(redPack) > 0) {
            return redPack.getId();
        }
        return null;
    }


    @Override
    @RedisLock(keys = "#redPackRob.rid.toString() + '_' + #redPackRob.uid.toString()", expire = 5000, tryTimeout = 3000)
    public RedPackRob robRedPack(RedPackRob redPackRob) throws Exception {
        /**
         * 用分布式锁防止重复request请求
         * 流程；1。判断是否抢过
         *      2。判断红包是否还存在
         *      3。如果存就抢，并将记录放到缓存中
         *      4。异步更新红包记录，用户记录，红包日志记录
         *
         *  ps:存在隐患, redis数据丢失，造成红包被一个用户重复抢
         */
        Jedis jedis = null;
        try {

            jedis = jedisPool.getResource();
            if (checkRedPackRob(redPackRob.getUid(), redPackRob.getRid())) {
                throw new RedPackException("不能重复抢");
            }
            //天然原子性操作
            String value = RedisUtils.lpop(jedis, GlobalMeta.REDPACK_KEY_PREFIX + redPackRob.getRid());
            if (Objects.isNull(value)) {//为空表示已经被抢空
                throw new RedPackException("红包被抢空");
            }
            BigDecimal money = MoneyUtils.changeBigdecimal(Integer.valueOf(value));
            redPackRob.setMoney(money);
            RedisUtils.addObject(jedis, GlobalMeta.REDPACK_LOG_PREFIX + redPackRob.getUid() + "_" + redPackRob.getRid(), redPackRob);
            context.publishEvent(new RedPackRobEvent(this, redPackRob));

            return redPackRob;
        } catch (Exception e) {
            throw e;
        } finally {
            if (Objects.nonNull(jedis)) {
                jedis.close();
            }
        }
    }

    private boolean checkRedPackRob(Long uid, Long rid) {
        Jedis jedis = jedisPool.getResource();
        try {
            RedPackRob redPackRob = RedisUtils.getObejct(jedis, GlobalMeta.REDPACK_LOG_PREFIX + uid + "_" + rid, RedPackRob.class);
            if (Objects.nonNull(redPackRob)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRetainMoneyById(Long uid, BigDecimal money) {
        if (redPackMapper.updateRetainMoneyById(uid, money) > 0) {
            return true;
        }
        return false;
    }
}
