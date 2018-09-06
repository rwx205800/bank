package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yj.base.common.CommonResponse;
import com.yj.base.common.ConstantEnum;
import com.yj.gyl.bank.dto.TransferSend;
import com.yj.gyl.bank.dto.YopTradeOrderDto;
import com.yj.gyl.bank.dto.YopTransferSendDto;
import com.yj.gyl.bank.handler.IYopHandler;
import com.yj.gyl.bank.model.TTradeRecord;
import com.yj.gyl.bank.model.TTradeRecordWithBLOBs;
import com.yj.gyl.bank.model.TTransferRecord;
import com.yj.gyl.bank.rsdto.TradeOrderQueryRsDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import com.yj.gyl.bank.rsdto.TransferSendQueryRsDto;
import com.yj.gyl.bank.service.common.ITradeService;
import com.yj.gyl.bank.service.common.ITransferService;
import com.yj.gyl.bank.service.yibao.YeepayService;
import com.yj.gyl.bank.service.yibao.common.YopConfig;
import com.yj.gyl.bank.service.yibao.util.DateUtils;
import com.yj.gyl.composer.channel.api.IGYLAccountApi;
import com.yj.gyl.composer.channel.api.IGYLTradeApi;
import com.yj.gyl.composer.channel.dto.CashAccount;
import com.yj.gyl.composer.channel.dto.FailCash;
import com.yj.gyl.composer.channel.dto.StartCash;
import com.yj.gyl.composer.channel.dto.StartRecharge;
import com.yj.snowflake.api.ISnowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by renfei on 2018/6/27.
 */
@Component
public class YopHandlerImpl implements IYopHandler {
    private static final Logger logger = LoggerFactory.getLogger(RechargeHandlerImpl.class);
    @Autowired
    private ISnowflake snowflake;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IGYLTradeApi tradeApi;
    @Autowired
    private ITransferService transferService;
    @Autowired
    private IGYLAccountApi accountApi;

    @Override
    public CommonResponse tradeOrder(YopTradeOrderDto tradeOrderDto, Long userId) {
        logger.info("【网银充值入参：{}】", JSONObject.toJSONString(tradeOrderDto));
//        String orderId = snowflake.getSerialNo();
        String orderId = new Date().getTime() + "";
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("orderAmount", new BigDecimal(tradeOrderDto.getOrderAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        params.put("notifyUrl", YopConfig.getTradeOrderCallback());
        params.put("goodsParamExt", "{\"goodsName\":\"易老板" + "\",\"goodsDesc\":\"票据支付" + "\"}");

        Map<String, String> result = new HashMap<>();
        try {
            result = YeepayService.requestYOP(params, YopConfig.getTradeOrderURI(), YeepayService.TRADEORDER);
            logger.info("【网银充值下单结果：{}】", JSONObject.toJSONString(result));
        } catch (Exception e) {
            logger.error("【网银充值下单异常】",e);
            return CommonResponse.builder().result(1).status(1).msg("网银充值下单失败,请稍后再试!").build();
        }
        String token = result.get("token");
        String codeRe = result.get("code");
        String uniqueOrderNo = result.get("uniqueOrderNo");
        if (!"OPR00000".equals(codeRe)) {
            String message = result.get("message");
            logger.info("【网银充值下单失败：{}】", message);
            return CommonResponse.builder().result(1).status(1).msg(message).build();
        }

        params.put("parentMerchantNo", YopConfig.getParentMerchantNo());
        params.put("merchantNo", YopConfig.getMerchantNo());
        params.put("orderId", orderId);
        params.put("token", token);
        params.put("timestamp", new Date().getTime() + "");
        params.put("directPayType", "");
        params.put("cardType", "");
        params.put("userNo", userId.toString());
        params.put("userType", "USER_ID");
        params.put("ext", "");

        String url = null;
        try {
            logger.info("【网银充值--拼接支付链接入参：{}】", JSONObject.toJSONString(params));
            url = YeepayService.getUrl(params);
            logger.info("【网银充值--拼接支付链接返回url：{}】", url);
        } catch (UnsupportedEncodingException e) {
            logger.error("【网银充值--拼接支付链接异常】",e);
            return CommonResponse.builder().result(1).status(1).msg("网银充值失败,请稍后再试!").build();
        }
        CommonResponse commonResponse = saveRecord(tradeOrderDto, orderId, userId, uniqueOrderNo);
        if (commonResponse._isOk()) {
            StartRecharge recharge = new StartRecharge();
            recharge.setAmount(Double.valueOf(tradeOrderDto.getOrderAmount()));
            recharge.setContractId(orderId);
            recharge.setEndTime(DateUtils.getDayAfterByDay(new Date(), 1));
            recharge.setTradeDesc("网银充值");
            logger.info("网银充值--发起支付上链:{}", JSONObject.toJSONString(recharge));
            CommonResponse response = tradeApi.startRecharge(recharge);
            logger.info("网银充值--发起支付上链结果:{}", JSONObject.toJSONString(response));
            TTradeRecordWithBLOBs record = new TTradeRecordWithBLOBs();
            if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
                record.setChainStatus(response.getResult());
                tradeService.updateTradeRecordByOrderNo(record, orderId);
                return CommonResponse.builder().result(0).status(0).msg("发起充值成功").data(url).build();
            } else {
                record.setChainStatus(1);
                tradeService.updateTradeRecordByOrderNo(record, orderId);
                return CommonResponse.builder().result(1).status(1).msg("发起充值失败，请稍后再试！").build();
            }
        } else {
            return commonResponse;
        }
    }

    @Override
    public CommonResponse transferSend(YopTransferSendDto transferDto, Long userId) {
        logger.info("【网银代付代发入参：{}】", JSONObject.toJSONString(transferDto));
        CommonResponse<CashAccount> cashResponse = accountApi.myCashAccount();
        logger.info("【网银代付代发--查询账户余额：{}】", JSONObject.toJSONString(cashResponse));
        if (cashResponse._isFailed()) {
            return CommonResponse.builder().result(1).status(1).msg("发起提现失败，请稍后再试！").build();
        }
        CashAccount cashAccount = cashResponse.getData();
        BigDecimal amount = new BigDecimal(transferDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP);
        if (cashAccount == null || new BigDecimal(cashAccount.getAvlAmount()).compareTo(amount) == -1) { //可提现金额不足
            return CommonResponse.builder().result(1).status(1).msg("可提现金额不足！").build();
        }
        String orderId = snowflake.getSerialNo();
        String batchNo = DateUtils.getFlowTimeDate() + new Random().nextInt(999999999);

        StartCash startCash = new StartCash();
        startCash.setContractId(orderId);
        startCash.setTradeDesc(userId + "提现" + transferDto.getAmount());
        startCash.setAmount(Double.valueOf(amount.toString()));
        startCash.setEndTime(DateUtils.getDayAfterByDay(new Date(), 1));
        startCash.setBankCard(transferDto.getAccountNumber());
        startCash.setBankCode(transferDto.getBankCode());
        logger.info("【网银代付代发--发起提现上链:{}】", JSONObject.toJSONString(startCash));
        CommonResponse response = tradeApi.startCash(startCash);
        logger.info("【网银充值--发起支付上链结果:{}】", JSONObject.toJSONString(response));
        TTransferRecord transferRecord = new TTransferRecord();
        transferRecord.setProjectId(userId);
        transferRecord.setOrderNo(orderId);
        transferRecord.setBatchNo(batchNo);
        transferService.saveTransferRecord(userId, amount, 0, batchNo, orderId);//保存数据库记录
        if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
            transferRecord.setChainStatus(response.getResult());
            transferRecord.setMessage(response.getMsg());
            transferService.updateTransferRecord(transferRecord);
            //发起提现
//            CommonResponse commonResponse = cash(transferDto, orderId, batchNo);
            TransferSend transferSend = new TransferSend();
            transferSend.setOrderId(orderId);
            transferSend.setBatchNo(batchNo);
            transferSend.setAmount(amount);
            transferSend.setAccountName(transferDto.getAccountName());
            transferSend.setAccountNumber(transferDto.getAccountNumber());
            transferSend.setBankCode(transferDto.getBankCode());
            transferSend.setBankName("");
            transferSend.setProvince("");
            transferSend.setCity("");
            CommonResponse commonResponse = transferService.transferSingle(transferSend);
            if (commonResponse._isFailed()){ //失败需要 解冻资金
                int chainStatus = 0;
                transferRecord.setStatus(2);//失败
                FailCash failCash = new FailCash();
                failCash.setContractId(orderId);
                CommonResponse failResponse = tradeApi.failCash(failCash);
                if (failResponse != null && (failResponse.getResult() == 0 || failResponse.getResult() == 2)) {//上链成功
                    chainStatus = response.getResult();
                } else {
                    chainStatus = 1;
                }
                transferRecord.setChainStatus(chainStatus);
                transferRecord.setMessage(failResponse.getMsg());
                transferService.updateTransferRecord(transferRecord);
            }
            return commonResponse;
        } else {
            transferRecord.setChainStatus(response.getResult());
            transferRecord.setMessage(response.getMsg());
            transferService.updateTransferRecord(transferRecord);
            return CommonResponse.builder().result(1).status(1).msg("发起提现失败，请稍后再试！").build();
        }
    }

    public CommonResponse cash(YopTransferSendDto transferDto, String orderId, String batchNo) {
        Map<String, Object> params = YeepayService.getParams(transferDto, orderId, batchNo);
        String uri = YopConfig.getPaymentURI();
        Map<String, Object> yopresponsemap = null;
        try {
            logger.info("【网银代付代发--发起请求入参：{}】", JSONObject.toJSONString(params));
            yopresponsemap = YeepayService.yeepayYOP(params, uri);
            logger.info("【网银代付代发--发起请求结果：{}】", JSONObject.toJSONString(yopresponsemap));
        } catch (IOException e) {
            logger.error("【网银代付代发--发起提现异常】",e);
            return CommonResponse.builder().result(1).status(1).msg("发起提现失败!").build();
        }
        String errorCode = (String) yopresponsemap.get("errorCode");
        if ("BAC001".equals(errorCode)) {
            String transferStatusCode = (String) yopresponsemap.get("transferStatusCode");
            String status = YeepayService.getStatus(transferStatusCode);
            if ("WAIT".equals(status)) { //成功 保存记录
                return CommonResponse.builder().result(0).status(0).msg("发起提现成功").data(yopresponsemap).build();
            }
        }
        return CommonResponse.builder().result(1).status(1).msg("发起提现失败!").build();
    }

    @Override
    public CommonResponse<TradeOrderQueryRsDto> tradeOrderQuery(String orderNo, Long userId) {
        logger.info("【易宝{}充值订单{}查询】", userId, orderNo);
        TTradeRecord record = tradeService.getTradeRecord(userId, orderNo);
        if (record == null) {
            logger.error("【订单号{}不存在】", orderNo);
            return CommonResponse.<TradeOrderQueryRsDto>builder().result(1).status(1).msg("订单号不存在!").build();
        }
        Map<String, String> params = new HashMap<>();
        params.put("orderId", orderNo);
        params.put("uniqueOrderNo", record.getOrderId());
        Map<String, String> result = new HashMap<>();
        try {
            result = YeepayService.requestYOP(params, YopConfig.getOrderQueryURI(), YeepayService.ORDERQUERY);
            logger.info("【易宝{}充值订单{}查询结果：{}】", userId, orderNo, JSONObject.toJSONString(result));
        } catch (IOException e) {
            logger.error("【订单号{}查询异常】", orderNo,e);
            return CommonResponse.<TradeOrderQueryRsDto>builder().result(1).status(1).msg("查询失败，请稍后再试!").build();
        }
        String code = result.get("code");
        String message = result.get("message");
        String payAmount = result.get("payAmount");
        String status = result.get("status");
        String paySuccessDate = result.get("paySuccessDate");
        if ("OPR00000".equals(code)) {
            TradeOrderQueryRsDto rsDto = new TradeOrderQueryRsDto();
            rsDto.setOrderId(orderNo);
            rsDto.setAmount(payAmount);
            rsDto.setPaySuccessDate(paySuccessDate);
            rsDto.setStatus(status);
            return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, rsDto);
        }
        return CommonResponse.<TradeOrderQueryRsDto>builder().result(1).status(1).msg(message).build();
    }

    @Override
    public CommonResponse<TransferSendQueryRsDto> transferSendQuery(String orderNo, Long userId) {
        logger.info("【易宝{}提现{}查询】", userId, orderNo);
        TTransferRecord record = transferService.getTransferRecord(userId, orderNo);
        if (record == null) {
            logger.error("【订单号{}不存在】", orderNo);
            return CommonResponse.<TransferSendQueryRsDto>builder().result(1).status(1).msg("订单号不存在!").build();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("customerNumber", YopConfig.getMerchantNo());
        params.put("batchNo", record.getBatchNo());
        params.put("product", "RJT");
        params.put("orderId", orderNo);
        Map<String, Object> yopresponsemap = null;
        try {
            yopresponsemap = YeepayService.yeepayYOP(params, YopConfig.getPaymentqueryURI());
            logger.info("【易宝{}提现{}查询结果：{}】", userId, orderNo, JSONObject.toJSONString(yopresponsemap));
        } catch (IOException e) {
            logger.error("【订单号{}查询异常】", orderNo,e);
            return CommonResponse.<TransferSendQueryRsDto>builder().result(1).status(1).msg("查询失败，请稍后再试!").build();
        }
        TransferSendQueryRsDto rsDto = new TransferSendQueryRsDto();
        if (yopresponsemap.get("list") != null) {
            String list = (String) yopresponsemap.get("list");
            JSONArray jsonArray = JSONArray.parseArray(list);
            if (jsonArray != null && jsonArray.size() > 0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String bankTrxStatusCode = jsonObject.getString("bankTrxStatusCode");
                String transferStatusCode = jsonObject.getString("transferStatusCode");
                rsDto.setStatus(YeepayService.getStatus(transferStatusCode, bankTrxStatusCode));
                rsDto.setAmount(jsonObject.getString("amount"));
                rsDto.setOrderId(orderNo);
                return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, rsDto);
            }
        }
        String errorMsg = (String) yopresponsemap.get("errorMsg");
        return CommonResponse.<TransferSendQueryRsDto>builder().result(1).status(1).msg(errorMsg).build();
    }

    @Override
    public CommonResponse<TransferSendQueryRsDto> transferSendDetailQuery(String orderNo, Long userId) {
        logger.info("【易宝{}提现{}查询】", userId, orderNo);
        TTransferRecord record = transferService.getTransferRecord(userId, orderNo);
        if (record == null) {
            logger.error("【订单号{}不存在】", orderNo);
            return CommonResponse.<TransferSendQueryRsDto>builder().result(1).status(1).msg("订单号不存在!").build();
        }
        CommonResponse commonResponse = transferService.transAcountDetail(orderNo, record.getBatchNo(), 10);
        logger.info("【易宝{}提现{}查询结果：{}】", userId, orderNo, JSONObject.toJSONString(commonResponse));
        String status = "0";
        if (commonResponse != null && commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) {
            status = "1";
        } else if (commonResponse.getResult() == 1) {
            status = "2";
        }
        TransferSendQueryRsDto rsDto = new TransferSendQueryRsDto();
        rsDto.setOrderId(orderNo);
        rsDto.setAmount(record.getAmount().toString());
        rsDto.setStatus(status);
        return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, rsDto);
    }

    public CommonResponse saveRecord(YopTradeOrderDto tradeOrderDto, String orderId, Long userId, String uniqueOrderNo) {
        TradeRecordRsDto recordRsDto = new TradeRecordRsDto();
        recordRsDto.setUserId(userId);
        recordRsDto.setAmount(new BigDecimal(tradeOrderDto.getOrderAmount()));
        recordRsDto.setOrderNo(orderId);
        recordRsDto.setOrderId(uniqueOrderNo);
        recordRsDto.setSource("10");
        recordRsDto.setType(0); //交易类型（0:充值  1:提现）
        TTradeRecord tradeRecord = tradeService.getTradeRecord(orderId);
        if (tradeRecord == null) {
            tradeService.saveTradeRecord(recordRsDto, new Long(0));
            return CommonResponse.builder().result(0).status(0).msg("ok").build();
        } else if (tradeRecord.getStatus() == 0) {
            TTradeRecordWithBLOBs tradeRecordWithBLOBs = new TTradeRecordWithBLOBs();
            tradeRecordWithBLOBs.setId(tradeRecord.getId());
            tradeRecordWithBLOBs.setAmount(recordRsDto.getAmount());
            tradeRecordWithBLOBs.setUserId(recordRsDto.getUserId());
            tradeRecordWithBLOBs.setOrderNo(recordRsDto.getOrderNo());
            tradeRecordWithBLOBs.setOrderId(recordRsDto.getOrderId());
            tradeRecordWithBLOBs.setType(recordRsDto.getType());
            tradeRecordWithBLOBs.setSource(recordRsDto.getSource());
            tradeRecordWithBLOBs.setReqMsg(recordRsDto.getReqMsg());
            tradeRecordWithBLOBs.setCardMerchantId(new Long(0));
            tradeService.updateTradeRecord(tradeRecordWithBLOBs);
            return CommonResponse.builder().result(0).status(0).msg("ok").build();
        } else {
            return CommonResponse.builder().result(1).status(1).msg("订单号重复").build();
        }
    }

}
