package com.boot.service;


import com.boot.domain.User;

import java.math.BigDecimal;

/**
 * 用户服务相关接口
 */
public interface UserService {

    /**
     * 添加用户
     * @param user
     * @return
     */
    Long add(User user);

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    boolean deleteByID(Long id);

    /**
     * 用户更新
     * @param user
     * @return
     */
    boolean updateById(User user);


    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    User findById(Long id);

    /**
     * 更新用户数据
     * @param uid
     * @param money
     * @return
     */
    boolean addMoneyById(Long uid, BigDecimal money);
}
