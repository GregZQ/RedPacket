package com.boot.utils;

import com.boot.meta.GlobalMeta;

import java.math.BigDecimal;

public class MoneyUtils {

    /**
     * 转换为不丢失精度的int。 相当于扩大100倍
     * @param now
     * @return
     */
    public static Integer changeInt(BigDecimal now){
        return  now.multiply(GlobalMeta.BASE_VALUE).intValue();
    }

    /**
     * 转换为double
     * @return
     */
    public static BigDecimal changeBigdecimal(Integer value){
        return BigDecimal.valueOf(value*1.0/100);
    }
}
