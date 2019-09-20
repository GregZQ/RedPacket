package com.boot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

/**
 * UserMapper
 */
public interface UserMapper extends BaseMapper<User> {

    @Update("update t_user set score = score + #{score} where id = #{id}")
    int addMoneyById(@Param("id") Long id,@Param("score") BigDecimal money);
}
