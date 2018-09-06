package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yj.gyl.bank.handler.IYbCallBackHandler;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.service.common.IMerchantService;
import com.yj.gyl.bank.service.common.ITradeService;
import com.yj.gyl.bank.service.yibao.util.AES;
import com.yj.gyl.bank.service.yibao.util.CheckUtils;
import com.yj.gyl.bank.service.yibao.util.EncryUtil;
import com.yj.gyl.bank.service.yibao.util.RSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by renfei on 2017/12/19.
 */
@Component
public class YbCallBachHandlerImpl implements IYbCallBackHandler {
    private Logger logger = LoggerFactory.getLogger(YbCallBachHandlerImpl.class);
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IMerchantService merchantService;

    @Override
    public void callbackRecharge(Long merchantId) throws Exception {
        logger.info("-------------易宝充值回调----------商户号ID:{}", merchantId);
        JSONObject jsonobject = getParams(request);
        Map<String, String> result = new HashMap<String, String>();
        String dataFromYeepay = jsonobject.getString("data");
        String encryptkeyFromYeepay = jsonobject.getString("encryptkey");
        logger.info("[充值dataFromYeepay]----{}", dataFromYeepay);
        logger.info("[充值encryptkeyFromYeepay]----{}", encryptkeyFromYeepay);
        boolean signMatch = false;
        TMerchant tMerchant = merchantService.getMerchantById(merchantId);
        signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay,
                tMerchant.getPublicKey(), tMerchant.getMerchantPrivateKey());
        if (!signMatch) {
            logger.error("签名不匹配");
            return;
        }
        String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, tMerchant.getMerchantPrivateKey());
        String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
        result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {
        });
        logger.info("[易宝充值回调request]----{}", result);
        tradeService.rechargeTradeRecord(result);
        //响应易宝
        response.getWriter().write("SUCCESS");
    }

    @Override
    public void callbackWithDraw(Long merchantId) throws Exception {
        logger.info("-------------易宝提现回调----------商户号ID:{}", merchantId);
        JSONObject jsonobject = getParams(request);
        Map<String, String> result = new HashMap<String, String>();
        String dataFromYeepay = jsonobject.get("data").toString();
        String encryptkeyFromYeepay = jsonobject.get("encryptkey").toString();
        logger.info("[提现dataFromYeepay]----" + dataFromYeepay);
        logger.info("[提现encryptkeyFromYeepay]----" + encryptkeyFromYeepay);
        TMerchant tMerchant = merchantService.getMerchantById(merchantId);
        boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay, tMerchant.getPublicKey(), tMerchant.getMerchantPrivateKey());
        if (!signMatch) { //解析签名
            logger.error("签名不匹配");
            return;
        }
        String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, tMerchant.getMerchantPrivateKey());
        String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
        result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {
        });
        logger.info("[易宝提现回调request]----------" + JSON.toJSONString(result));
        tradeService.withDrawTradeRecord(result);
        response.getWriter().write("SUCCESS");
    }

    private JSONObject getParams(HttpServletRequest request) {
        JSONObject params = new JSONObject();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement();
            String value = request.getParameter(key);
            params.put(key, value);
        }
        return params;
    }

    /**
     * 易宝网银充值回调接口
     *
     * @throws Exception
     */
    @Override
    public void callbackEbankRecharge() throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        logger.info("-------------易宝网银充值回调接口----------");
        JSONObject jsonobject = getParams(request);
        logger.info("[易宝网银充值回调]----------" + jsonobject.toJSONString());
        jsonobject.putAll(result);
        // 商户编号
        String p1_MerId = result.get("p1_MerId") == null ? "" : result.get("p1_MerId");
        // 业务类型
        String r0_Cmd = result.get("r0_Cmd") == null ? "" : result.get("r0_Cmd");
        // 支付结果
        String r1_Code = result.get("r1_Code") == null ? "" : result.get("r1_Code");
        // 易宝交易流水号
        String r2_TrxId = result.get("r2_TrxId") == null ? "" : result.get("r2_TrxId");
        // 支付金额
        String r3_Amt = result.get("r3_Amt") == null ? "" : result.get("r3_Amt");
        // 交易币种
        String r4_Cur = result.get("r4_Cur") == null ? "" : result.get("r4_Cur");
        // 商品名称
        String r5_Pid = result.get("r5_Pid") == null ? "" : result.get("r5_Pid");
        // 商户订单号
        String r6_Order = result.get("r6_Order") == null ? "" : result.get("r6_Order");
        // 易宝支付会员
        String r7_Uid = result.get("r7_Uid") == null ? "" : result.get("r7_Uid");
        // 商户扩展信息
        String r8_MP = result.get("r8_MP") == null ? "" : result.get("r8_MP");
        // 通知类型
        String r9_BType = result.get("r9_BType") == null ? "" : result.get("r9_BType");
        // 支付通道编码
        String rb_BankId = result.get("rb_BankId") == null ? "" : result.get("rb_BankId");
        // 银行订单号
        String ro_BankOrderId = result.get("ro_BankOrderId") == null ? "" : result.get("ro_BankOrderId");
        // 支付成功时间
        String rp_PayDate = result.get("rp_PayDate") == null ? "" : result.get("rp_PayDate");
        // 神州行充值卡号
        String rq_CardNo = result.get("rq_CardNo") == null ? "" : result.get("rq_CardNo");
        // 通知时间
        String ru_Trxtime = result.get("ru_Trxtime") == null ? "" : result.get("ru_Trxtime");
        // 用户手续费
        String rq_SourceFee = result.get("rq_SourceFee") == null ? "" : result.get("rq_SourceFee");
        // 商户手续费
        String rq_TargetFee = result.get("rq_TargetFee") == null ? "" : result.get("rq_TargetFee");

        String[] strArr = new String[]{p1_MerId, r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order,
                r7_Uid, r8_MP, r9_BType};

        String hmac_safe = result.get("hmac_safe");
        String hmac = result.get("hmac");
        boolean hmacBool = CheckUtils.verifyCallbackHmac(strArr, hmac);
        if (!hmacBool) {
            logger.error("签名不匹配");
            return;
        }
        boolean safeBool = CheckUtils.verifyCallbackHmac_safe(strArr, hmac_safe);
        if (!safeBool) {
            logger.error("安全签名不匹配");
            return;
        }
        tradeService.yeePayRecharge(result);
        response.getWriter().write("SUCCESS");

    }

}
