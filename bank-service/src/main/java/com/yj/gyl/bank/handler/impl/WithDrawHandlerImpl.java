package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.yj.base.alimq.producer.AliMQProducerTemplate;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.TradeRecordDto;
import com.yj.gyl.bank.dto.WithdrawDto;
import com.yj.gyl.bank.handler.IWithDrawHandler;
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
import com.yj.gyl.bank.service.yibao.common.YiBaoConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hepei
 * @date 2017/12/13 19:06:42
 */
@Component
public class WithDrawHandlerImpl implements IWithDrawHandler {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IBankCardService bankCardService;

    @Autowired
    private IMerchantService merchantService;

    @Autowired
    private IBankCardMerchantService bankCardMerchantService;

    @Autowired
    private ITradeService tradeService;

    @Autowired
    private com.yj.gyl.bank.service.common.IPayService payService;

    @Autowired
    private AliMQProducerTemplate template;

    @Override
    public CommonResponse<TradeRecordRsDto> withdraw(WithdrawDto withdrawDto) {
        logger.info("withdraw" + JSONObject.toJSONString(withdrawDto));
        if (StringUtils.isEmpty(withdrawDto.getUserId())) {
            logger.info("用户id为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_USERID_EMPTY);
        }
        if (StringUtils.isEmpty(withdrawDto.getCardNo())) {
            logger.info("银行卡号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_CARDNO_EMPTY);
        }
        if (withdrawDto.getAmount() == null || withdrawDto.getAmount().compareTo(new BigDecimal(YiBaoConfig.getMinLimit())) < 0) {
            logger.info("提现金额为空或不满足最小提现金额");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_AMOUNT);
        }
        if (StringUtils.isEmpty(withdrawDto.getResponseUrl())) {
            logger.info("请填写回调地址");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_RESPONSEURL);
        }
        if (StringUtils.isEmpty(withdrawDto.getOrderNo())) {
            logger.info("请填写订单号");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_ORDERNO_EMPTY);
        }
        if ("test".equals(YiBaoConfig.getEnvironment())) {
            logger.info("【提现测试金额：0.01】");
            withdrawDto.setAmount(new BigDecimal("0.01"));
        }
        TBankCard bankCard = bankCardService.getUserBankCard(withdrawDto.getUserId(), withdrawDto.getCardNo());
        if (bankCard == null) {
            logger.info("银行卡信息不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_CARD_NOTEXIST);
        }
        TMerchant merchant = merchantService.getMerchantBySource(withdrawDto.getSource());
        if (merchant == null) {
            logger.info("交易通道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_SOURCE_ERROR);
        }
        //根据路由筛选绑卡渠道
        TBankCardMerchant bankCardMerchant = bankCardMerchantService.getMerchant(bankCard.getId(), merchant.getId(), withdrawDto.getSource());
        if (bankCardMerchant == null) {
            logger.info("该银行卡未绑定");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_CARD_NOT_BIND);
        }
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("渠道不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_WITHDRAW_SOURCE_ERROR);
        }
        TTradeRecord tTradeRecord = tradeService.getTradeRecord(withdrawDto.getUserId(), withdrawDto.getOrderNo());
        if (tTradeRecord == null) {
            TradeRecordRsDto rsDto = new TradeRecordRsDto();
            rsDto.setUserId(withdrawDto.getUserId());
            rsDto.setOrderNo(withdrawDto.getOrderNo());
            rsDto.setOrderId("");
            rsDto.setAmount(withdrawDto.getAmount());
            rsDto.setType(TradeTypeEnum.withDraw.getValue());
            rsDto.setResponseUrl(withdrawDto.getResponseUrl());
            rsDto.setReqMsg("");
            rsDto.setSource(withdrawDto.getSource());
            tradeService.saveTradeRecord(rsDto, bankCardMerchant.getId());
        }
        //提现
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse<TradeRecordRsDto> commonResponse = payClient.withdraw(withdrawDto, merchant);
        if (commonResponse != null && commonResponse.getData() != null) {
            TTradeRecordWithBLOBs bloBs = new TTradeRecordWithBLOBs();
            bloBs.setOrderId(commonResponse.getData().getOrderId());
            bloBs.setReqMsg(commonResponse.getData().getReqMsg());
            tradeService.updateTradeRecordByOrderNo(bloBs, withdrawDto.getOrderNo());
        }
        //提现埋点
        if (commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
            TransactionPayMessage payMessage = new TransactionPayMessage();
            payMessage.setUserId(withdrawDto.getUserId());
            payMessage.setMoney(withdrawDto.getAmount());
            payMessage.setOrderNo(withdrawDto.getOrderNo());
            payMessage.setType(TradeTypeEnum.withDraw.getValue());
            payMessage.setSource(withdrawDto.getSource());
            payMessage.setCreateTime(new Date());
            //发起提现
            payMessage.setStatus(0);
            template.saveMessage(TransactionPayResult.TRANSACTION_PAY_WITHDRAW_RESULT, payMessage);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse withdrawRecord(String orderNo) {
        if (StringUtils.isEmpty(orderNo)) {
            logger.info("订单号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_EMPTY);
        }
        TTradeRecord record = tradeService.getTradeRecord(orderNo);
        if (record == null) {
            logger.info("订单号有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_ORDERNO_ERROR);
        }
        TBankCardMerchant merchant = bankCardMerchantService.getBankCardMerchantById(record.getCardMerchantId());
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SEARCH_SOURCE_ERROR);
        }
        TMerchant tMerchant = merchantService.getMerchantById(merchant.getMerchantId());
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        TradeRecordDto recordDto = new TradeRecordDto();
        recordDto.setOrderNo(orderNo);
        recordDto.setOrderId(record.getOrderId());
        return payClient.withdrawRecord(recordDto, tMerchant);
    }
}
