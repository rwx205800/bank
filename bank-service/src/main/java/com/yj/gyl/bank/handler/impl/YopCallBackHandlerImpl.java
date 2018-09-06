package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.yj.gyl.bank.handler.IYopCallBackHandler;
import com.yj.gyl.bank.service.common.ITradeService;
import com.yj.gyl.bank.service.yibao.YeepayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renfei on 2018/6/27.
 */
@Component
public class YopCallBackHandlerImpl implements IYopCallBackHandler {
    private Logger logger = LoggerFactory.getLogger(YopCallBackHandlerImpl.class);

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ITradeService tradeService;

    @Override
    public void tradeOrderCallback() throws Exception {
        logger.info("-------------B2B网银充值回调----------");
        String responseMsg = request.getParameter("response");
        String customerId = request.getParameter("customerIdentification");
        logger.info("【B2B网银充值异步回调验签入参：{}】", JSONObject.toJSONString(responseMsg));
        Map<String, String> result = YeepayService.callback(responseMsg);
        logger.info("【B2B网银充值异步回调验签结果：{}】", JSONObject.toJSONString(result));
        if (result.size() > 0) {
            tradeService.yopRechargeTradeRecord(result);
        }
        response.getWriter().write("SUCCESS");
    }

    @Override
    public void transferSendCallback() throws Exception {
        logger.info("-------------B2B网银代付代发回调----------");
        String responseMsg = request.getParameter("response");
        logger.info("【B2B网银代付代发回调验签入参：{}】", JSONObject.toJSONString(responseMsg));
        Map<String, String> result = YeepayService.callback(responseMsg);
        logger.info("【B2B网银代付代发回调验签结果：{}】", JSONObject.toJSONString(result));
        if (result.size() > 0) {
            tradeService.yopWithDrawTradeRecord(result);
        }
        response.getWriter().write("SUCCESS");
    }

    @Override
    public void tradeOrderCallbackForPage() throws Exception {
        logger.info("-------------B2B网银充值页面回调通知结果----------");
        String merchantNo = request.getParameter("merchantNo");
        String parentMerchantNo = request.getParameter("parentMerchantNo");
        String orderId = request.getParameter("orderId");
        String sign = request.getParameter("sign");
        Map<String, String> responseMap = new HashMap<String, String>();
        responseMap.put("merchantNo", merchantNo);
        responseMap.put("parentMerchantNo", parentMerchantNo);
        responseMap.put("orderId", orderId);
        responseMap.put("sign", sign);

        boolean flag = YeepayService.verifyCallback(responseMap);

        if (!flag) {
            logger.error("验签失败");
            return;
        }
    }
}
