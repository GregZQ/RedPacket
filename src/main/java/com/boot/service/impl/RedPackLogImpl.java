package com.boot.service.impl;

import com.boot.domain.RedPackLog;
import com.boot.mapper.RedPackLogMapper;
import com.boot.service.RedPackLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
public class RedPackLogImpl implements RedPackLogService {

    @Resource
    private RedPackLogMapper redPackLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(RedPackLog redPackLog) {
        if (redPackLogMapper.insert(redPackLog) > 0){
            return redPackLog.getId();
        }
        return null;
    }
}
