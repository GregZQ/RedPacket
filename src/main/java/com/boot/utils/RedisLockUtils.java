package com.boot.utils;


import com.boot.domain.RedisLockInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;

@Component
@Slf4j
public class RedisLockUtils {

    @Resource
    private JedisPool jedisPool;

    private static final String LUA_SCRIPT_LOCK = "return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])";
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<String>(LUA_SCRIPT_LOCK, String.class);
    private static final String LUA_SCRIPT_UNLOCK = "if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])) else return '0' end";
    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<String>(LUA_SCRIPT_UNLOCK, String.class);


    /**
     * 加锁
     * @param redisKey   缓存KEY
     * @param expire     到期时间 毫秒
     * @param tryTimeout 尝试获取锁超时时间 毫秒
     * @return
     */
    public RedisLockInfo tryLock(String redisKey, long expire, long tryTimeout) {
        Assert.isTrue(tryTimeout > 0, "tryTimeout必须大于0");
        long timestamp = System.currentTimeMillis();
        int tryCount = 0;

        String lockId = UUID.randomUUID().toString();
        try(Jedis jedis = jedisPool.getResource()) {
            while ((System.currentTimeMillis() - timestamp) < tryTimeout) {
                try {
                    String response = jedis.set(redisKey, lockId, "NX", "PX", expire);
                    if ("OK".equals(response)) {
                        log.info("lock: {}, {}", lockId, tryCount);
                        return new RedisLockInfo(lockId, redisKey, expire, tryTimeout, tryCount);
                    } else {
                        Thread.sleep(50);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("", e);
                }
            }
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param redisLockInfo 获取锁返回的对象
     * @return
     */
    public boolean releaseLock(RedisLockInfo redisLockInfo) {
        Long releaseSuccess = 1L;
        try(Jedis jedis = jedisPool.getResource()) {
            String command = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            if (releaseSuccess.equals(jedis.eval(command, Collections.singletonList(redisLockInfo.getRedisKey()), Collections.singletonList(redisLockInfo.getLockId())))) {
                return true;
            }
        }
        return false;
    }
}
