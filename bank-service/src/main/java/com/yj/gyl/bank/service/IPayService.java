package com.yj.gyl.bank.service;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.*;
import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.rsdto.ConfirmBindCardRsDto;
import com.yj.gyl.bank.rsdto.PreBindCardRsDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

/**
 * Created by renfei on 2017/12/18.
 */
public interface IPayService {
    /**
     * 预绑卡（有短信绑卡，发送绑卡请求、易宝发送验证码短信）
     *
     * @param preBindCardDto
     * @param tMerchant
     * @return
     */
    CommonResponse<PreBindCardRsDto> preBindCard(@RequestBody PreBindCardDto preBindCardDto, TMerchant tMerchant);

    /**
     * 确认绑卡
     *
     * @param confirmBindCardDto
     * @param tMerchant
     * @return
     */
    CommonResponse<ConfirmBindCardRsDto> confirmBindCard(@RequestBody ConfirmBindCardDto confirmBindCardDto, TMerchant tMerchant);

    /**
     * 解绑卡
     *
     * @param tMerchant
     */
    CommonResponse delBindCard(TBankCard tBankCard, TMerchant tMerchant);

    /**
     * 获取绑卡信息
     *
     * @param userId
     * @return
     */
    CommonResponse getAllBindCards(Long userId);

    /**
     * 预充值（有短信充值，发送充值请求、易宝发送验证码短信）
     *
     * @param preRechargeDto
     * @param tMerchant
     * @return
     */
    CommonResponse<TradeRecordRsDto> preRecharge(@RequestBody PreRechargeDto preRechargeDto, TMerchant tMerchant);

    /**
     * 确认充值
     *
     * @param confirmRechargeDto
     * @param tMerchant
     * @return
     */
    CommonResponse<TradeRecordRsDto> confirmRecharge(@RequestBody ConfirmRechargeDto confirmRechargeDto, TMerchant tMerchant);

    /**
     * 充值 重发短信验证码
     *
     * @param requestNo 充值请求号
     * @param tMerchant
     * @return
     */
    CommonResponse rechargeResendSMS(String requestNo, TMerchant tMerchant);

    /**
     * 充值记录查询
     *
     * @param requestNo 充值请求号
     * @param tMerchant
     * @return
     */
    CommonResponse rechargeRecord(String requestNo, TMerchant tMerchant, Date payTime);

    /**
     * 提现
     *
     * @param withdrawDto
     * @param tMerchant
     * @return
     */
    CommonResponse<TradeRecordRsDto> withdraw(@RequestBody WithdrawDto withdrawDto, TMerchant tMerchant);

    /**
     * 查询提现记录
     *
     * @param recordRsDto
     * @param tMerchant
     * @return
     */
    CommonResponse withdrawRecord(@RequestBody TradeRecordDto recordRsDto, TMerchant tMerchant);

    /**
     * 查询支付渠道的绑卡列表
     *
     * @param userId    用户id
     * @param cardNo    银行卡号
     * @param tMerchant 商户号信息
     * @return
     */
    CommonResponse queryBindCardList(Long userId, String cardNo, TMerchant tMerchant);

    /**
     * 获取网银支付URL
     *
     * @param rechargeDto
     * @return
     */
    CommonResponse getYeePayUrl(YeePayRechargeDto rechargeDto);

    /**
     * 网银充值订单查询
     *
     * @param orderNo
     * @return
     */
    CommonResponse yeePayRechargeRecord(String orderNo);
}
