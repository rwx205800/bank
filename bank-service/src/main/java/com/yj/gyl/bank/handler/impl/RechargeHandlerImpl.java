package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.yj.base.alimq.producer.AliMQProducerTemplate;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.ConfirmRechargeDto;
import com.yj.gyl.bank.dto.PreRechargeDto;
import com.yj.gyl.bank.dto.YeePayRechargeDto;
import com.yj.gyl.bank.handler.IRechargeHandler;
import com.yj.gyl.bank.handler.common.PaymentType;
import com.yj.gyl.bank.handler.common.TradeTypeEnum;
import com.yj.gyl.bank.model.*;
import com.yj.gyl.bank.mq.TransactionPayMessage;
import com.yj.gyl.bank.mq.TransactionPayResult;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import com.yj.gyl.bank.rsdto.TransactionPayEnum;
import com.yj.gyl.bank.service.IPayService;
import com.yj.gyl.bank.service.PayClientServiceFactor;
import com.yj.gyl.bank.service.common.IBankCardMerchantService;
import com.yj.gyl.bank.service.common.IBankCardService;
import com.yj.gyl.bank.service.common.IMerchantService;
import com.yj.gyl.bank.service.common.ITradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hepei
 * @date 2017/12/13 19:06:22
 */
@Component
public class RechargeHandlerImpl implements IRechargeHandler {

    private static final Logger logger = LoggerFactory.getLogger(RechargeHandlerImpl.class);

    @Autowired
    private IBankCardService bankCardService;

    @Autowired
    private IBankCardMerchantService bankCardMerchantService;

    @Autowired
    private ITradeService tradeService;

    @Autowired
    private IMerchantService merchantService;

    @Autowired
    private com.yj.gyl.bank.service.common.IPayService payService;

    @Autowired
    private AliMQProducerTemplate template;

    @Override
    public CommonResponse<TradeRecordRsDto> preRecharge(PreRechargeDto preRechargeDto) {
        logger.info("[预充值(发送短信)入参]:{}", JSONObject.toJSONString(preRechargeDto));
        if (!StringUtils.hasText(preRechargeDto.getCardNo())) {
            logger.info("[预充值(发送短信)错误]: 支付银行卡号不能为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_EMPTY);
        }
        if (!StringUtils.hasText(preRechargeDto.getCardNo())) {
            logger.info("[预充值(发送短信)错误]: 支付交易号不能为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_ORDERNO_EMPTY);
        }
        if (preRechargeDto.getUserId() == null || preRechargeDto.getUserId().compareTo(new Long("0")) < 0) {
            logger.info("[预充值(发送短信)错误]: 用户ID有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_UID_EMPTY);
        }
        if (preRechargeDto.getAmount() == null || preRechargeDto.getAmount().compareTo(new BigDecimal("0.01")) < 0) {
            logger.info("[预充值(发送短信)错误]: 充值金额不能小于0.01");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_MONEY_EMPTY);
        }

        //获取绑卡记录
        TBankCard bankCard = bankCardService.getUserBankCard(preRechargeDto.getUserId(), preRechargeDto.getCardNo());
        if (bankCard == null) {
            logger.info("[预充值(发送短信)错误]: 该银行卡信息不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_NOT_EXIST);
        }
        //获取交易来源的交易通道账户号
        TMerchant tMerchant = merchantService.getMerchantBySource(preRechargeDto.getSource());
        if (tMerchant == null) {
            logger.info("[预充值(发送短信)错误]: 支付通道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        //获取绑卡的通道记录
        TBankCardMerchant merchant = bankCardMerchantService.getMerchant(bankCard.getId(), tMerchant.getId(), preRechargeDto.getSource());
        if (merchant == null) {
            logger.info("[预充值(发送短信)错误]: 未查询到绑卡记录");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_NOT_BIND);
        }
        //查询支付通道
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("[预充值(发送短信)错误]: 未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse<TradeRecordRsDto> commonResponse = payClient.preRecharge(preRechargeDto, tMerchant);
        if (commonResponse != null && commonResponse.getData() != null) {
            //保存请求记录
            TradeRecordRsDto recordRsDto = commonResponse.getData();
            //交易类型（0:充值  1:提现）
            recordRsDto.setType(TradeTypeEnum.recharge.getValue());
            TTradeRecord tradeRecord = tradeService.getTradeRecord(recordRsDto.getOrderNo());
            if (tradeRecord == null) {
                tradeService.saveTradeRecord(recordRsDto, merchant.getId());
            } else {
                TTradeRecordWithBLOBs tradeRecordWithBLOBs = new TTradeRecordWithBLOBs();
                tradeRecordWithBLOBs.setId(tradeRecord.getId());
                tradeRecordWithBLOBs.setAmount(recordRsDto.getAmount());
                tradeRecordWithBLOBs.setUserId(recordRsDto.getUserId());
                tradeRecordWithBLOBs.setOrderNo(recordRsDto.getOrderNo());
                tradeRecordWithBLOBs.setOrderId(recordRsDto.getOrderId());
                tradeRecordWithBLOBs.setType(recordRsDto.getType());
                tradeRecordWithBLOBs.setCallbackUrl(recordRsDto.getResponseUrl());
                tradeRecordWithBLOBs.setReqMsg(recordRsDto.getReqMsg());
                tradeRecordWithBLOBs.setCardMerchantId(merchant.getId());
                tradeRecordWithBLOBs.setUpdateTime(new Date());
                tradeService.updateTradeRecord(tradeRecordWithBLOBs);
            }
        }
        return commonResponse;
    }

    @Override
    public CommonResponse<TradeRecordRsDto> confirmRecharge(ConfirmRechargeDto confirmRechargeDto) {
        logger.info("[确认充值入参]:{}", JSONObject.toJSONString(confirmRechargeDto));
        if (confirmRechargeDto.getUserId() == null || confirmRechargeDto.getUserId().compareTo(new Long("0")) < 0) {
            logger.info("[确认充值错误]: 用户ID有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_UID_EMPTY);
        }
        if (!StringUtils.hasText(confirmRechargeDto.getOrderNo())) {
            logger.info("[确认充值错误]: 支付交易号不能为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_ORDERNO_EMPTY);
        }
        if (!StringUtils.hasText(confirmRechargeDto.getValidateCode())) {
            logger.info("[确认充值错误]: 短信验证码不能为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_VERIFY_RECHARGE_VALIDATE_CODE_EMPTY);
        }
        //查询交易记录
        TTradeRecord tradeRecord = tradeService.getTradeRecord(confirmRechargeDto.getUserId(), confirmRechargeDto.getOrderNo());
        if (tradeRecord == null) {
            logger.info("[确认充值错误]: 未查询到相关交易记录");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_VERIFY_RECHARGE_RECORD_EMPTY);
        }
        TBankCardMerchant merchant = bankCardMerchantService.getBankCardMerchantById(tradeRecord.getCardMerchantId());
        if (merchant == null) {
            logger.info("[确认充值错误]: 未查询到绑卡记录");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_NOT_BIND);
        }
        //确定支付通道类型
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("[确认充值错误]: 未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        //获取交易来源的交易通道账户号
        TMerchant tMerchant = merchantService.getMerchantById(merchant.getMerchantId());
        if (tMerchant == null) {
            logger.info("[确认充值错误]: 支付通道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        //确定支付通道
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse<TradeRecordRsDto> commonResponse = payClient.confirmRecharge(confirmRechargeDto, tMerchant);
        //充值埋点
        if (commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
            TransactionPayMessage payMessage = new TransactionPayMessage();
            payMessage.setUserId(tradeRecord.getUserId());
            payMessage.setMoney(tradeRecord.getAmount());
            payMessage.setOrderNo(tradeRecord.getOrderNo());
            payMessage.setType(TradeTypeEnum.recharge.getValue());
            payMessage.setSource(merchant.getSource());
            payMessage.setCreateTime(tradeRecord.getCreateTime());
            //发起充值
            payMessage.setStatus(0);
            template.saveMessage(TransactionPayResult.TRANSACTION_PAY_RECHARGE_RESULT, payMessage);
        }
        if (commonResponse != null && commonResponse.getData() != null) {
            //保存请求记录
            TradeRecordRsDto recordRsDto = commonResponse.getData();
            //交易类型（0:充值  1:提现）
            recordRsDto.setType(TradeTypeEnum.recharge.getValue());
            recordRsDto.setAmount(tradeRecord.getAmount());
            commonResponse.setData(recordRsDto);

            TTradeRecordWithBLOBs tradeRecordWithBLOBs = tradeService.getTradeRecordWithBLOBs(recordRsDto.getUserId(), recordRsDto.getOrderNo());
            if (tradeRecordWithBLOBs == null) {
                logger.info("[确认充值异常, 充值订单号:{} 未查询到相关记录！]", confirmRechargeDto.getOrderNo());
            }
            TTradeRecordWithBLOBs recordWithBLOBs = new TTradeRecordWithBLOBs();
            recordWithBLOBs.setId(tradeRecordWithBLOBs.getId());
            recordWithBLOBs.setOrderId(recordRsDto.getOrderId());
            recordWithBLOBs.setReqMsg(tradeRecordWithBLOBs.getReqMsg() + "|" + recordRsDto.getReqMsg());
            recordWithBLOBs.setRespMsg(recordRsDto.getData());
            recordWithBLOBs.setUpdateTime(new Date());
            tradeService.updateTradeRecordByStatus(recordWithBLOBs);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse rechargeResendSMS(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            logger.info("[充值重发短信验证码错误]: 充值请求号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_EMPTY);
        }
        TTradeRecord record = tradeService.getTradeRecord(orderNo);
        if (record == null) {
            logger.info("[充值重发短信验证码错误]: 充值请求号有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_ERROR);
        }
        TBankCardMerchant merchant = bankCardMerchantService.getBankCardMerchantById(record.getCardMerchantId());
        if (merchant == null) {
            logger.info("[充值重发短信验证码错误]: 未查询到绑卡记录");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_NOT_BIND);
        }
        TMerchant tMerchant = merchantService.getMerchantById(merchant.getMerchantId());
        if (tMerchant == null) {
            logger.info("[充值重发短信验证码错误]: 支付通道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("[充值重发短信验证码错误]: 未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        return payClient.rechargeResendSMS(orderNo, tMerchant);
    }

    @Override
    public CommonResponse rechargeRecord(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            logger.info("[查询充值错误]: 充值请求号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_EMPTY);
        }
        TTradeRecord record = tradeService.getTradeRecord(orderNo);
        if (record == null) {
            logger.info("[查询充值错误]: 充值请求号有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_ERROR);
        }
        TBankCardMerchant merchant = bankCardMerchantService.getBankCardMerchantById(record.getCardMerchantId());
        if (merchant == null) {
            logger.info("[查询充值错误]: 未查询到绑卡记录");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CARDNO_NOT_BIND);
        }
        TMerchant tMerchant = merchantService.getMerchantById(merchant.getMerchantId());
        if (tMerchant == null) {
            logger.info("[查询充值错误]: 支付通道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("[查询充值错误]: 未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_RECHARGE_CHANNEL_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        return payClient.rechargeRecord(orderNo, tMerchant,record.getCreateTime());
    }

    /**
     * 网银充值
     *
     * @param rechargeDto
     * @return
     */
    @Override
    public CommonResponse yeePayRecharge(YeePayRechargeDto rechargeDto) {
        logger.info("网银充值入参：{}", JSONObject.toJSONString(rechargeDto));
        if (rechargeDto.getUserId() == null || rechargeDto.getUserId().compareTo(new Long("0")) < 1) {
            return CommonResponse.builder().result(1).status(1).msg("用户id为空").build();
        }
        if (!StringUtils.hasText(rechargeDto.getOrderNO())) {
            return CommonResponse.builder().result(1).status(1).msg("订单号为空").build();
        }
        if (rechargeDto.getAmount() == null || rechargeDto.getAmount().compareTo(new BigDecimal("0.01")) < 0) {
            return CommonResponse.builder().result(1).status(1).msg("充值金额有误").build();
        }
        //默认 网银一键支付
        if ("YJZF-NET-B2C".equals(rechargeDto.getFrpCode())) {
            if (!StringUtils.hasText(rechargeDto.getBankCardNo())) {
                return CommonResponse.builder().result(1).status(1).msg("银行卡号为空").build();
            }
            if (!StringUtils.hasText(rechargeDto.getPhone())) {
                return CommonResponse.builder().result(1).status(1).msg("预留手机号为空").build();
            }
            if (!StringUtils.hasText(rechargeDto.getUserName())) {
                return CommonResponse.builder().result(1).status(1).msg("用户名为空").build();
            }
            if (!StringUtils.hasText(rechargeDto.getIdCardNO())) {
                return CommonResponse.builder().result(1).status(1).msg("身份证号码为空").build();
            }
        }
        if (!StringUtils.hasText(rechargeDto.getResponseUrl())) {
            return CommonResponse.builder().result(1).status(1).msg("回调地址为空").build();
        }
        TradeRecordRsDto recordRsDto = new TradeRecordRsDto();
        recordRsDto.setUserId(rechargeDto.getUserId());
        recordRsDto.setCardNo(rechargeDto.getBankCardNo());
        recordRsDto.setAmount(rechargeDto.getAmount());
        recordRsDto.setOrderNo(rechargeDto.getOrderNO());
        recordRsDto.setSource(rechargeDto.getSource());
        //交易类型（0:充值  1:提现）
        recordRsDto.setType(0);
        recordRsDto.setResponseUrl(rechargeDto.getResponseUrl());
        TTradeRecord tradeRecord = tradeService.getTradeRecord(rechargeDto.getOrderNO());
        if (tradeRecord == null) {
            tradeService.saveTradeRecord(recordRsDto, new Long(-1));
        } else if (tradeRecord.getStatus() == 0) {
            TTradeRecordWithBLOBs tradeRecordWithBLOBs = new TTradeRecordWithBLOBs();
            tradeRecordWithBLOBs.setId(tradeRecord.getId());
            tradeRecordWithBLOBs.setAmount(recordRsDto.getAmount());
            tradeRecordWithBLOBs.setUserId(recordRsDto.getUserId());
            tradeRecordWithBLOBs.setOrderNo(recordRsDto.getOrderNo());
            tradeRecordWithBLOBs.setOrderId(recordRsDto.getOrderId());
            tradeRecordWithBLOBs.setType(recordRsDto.getType());
            tradeRecordWithBLOBs.setSource(recordRsDto.getSource());
            tradeRecordWithBLOBs.setCallbackUrl(recordRsDto.getResponseUrl());
            tradeRecordWithBLOBs.setReqMsg(recordRsDto.getReqMsg());
            tradeRecordWithBLOBs.setCardMerchantId(new Long(-1));
            tradeRecordWithBLOBs.setUpdateTime(new Date());
            tradeService.updateTradeRecord(tradeRecordWithBLOBs);
        } else {
            return CommonResponse.builder().result(1).status(1).msg("订单号重复").build();
        }

        IPayService payClient = PayClientServiceFactor.CreatePayClient(PaymentType.yibao);
        return payClient.getYeePayUrl(rechargeDto);
    }

    /**
     * 网银充值订单查询
     *
     * @param orderNo
     * @return
     */
    @Override
    public CommonResponse yeePayRechargeRecord(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            return CommonResponse.builder().result(1).status(1).msg("订单号不能为空").build();
        }
        TTradeRecord record = tradeService.getTradeRecord(orderNo);
        if (record == null) {
            return CommonResponse.builder().result(1).status(1).msg("未找到相应订单记录").build();
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(PaymentType.yibao);
        return payClient.yeePayRechargeRecord(orderNo);
    }

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    @Override
    public CommonResponse yeePayBankFrpList() {
        return payService.yeePayBankFrpList();
    }
}
