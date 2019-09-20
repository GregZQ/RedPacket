package com.boot.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.cache.annotation.Cacheable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 红包domain
 */
@Data
@TableName(value = "t_redpack")
public class RedPack {

    @TableId(type = IdType.AUTO)
    private Long id;
    private int count;
    private BigDecimal price;
    @TableField(value = "retainPrice")
    private BigDecimal retainPrice;//剩余金额
    @TableField(value = "createDate",fill = FieldFill.INSERT)
    private Date createDate;
    @TableField(value = "prcDate",fill = FieldFill.INSERT)
    private Date prcDate;
}
