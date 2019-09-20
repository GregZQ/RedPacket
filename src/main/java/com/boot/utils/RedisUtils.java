package com.boot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * redis工具，没有使用注解缓存
 */

public class RedisUtils {


    /**
     * 向redis添加一个对象
     * @param jedis
     * @param key
     * @param redisValue
     * @throws Exception
     */
    public static void addObject(Jedis jedis,String key, Object redisValue) throws Exception {
        String value = JsonUtils.objectToString(redisValue);
        addString(jedis,key,value);
    }

    /**
     * 向redis添加一个字符串
     * @param jedis
     * @param key
     * @param value
     * @throws Exception
     */
    public static void addString(Jedis jedis, String key, String value) throws Exception{
        jedis.set(key,value);
    }

    /**
     * 根据key删除一个对象
     * @param jedis
     * @param key
     */
    public static void delete(Jedis jedis, String key){
        jedis.del(key);
    }

    /**
     * 根据key查询对象
     */
    public static <T> T getObejct(Jedis jedis,String key, Class<T> clazz) throws Exception {
        String value = get(jedis,key);
        return StringUtils.isEmpty(value) ? null : JsonUtils.stringToObj(value,clazz);
    }

    /**
     * 根据key查询字符串
     */
    public static String get(Jedis jedis, String key){
        return jedis.get(key);
    }

    /**
     * 插入一个列表
     * @param jedis
     * @param key
     * @param value
     */
    public static void addList(Jedis jedis, String key, List<String> value){
        if (value == null){value = Lists.newArrayList();}
        String[] tmp = value.toArray(new String[value.size()]);
        jedis.lpush(key, tmp);
    }

    /**
     * 获取列表当中的元素
     */
    public static String lpop(Jedis jedis, String key){
        return jedis.lpop(key);
    }
}
