package com.yj.gyl.bank.service.common;

import com.yj.gyl.bank.model.TTradeRecord;
import com.yj.gyl.bank.model.TTradeRecordWithBLOBs;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;

import java.util.Map;

/**
 * Created by renfei on 2017/12/19.
 */
public interface ITradeService {

    void saveTradeRecord(TradeRecordRsDto recordRsDto, Long merchantId);

    TTradeRecord getTradeRecord(String orderNo);

    TTradeRecord getTradeRecord(Long uId, String orderNo);

    TTradeRecordWithBLOBs getTradeRecordWithBLOBs(Long uId, String orderNo);

    void updateTradeRecord(long tradeId, String requestno, String respMsg, int status, int notifyStatus);

    void updateTradeRecord(TTradeRecordWithBLOBs tradeRecord);

    void updateTradeRecordByStatus(TTradeRecordWithBLOBs tradeRecord);

    /**
     * 根据订单编号 更新记录
     *
     * @param tradeRecord 更新字段
     * @param orderNo     订单编号
     */
    void updateTradeRecordByOrderNo(TTradeRecordWithBLOBs tradeRecord, String orderNo);

    void withDrawTradeRecord(Map<String, String> result);

    void rechargeTradeRecord(Map<String, String> result);

    void yopRechargeTradeRecord(Map<String, String> result);

    void yopWithDrawTradeRecord(Map<String, String> result);
    /**
     * 解析易宝网易充值回调结果
     *
     * @param result
     */
    void yeePayRecharge(Map<String, String> result);

}
