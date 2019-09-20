package com.boot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class JsonUtils {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper mapper;
    static {
        mapper = new ObjectMapper();
    }

    /**
     * 对象转字符串
     * 出错直接向上抛出
     * @param obj
     * @return
     * @throws JsonProcessingException
     */
    public static String objectToString(Object obj) throws Exception {
            return mapper.writeValueAsString(obj);
    }

    /**
     * 字符串转对象
     * 出错直接向上抛出
     * @param value
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T stringToObj(String value,Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }

}
