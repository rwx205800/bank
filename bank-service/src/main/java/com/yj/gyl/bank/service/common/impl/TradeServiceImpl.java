package com.yj.gyl.bank.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.yj.base.alimq.producer.AliMQProducerTemplate;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dao.MyOrderMapper;
import com.yj.gyl.bank.dao.TTradeNotifyMapper;
import com.yj.gyl.bank.dao.TTradeRecordMapper;
import com.yj.gyl.bank.dao.TTransferRecordMapper;
import com.yj.gyl.bank.handler.common.TradeTypeEnum;
import com.yj.gyl.bank.handler.impl.YbCallBachHandlerImpl;
import com.yj.gyl.bank.model.*;
import com.yj.gyl.bank.mq.TransactionPayMessage;
import com.yj.gyl.bank.mq.TransactionPayResult;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import com.yj.gyl.bank.service.common.ITradeService;
import com.yj.gyl.bank.service.yibao.YeepayService;
import com.yj.gyl.composer.channel.api.IGYLTradeApi;
import com.yj.gyl.composer.channel.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renfei on 2017/12/19.
 */
@Service
@Transactional
public class TradeServiceImpl implements ITradeService {
    private Logger logger = LoggerFactory.getLogger(YbCallBachHandlerImpl.class);

    @Autowired
    private TTradeRecordMapper tradeRecordMapper;
    @Autowired
    private TTradeNotifyMapper tradeNotifyMapper;
    @Autowired
    private TTransferRecordMapper transferRecordMapper;
    @Autowired
    private MyOrderMapper orderMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AliMQProducerTemplate template;
    @Autowired
    private IGYLTradeApi tradeApi;

    @Override
    public void saveTradeRecord(TradeRecordRsDto recordRsDto, Long merchantId) {
        TTradeRecordWithBLOBs record = new TTradeRecordWithBLOBs();
        record.setUserId(recordRsDto.getUserId());
        record.setCardMerchantId(merchantId);
        record.setOrderNo(recordRsDto.getOrderNo());
        record.setOrderId(recordRsDto.getOrderId());
        record.setAmount(recordRsDto.getAmount());
        record.setType(recordRsDto.getType());
        record.setCallbackUrl(recordRsDto.getResponseUrl());
        record.setCreateTime(new Date());
        record.setReqMsg(recordRsDto.getReqMsg());
        record.setSource(recordRsDto.getSource());
        tradeRecordMapper.insertSelective(record);

    }

    @Override
    public TTradeRecord getTradeRecord(String orderNo) {
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andOrderNoEqualTo(orderNo);
        List<TTradeRecord> list = tradeRecordMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public TTransferRecord getTransferRecord(String orderNo) {
        TTransferRecordExample example = new TTransferRecordExample();
        example.or().andOrderNoEqualTo(orderNo);
        List<TTransferRecord> list = transferRecordMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TTradeRecord getTradeRecord(Long uId, String orderNo) {
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andUserIdEqualTo(uId).andOrderNoEqualTo(orderNo);
        List<TTradeRecord> list = tradeRecordMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TTradeRecordWithBLOBs getTradeRecordWithBLOBs(Long uId, String orderNo) {
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andUserIdEqualTo(uId).andOrderNoEqualTo(orderNo);
        List<TTradeRecordWithBLOBs> list = tradeRecordMapper.selectByExampleWithBLOBs(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public void updateTradeRecord2(String orderId, String requestno, String respMsg, int status, int chainStatus) {
        TTradeRecordWithBLOBs record = new TTradeRecordWithBLOBs();
        record.setRespMsg(respMsg);
        record.setStatus(status);
        record.setOrderId(orderId);
        record.setChainStatus(chainStatus);
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andOrderNoEqualTo(requestno);
        logger.info("【更新{}交易记录：{}】", requestno, JSONObject.toJSONString(example));
        logger.info("【更新{}交易记录：{}】", requestno, JSONObject.toJSONString(record));
        int res = tradeRecordMapper.updateByExampleSelective(record, example);
        logger.info("【更新{}交易记录结果：{}】", requestno, res);
    }

    public void updateTransferRecord(String orderNo,String respMsg,int status,int chainStauts){
        TTransferRecord record = new TTransferRecord();
        record.setChainStatus(chainStauts);
        record.setStatus(status);
        record.setMessage(respMsg);
        TTransferRecordExample example = new TTransferRecordExample();
        example.or().andOrderNoEqualTo(orderNo);
        transferRecordMapper.updateByExampleSelective(record,example);
    }


    @Override
    public void updateTradeRecord(long tradeId, String requestno, String respMsg, int status, int notifyStatus) {
        TTradeRecordWithBLOBs record = new TTradeRecordWithBLOBs();
        record.setRespMsg(respMsg);
        record.setStatus(status);
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andOrderNoEqualTo(requestno);
        logger.info("【更新{}交易记录：{}】", requestno, JSONObject.toJSONString(example));
        logger.info("【更新{}交易记录：{}】", requestno, JSONObject.toJSONString(record));
        int res = tradeRecordMapper.updateByExampleSelective(record, example);
        logger.info("【更新{}交易记录结果：{}】", requestno, res);

        TTradeNotifyExample tTradeNotifyExample = new TTradeNotifyExample();
        tTradeNotifyExample.or().andTradeIdEqualTo(tradeId);
        long l = tradeNotifyMapper.countByExample(tTradeNotifyExample);
        if (l > 0) {
            return;
        }
        TTradeNotify notify = new TTradeNotify();
        notify.setTradeId(tradeId);
        notify.setRetryTime(new Date());
        notify.setCreateTime(new Date());
        notify.setStatus(notifyStatus);
        tradeNotifyMapper.insertSelective(notify);
    }

    @Override
    public void updateTradeRecord(TTradeRecordWithBLOBs tradeRecord) {
        tradeRecordMapper.updateByPrimaryKeySelective(tradeRecord);
    }

    @Override
    public void updateTradeRecordByStatus(TTradeRecordWithBLOBs tradeRecord) {
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andStatusEqualTo(0).andIdEqualTo(tradeRecord.getId());
        tradeRecordMapper.updateByExampleSelective(tradeRecord, example);
    }

    @Override
    public void updateTradeRecordByOrderNo(TTradeRecordWithBLOBs tradeRecord, String orderNo) {
        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andOrderNoEqualTo(orderNo);
        tradeRecordMapper.updateByExampleSelective(tradeRecord, example);
    }

    @Override
    public void withDrawTradeRecord(Map<String, String> result) {
        String orderNo = result.get("requestno");
        Long id = orderMapper.lockTradeRecord(orderNo);
        if (id == null || id <= 0) {
            logger.error("【提现回调记录不存在：{}】", JSONObject.toJSONString(result));
            return;
        }
        TTradeRecord withdrawFlow = getTradeRecord(orderNo);
        if (withdrawFlow != null && withdrawFlow.getStatus() == 0) {
            TransactionPayMessage payMessage = new TransactionPayMessage();
            payMessage.setUserId(withdrawFlow.getUserId());
            payMessage.setMoney(withdrawFlow.getAmount());
            payMessage.setOrderNo(withdrawFlow.getOrderNo());
            payMessage.setType(TradeTypeEnum.withDraw.getValue());
            payMessage.setSource(withdrawFlow.getSource());
            payMessage.setCreateTime(new Date());
            int status = 0;
            if ("WITHDRAW_SUCCESS".equals(result.get("status").toString())) {
                status = 1;
                payMessage.setStatus(1);
            } else {
                status = 2;
                payMessage.setStatus(2);
            }
            template.saveMessage(TransactionPayResult.TRANSACTION_PAY_WITHDRAW_RESULT, payMessage);
            int notifyStatus = tradeNotify(withdrawFlow, status);
            logger.info("提现订单号：{},记录状态{}", orderNo, status);
            updateTradeRecord(withdrawFlow.getId(), orderNo, JSONObject.toJSONString(result), status, notifyStatus);
        } else {
            logger.error("【提现回调记录查询有误或者再次通知：{}】", JSONObject.toJSONString(withdrawFlow));
        }
    }

    @Override
    public void rechargeTradeRecord(Map<String, String> result) {
        String orderNo = "979349843602444288";
        if (result != null)
            orderNo = result.get("requestno");
        Long id = orderMapper.lockTradeRecord(orderNo);
        if (id == null || id <= 0) {
            logger.error("【充值回调记录不存在：{}】", JSONObject.toJSONString(result));
            return;
        }
       /* for(int i=0;i<10;i++) {
            logger.info("order:"+orderNo+",num:"+i);
        	try {
                // 括号内的参数是毫秒值,线程休眠1s
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        	if(i==9) {
        		return ;
        	}
        }*/
        TTradeRecord tradeRecord = getTradeRecord(orderNo);
        int status = 0;
        if (tradeRecord != null && tradeRecord.getStatus() == 0) {
            logger.info("【充值回调更新】");
            TransactionPayMessage payMessage = new TransactionPayMessage();
            payMessage.setUserId(tradeRecord.getUserId());
            payMessage.setMoney(tradeRecord.getAmount());
            payMessage.setOrderNo(tradeRecord.getOrderNo());
            payMessage.setType(TradeTypeEnum.recharge.getValue());
            payMessage.setSource(tradeRecord.getSource());
            payMessage.setCreateTime(new Date());
            if ("PAY_SUCCESS".equals(result.get("status").toString())) {
                status = 1;
                payMessage.setStatus(1);
            } else {
                status = 2;
                payMessage.setStatus(2);
            }
            template.saveMessage(TransactionPayResult.TRANSACTION_PAY_RECHARGE_RESULT, payMessage);
            int notifyStatus = tradeNotify(tradeRecord, status);
            logger.info("充值订单号:{},记录状态:{}", orderNo, status);
            updateTradeRecord(tradeRecord.getId(), orderNo, JSONObject.toJSONString(result), status, notifyStatus);
        } else {
            logger.error("【充值回调记录查询有误或者再次通知：{}】", JSONObject.toJSONString(tradeRecord));
        }
    }

    @Override
    public void yopRechargeTradeRecord(Map<String, String> result) {
        String orderNo = "979349843602444288";
        if (result != null) {
            orderNo = result.get("orderId");
        }
        String uniqueOrderNo = result.get("uniqueOrderNo");
        Long id = orderMapper.lockTradeRecord(orderNo);
        if (id == null || id <= 0) {
            logger.error("【充值回调记录不存在：{}】", JSONObject.toJSONString(result));
            return;
        }
        TTradeRecord tradeRecord = getTradeRecord(orderNo);
        int status = 0;
        int chainStatus = 0;
        if (tradeRecord != null && tradeRecord.getStatus() == 0) {
            if ("SUCCESS".equals(result.get("status").toString())) {
                status = 1;
                SuccessRecharge successRecharge = new SuccessRecharge();
                successRecharge.setContractId(orderNo);
                CommonResponse response = tradeApi.successRecharge(successRecharge);
                if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
                    chainStatus = response.getResult();
                } else {
                    chainStatus = 1;
                }
            } else {
                status = 2;
                FailRecharge failRecharge = new FailRecharge();
                failRecharge.setContractId(orderNo);
                CommonResponse response = tradeApi.failRecharge(failRecharge);
                if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
                    chainStatus = response.getResult();
                } else {
                    chainStatus = 1;
                }
            }
            logger.info("充值订单号:{},记录状态:{}", orderNo, status);
            updateTradeRecord2(uniqueOrderNo, orderNo, JSONObject.toJSONString(result), status, chainStatus);
        } else {
            logger.error("【充值回调记录查询有误或者再次通知：{}】", JSONObject.toJSONString(tradeRecord));
        }
    }

    @Override
    public void yopWithDrawTradeRecord(Map<String, String> result) {
        String orderNo = result.get("orderId");
        String batchNo = result.get("batchNo");
        Long id = orderMapper.lockTransferRecord(orderNo);
        if (id == null || id <= 0) {
            logger.error("【代付代发记录不存在：{}】", JSONObject.toJSONString(result));
            return;
        }
        TTransferRecord withdrawFlow = getTransferRecord(orderNo);
        if (withdrawFlow != null && withdrawFlow.getStatus() == 0) {
            String transferStatusCode = result.get("transferStatusCode"); //打款状态码
            String bankTrxStatusCode = result.get("bankTrxStatusCode");//银行处理状态
            int status = 0;
            int chainStatus = 0;
            if ("SUCCESS".equals(YeepayService.getStatus(transferStatusCode, bankTrxStatusCode))) {
                status = 1;
                SuccessCash successCash = new SuccessCash();
                successCash.setContractId(orderNo);
                successCash.setBankOrderNo(batchNo);
                successCash.setChannelOrderNo("YB");
                CommonResponse response = tradeApi.successCash(successCash);
                if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
                    chainStatus = response.getResult();
                } else {
                    chainStatus = 1;
                }
            } else if ("FAIL".equals(YeepayService.getStatus(transferStatusCode, bankTrxStatusCode))) {
                status = 2;
                FailCash failCash = new FailCash();
                failCash.setContractId(orderNo);
                CommonResponse response = tradeApi.failCash(failCash);
                if (response != null && (response.getResult() == 0 || response.getResult() == 2)) {//上链成功
                    chainStatus = response.getResult();
                } else {
                    chainStatus = 1;
                }
            }
            logger.info("提现订单号：{},记录状态{}", orderNo, status);
            updateTransferRecord(orderNo, JSONObject.toJSONString(result), status, chainStatus);
        } else {
            logger.error("【提现回调记录查询有误或者再次通知：{}】", JSONObject.toJSONString(withdrawFlow));
        }
    }





    public int tradeNotify(TTradeRecord tradeRecord, int status) {
        Map<String, Object> map = new HashMap();
        map.put("userId", tradeRecord.getUserId());
        map.put("orderNo", tradeRecord.getOrderNo());
        map.put("result", status == 1 ? 0 : 1);
        try {
            String payResult = restTemplate.postForObject(tradeRecord.getCallbackUrl(), map, String.class);
            if ("SUCCESS".equals(payResult)) {
                return 1;
            }
        } catch (Exception e) {
            logger.info("-------------回调通知失败----------{}", e.getMessage(), e);
            return 2;
        }
        return 2;
    }

    /**
     * 解析易宝网易充值回调结果
     *
     * @param result
     */
    @Override
    public void yeePayRecharge(Map<String, String> result) {
        // 支付结果
        String r1_Code = result.get("r1_Code") == null ? "" : result.get("r1_Code");
        // 易宝交易流水号
        String r2_TrxId = result.get("r2_TrxId") == null ? "" : result.get("r2_TrxId");
        // 支付金额
        String r3_Amt = result.get("r3_Amt") == null ? "" : result.get("r3_Amt");
        // 商户订单号
        String r6_Order = result.get("r6_Order") == null ? "" : result.get("r6_Order");
//        orderMapper.lockTradeRecord(r6_Order);

        TTradeRecordExample example = new TTradeRecordExample();
        example.or().andOrderNoEqualTo(r6_Order);
        List<TTradeRecord> list = tradeRecordMapper.selectByExample(example);
        if (list == null || list.size() < 1) {
            logger.info("未找到相应订单记录，易宝网易充值回调：{} ", result);
            return;
        }
        TTradeRecord record = list.get(0);
        if (record.getStatus() != 0) {
            logger.error("订单状态已经改变 ", result);
            return;
        }
        if (new BigDecimal(r3_Amt).compareTo(record.getAmount()) != 0) {
            logger.info("支付金额不对，易宝网易充值回调：{} ", result);
            return;
        }
        TTradeRecordWithBLOBs tradeRecord = new TTradeRecordWithBLOBs();
        tradeRecord.setId(record.getId());
        tradeRecord.setOrderId(r2_TrxId);
        tradeRecord.setUpdateTime(new Date());

        TransactionPayMessage payMessage = new TransactionPayMessage();
        int status;
        if ("1".equals(r1_Code)) {
            // 支付成功
            tradeRecord.setStatus(1);
            payMessage.setStatus(1);
            status = 1;
        } else {
            // 失败
            tradeRecord.setStatus(2);
            payMessage.setStatus(2);
            status = 2;
        }
        tradeRecordMapper.updateByPrimaryKeySelective(tradeRecord);
        payMessage.setUserId(record.getUserId());
        payMessage.setMoney(record.getAmount());
        payMessage.setOrderNo(record.getOrderNo());
        payMessage.setType(TradeTypeEnum.recharge.getValue());
        payMessage.setSource(record.getSource());
        payMessage.setCreateTime(new Date());
        template.saveMessage(TransactionPayResult.TRANSACTION_PAY_WITHDRAW_RESULT, payMessage);

        //回调通知结果
        int notifyStatus = tradeNotify(record, status);
        updateTradeRecord(record.getId(), record.getOrderNo(), JSONObject.toJSONString(result), status, notifyStatus);

    }
}
