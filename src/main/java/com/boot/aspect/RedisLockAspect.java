package com.boot.aspect;

import com.boot.annotation.RedisLock;
import com.boot.domain.RedisLockInfo;
import com.boot.aspect.support.RedisKeyGenerator;
import com.boot.utils.RedisLockUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RedisLockAspect {
    @Autowired
    private RedisLockUtils redisLockUtils;
    @Autowired
    private RedisKeyGenerator redisKeyGenerator;

    /**
     * 方法环绕加锁
     * @param point
     * @param redisLock
     * @return
     * @throws Throwable
     */
    @Around("@annotation(redisLock)")
    public Object around(ProceedingJoinPoint point, RedisLock redisLock) throws Throwable {
        RedisLockInfo redisLockInfo = null;
        try {
            String keyName = redisKeyGenerator.getKeyName(point, redisLock);
            redisLockInfo = redisLockUtils.tryLock(keyName, redisLock.expire(), redisLock.tryTimeout());
            if (null != redisLockInfo) {
                Object result = point.proceed();
                return result;
            }
        } catch (Throwable e) {
            log.error("around exception", e);
            throw e;
        } finally {
            if (null != redisLockInfo) {
                redisLockUtils.releaseLock(redisLockInfo);
            }
        }
        throw new RuntimeException("service is busy");

    }
}
