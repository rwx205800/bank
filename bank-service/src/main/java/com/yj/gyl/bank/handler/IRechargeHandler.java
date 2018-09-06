package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.ConfirmRechargeDto;
import com.yj.gyl.bank.dto.PreRechargeDto;
import com.yj.gyl.bank.dto.YeePayRechargeDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;

/**
 * @author hepei
 * @date 2017/12/13 19:04:52
 */
public interface IRechargeHandler {
    /**
     * 预充值(发送短信)
     *
     * @param preRechargeDto
     * @return
     */
    CommonResponse<TradeRecordRsDto> preRecharge(PreRechargeDto preRechargeDto);

    /**
     * 确认充值
     *
     * @param confirmRechargeDto
     * @return
     */
    CommonResponse<TradeRecordRsDto> confirmRecharge(ConfirmRechargeDto confirmRechargeDto);

    /**
     * 预充值(重发短信)
     *
     * @param orderNo 充值请求号
     * @return
     */
    CommonResponse rechargeResendSMS(String orderNo);

    /**
     * 充值记录查询
     *
     * @param orderNo 充值请求号
     * @return
     */
    CommonResponse rechargeRecord(String orderNo);

    /**
     * 网银充值
     *
     * @param rechargeDto
     * @return
     */
    CommonResponse yeePayRecharge(YeePayRechargeDto rechargeDto);

    /**
     * 网银充值订单查询
     *
     * @param orderNo
     * @return
     */
    CommonResponse yeePayRechargeRecord(String orderNo);

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    CommonResponse yeePayBankFrpList();
}
