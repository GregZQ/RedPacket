package com.boot.controller;


import com.boot.annotation.RateLimiter;
import com.boot.domain.RedPack;
import com.boot.domain.RedPackRob;
import com.boot.domain.result.Result;
import com.boot.exception.RedPackException;
import com.boot.meta.GlobalMeta;
import com.boot.meta.ResultCodeMeta;
import com.boot.service.RedPackLogService;
import com.boot.service.RedPackService;
import com.boot.utils.MoneyUtils;
import com.boot.aspect.support.RedPackGenerator;
import com.boot.utils.RedisUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/redpack")
public class RedPackController {

    public static final Logger logger = LoggerFactory.getLogger(RedPackController.class);

    public static final BigDecimal DEFAULT_VALUE = BigDecimal.valueOf(0);

    @Autowired
    private RedPackService redPackService;

    @Autowired
    private RedPackLogService redPackLogService;

    @Autowired
    private JedisPool jedisPool;

    @PostMapping("/add")
    public Result<Long> add(@RequestBody RedPack redPack){
        try {
            Preconditions.checkNotNull(redPack);
            Preconditions.checkNotNull(redPack.getCount());
            Preconditions.checkArgument(Objects.nonNull(redPack.getPrice()) &&
                    (redPack.getPrice().compareTo(DEFAULT_VALUE) > 0));
            //数据补全
            redPack.setRetainPrice(redPack.getPrice());
            Long id = this.redPackService.add(redPack);
            List<Integer> packList = RedPackGenerator.genRandList(MoneyUtils.changeInt(redPack.getPrice()),
                    redPack.getCount(), GlobalMeta.REDPACK_MIN, GlobalMeta.REDPACK_MAX);
            try(Jedis jedis = jedisPool.getResource()){
                RedisUtils.addList(jedis, GlobalMeta.REDPACK_KEY_PREFIX+id,packList.stream().map(t->String.valueOf(t)).collect(Collectors.toList()));
            }
            return Result.isOk(id);
        }catch (Exception e){
            logger.error(e.getMessage());
            return Result.build(ResultCodeMeta.OTHER_ERROE, e);
        }
    }

    /**
     * ***************核心操作:抢红包**************************
     * 主流程：1.用户调用service抢红包。
     *         2.controller进行限流,service使用分布式锁防止重复用户，每个用户尝试抢红包。
     *         3.用户抢完红包后发送事件，级联更新日志表与红包表.同时在缓存中插入用户抢红包记录。用于判断用户是否抢过红包
     * @param redPackRob
     * @return
     */
    @PostMapping("/rob")
    @RateLimiter
    public Result<String> robRedPack(@RequestBody RedPackRob redPackRob){
        Jedis jedis = jedisPool.getResource();
        try{

            redPackRob = this.redPackService.robRedPack(redPackRob);

            return Result.isOk(redPackRob);
        }catch (RedPackException e){
            return Result.build(ResultCodeMeta.NO_REDPACK,e.getMessage());
        } catch (Exception e){
            return Result.build(ResultCodeMeta.OTHER_ERROE,e);
        }finally {
            jedis.close();
        }
    }
}
