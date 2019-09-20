package com.boot.service.impl;

import com.boot.domain.User;
import com.boot.mapper.UserMapper;
import com.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(User user) {
         if (this.userMapper.insert(user) > 0){
             return user.getId();
         }
         return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByID(Long id) {
        if (this.userMapper.deleteById(id) > 0){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(User user) {
        if (this.userMapper.updateById(user) >0){
            return true;
        }
        return false;
    }

    @Override
    public User findById(Long id) {
        return this.userMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMoneyById(Long id, BigDecimal money) {
        if (this.userMapper.addMoneyById(id, money) > 0){
            return true;
        }
        return false;
    }
}
