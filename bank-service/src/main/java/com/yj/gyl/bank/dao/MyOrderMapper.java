package com.yj.gyl.bank.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyOrderMapper {

    /**
     * 获取订单的锁
     *
     * @param orderNo
     * @return
     */
    Long lockTradeRecord(String orderNo);

    Long lockTransferRecord(String orderNo);
}
