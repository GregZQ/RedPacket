package com.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.domain.RedPack;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * 红包mapper
 */
public interface RedPackMapper extends BaseMapper<RedPack> {

    @Update("update t_redpack set retainPrice = retainPrice - #{money}")
    int updateRetainMoneyById(@Param("uid") Long uid, @Param("money") BigDecimal money);
}
