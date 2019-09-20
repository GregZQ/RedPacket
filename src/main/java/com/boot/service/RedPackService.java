package com.boot.service;


import com.boot.domain.RedPack;
import com.boot.domain.RedPackRob;

import java.math.BigDecimal;

/**
 * 红包service
 */
public interface RedPackService {

    /**
     * 添加红包总数
     * @param redPack
     * @return
     */
    Long add(RedPack redPack);

    /**
     * 抢红包方法
     * @param redPackRob
     * @return
     */
    RedPackRob robRedPack(RedPackRob redPackRob) throws Exception;

    /**
     * 更新剩余金额
     * @param uid
     * @param money
     * @return
     */
    boolean updateRetainMoneyById(Long uid, BigDecimal money);
}
