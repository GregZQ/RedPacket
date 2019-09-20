package com.boot.controller;


import com.boot.domain.User;
import com.boot.domain.result.Result;
import com.boot.meta.GlobalMeta;
import com.boot.meta.ResultCodeMeta;
import com.boot.service.UserService;
import com.boot.utils.RedisUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Resource
    private JedisPool jedisPool;

    @PostMapping("/add")
    public Result<Long> add(@RequestBody User user) {
        try {
            Preconditions.checkNotNull(user);
            Preconditions.checkNotNull(user.getName());
            Preconditions.checkNotNull(user.getAge());
            Preconditions.checkNotNull(user.getAddress());
            Preconditions.checkNotNull(user.getGender());
            user.setScore(BigDecimal.valueOf(0));
            //数据补全

            Long id = this.userService.add(user);
            user.setId(id);
            try (Jedis jedis = jedisPool.getResource()) {
                RedisUtils.addObject(jedis, GlobalMeta.USER_KEY_PREFIX + id, user);
            }
            return Result.isOk(id);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.build(ResultCodeMeta.OTHER_ERROE, e);
        }
    }


    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) {
        try {
            Preconditions.checkNotNull(id);

            boolean flag = this.userService.deleteByID(id);
            if (flag) {
                try (Jedis jedis = jedisPool.getResource()) {
                    RedisUtils.delete(jedis, GlobalMeta.USER_KEY_PREFIX + id);
                }
            }
            return Result.isOk(flag);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.build(ResultCodeMeta.OTHER_ERROE, e.getMessage());
        }
    }

    @PostMapping("/update")
    public Result<Boolean> updateById(@RequestBody User user) {
        try {
            Preconditions.checkNotNull(user);
            Preconditions.checkNotNull(user.getId());
            Preconditions.checkNotNull(user.getAddress());
            Preconditions.checkNotNull(user.getName());
            Preconditions.checkNotNull(user.getAge());

            boolean flag = this.userService.updateById(user);
            if (flag) {
                try (Jedis jedis = jedisPool.getResource()) {
                    RedisUtils.addObject(jedis, GlobalMeta.USER_KEY_PREFIX + user.getId(), user);
                }
            }
            return Result.isOk(flag);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.build(ResultCodeMeta.OTHER_ERROE, e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<User> findById(@PathVariable Long id) {
        Jedis jedis = jedisPool.getResource();
        try {
            Preconditions.checkNotNull(id);
            User user = RedisUtils.getObejct(jedis, GlobalMeta.USER_KEY_PREFIX + id, User.class);
            if (Objects.isNull(user) && !User.isNullObject(user)) {
                user = this.userService.findById(id);
                if (Objects.isNull(user)){user = GlobalMeta.NULL_USER;}//添加一个空对象，避免对象为空后重复查询
                RedisUtils.addObject(jedis, GlobalMeta.USER_KEY_PREFIX + id, user);
            }
            return Result.isOk(User.isNullObject(user) ? null : user);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.build(ResultCodeMeta.OTHER_ERROE, e.getMessage());
        } finally {
            jedis.close();
        }
    }

}
