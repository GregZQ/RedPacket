package com.boot.listener;

import com.boot.domain.RedPackLog;
import com.boot.domain.RedPackRob;
import com.boot.domain.User;
import com.boot.service.RedPackLogService;
import com.boot.service.RedPackService;
import com.boot.service.UserService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;


@Component
public class RedPackRobEventListener {

    @Resource
    private RedPackLogService redPackLogService;

    @Resource
    private UserService userService;

    @Resource
    private RedPackService redPackService;

    @EventListener
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void consumer(RedPackRobEvent redPackRobEvent) throws InterruptedException {

        RedPackRob redPackRob = redPackRobEvent.getRedPackRob();

        User user = userService.findById(redPackRob.getUid());

        //更新用户金额
        BigDecimal allMoney = user.getScore().add(redPackRob.getMoney());
        this.userService.addMoneyById(user.getId(),redPackRob.getMoney());

        //更新红包日志
        RedPackLog redPackLog = new RedPackLog();
        redPackLog.setAllMoney(allMoney);
        redPackLog.setRid(redPackRob.getRid());
        redPackLog.setUid(redPackRob.getUid());
        redPackLog.setRobMoney(redPackRob.getMoney());
        redPackLogService.add(redPackLog);

        //更新红包余额
        redPackService.updateRetainMoneyById(redPackRob.getUid(),redPackRob.getMoney());
    }
}
