package com.boot.aspect.support;

import io.lettuce.core.ZAddArgs;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.ZParams;

import java.util.List;
import java.util.UUID;

public class RedisRateLimit {

    static final String BUCKET = "BUCKET";

    static final String BUCKET_COUNT = "BUCKET_COUNT";

    static final String BUCKET_MONITOR = "BUCKET_MONITOR";

    // 以恒定速率redis令牌桶中获取一个令牌
    public static String getTokenFromBucket(Jedis jedis, int limit, long timeout){
        String token = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        Transaction transaction = jedis.multi();

        //1.清空过期的数据
        transaction.zremrangeByScore(BUCKET_MONITOR.getBytes(), "-inf".getBytes(), String.valueOf(System.currentTimeMillis() - timeout).getBytes());
        ZParams zParams = new ZParams();
        zParams.weightsByDouble(1.0,0);
        transaction.zinterstore(BUCKET.getBytes(),zParams,BUCKET.getBytes(),BUCKET_MONITOR.getBytes());

        //2.自增计数器
        transaction.incr(BUCKET_COUNT);
        List<Object> results = transaction.exec();
        long counter = (Long) results.get(results.size() - 1);

        transaction = jedis.multi();
        transaction.zadd(BUCKET_MONITOR, now, token);
        transaction.zadd(BUCKET, counter, token);
        transaction.zrank(BUCKET, token);

        // 3、获取信号量的排名
        results = transaction.exec();
        long rank = (Long) results.get(results.size() - 1);

        // 4、判断信号量排名，如果这个信号量不在限定的排名内，否则移除
        if (rank < limit) {
            return token;
        } else {
            // 没有获取信号量，删除数据
            transaction = jedis.multi();
            transaction.zrem(BUCKET, token);
            transaction.zrem(BUCKET_MONITOR, token);
            transaction.exec();
        }
        return null;
    }


}
