package com.boot.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_redpack_log")
public class RedPackLog {
    @TableId(type = IdType.AUTO)
    private Long id;//日志主键
    private Long uid;//对应用户
    private Long rid;//对应红包
    @TableField(value = "robMoney")
    private BigDecimal robMoney;//抢的钱数
    @TableField(value = "allMoney")
    private BigDecimal allMoney;//用户总钱数
    @TableField(value = "createDate",fill = FieldFill.INSERT)
    private Date createDate;//创建时间
}
