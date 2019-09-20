package com.boot.domain;


import com.baomidou.mybatisplus.annotation.*;
import com.boot.meta.GenderMeta;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;//姓名
    private Long age;//年龄
    private GenderMeta gender;//性别
    private String address;//地址
    private BigDecimal score;//总余额


    /**
     * 判断一个对象为空对象
     * @param user
     * @return
     */
    public static boolean isNullObject(User user){
        if (Objects.isNull(user)){
            return false;
        }
        return Objects.isNull(user.getId())&&
               Objects.isNull(user.getName())&&
               Objects.isNull(user.getAge())&&
               Objects.isNull(user.getGender())&&
               Objects.isNull(user.getAddress())&&
               Objects.isNull(user.getScore());
    }

}
