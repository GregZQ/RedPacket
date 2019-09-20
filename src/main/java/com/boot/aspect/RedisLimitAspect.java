package com.boot.aspect;


import com.boot.annotation.RateLimiter;
import com.boot.aspect.support.RedisRateLimit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Objects;

@Component
@Aspect
public class RedisLimitAspect {

    @Resource
    private JedisPool jedisPool;

    @Pointcut("@annotation(com.boot.annotation.RateLimiter)")
    public void methodPointcut() {

    }

    @Before(value = "methodPointcut()")
    public void before(JoinPoint joinPoint){
        Class clazz = joinPoint.getTarget().getClass();
        String methodName = joinPoint.getSignature().getName();

        Class[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
        try{
            Method method = clazz.getMethod(methodName,parameterTypes);
            RateLimiter rateLimiter = method.getAnnotation(RateLimiter.class);
            if (Objects.nonNull(rateLimiter)){
                int limit = rateLimiter.limit();
                int timeout = rateLimiter.timeout();
                String token;
                try(Jedis jedis = jedisPool.getResource()){
                    token = RedisRateLimit.getTokenFromBucket(jedis,limit,timeout);
                }
                if (Objects.isNull(token)) {
                    throw new RuntimeException("limit");
                }
            }
        }catch (NoSuchMethodException e) {
            throw new RuntimeException("limit");
        }
    }

}
