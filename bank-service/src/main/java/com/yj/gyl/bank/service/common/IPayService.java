package com.yj.gyl.bank.service.common;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.handler.common.PaymentType;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;

/**
 * @author hepei
 * @date 2017/12/28 11:16:49
 */
public interface IPayService {
    /**
     * 根据订单编号查询订单详情
     *
     * @param orderNo 订单编号
     * @return
     */
    CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(String orderNo);

    /**
     * 获取交易通道
     *
     * @param channel
     * @return
     */
    PaymentType getChannelByChannel(String channel);

    /**
     * 获取交易通道
     *
     * @param userId 用户id
     * @param cardNo 银行卡号
     * @param source 交易来源appid
     * @return
     */
    PaymentType getChannel(Long userId, String cardNo, String source);

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    CommonResponse yeePayBankFrpList();
}
