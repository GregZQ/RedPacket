package com.boot.listener;

import com.boot.domain.RedPackRob;
import com.boot.domain.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;


@Data
public class RedPackRobEvent extends ApplicationEvent {

    private RedPackRob redPackRob;

    public RedPackRobEvent(Object source, RedPackRob redPackRob) {
        super(source);
        this.redPackRob = redPackRob;
    }
}
