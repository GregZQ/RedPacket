package com.boot.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 抢红包操作类
 */
@Data
public class RedPackRob {
    private  Long uid;
    private Long rid;
    private BigDecimal money;
}
