package com.yj.gyl.bank.service.yibao.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.DigestUtil;
import com.yeepay.HttpUtils;
import com.yj.gyl.bank.dto.*;
import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.service.yibao.util.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * 新投资通接口范例--API版
 *
 * @author
 */
@Service
public class YiBaoService {
    private static final Logger logger = LoggerFactory.getLogger(YiBaoService.class);

    /**
     * 有短验绑卡请求接口
     */
    public Map<String, String> bindCardRequest(PreBindCardDto preBindCardDto, TMerchant merchant) {
        Map<String, String> result = new HashMap<String, String>();
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //预绑卡请求地址
        String preBindBarkUrl = YiBaoConfig.getPreBindBarkUrl();
        String merchantAESKey = RandomUtil.getRandom(16);

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", "YB" + DateUtils.getFlowTime_18() + new Random().nextInt(999999999));
        dataMap.put("identityid", MD5Util.Hash(preBindCardDto.getUserId() + preBindCardDto.getCardNo()).toString());
        dataMap.put("idcardtype", "ID");
        dataMap.put("cardno", preBindCardDto.getCardNo());
        dataMap.put("idcardno", preBindCardDto.getIdCardNo());
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("username", preBindCardDto.getUserName());
        dataMap.put("phone", preBindCardDto.getPhone());
        dataMap.put("requesttime", DateUtils.toDateTimeString(new Date()));
        dataMap.put("advicesmstype", "MESSAGE");
        dataMap.put("customerenhancedtype", "AUTH_REMIT");
        dataMap.put("avaliabletime", "");
        dataMap.put("callbackurl", "");

        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);
        String jsonStr = JSON.toJSONString(dataMap);
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(preBindBarkUrl);
        logger.info("[易宝绑卡request参数]:-----{}", jsonStr);
        logger.info("[易宝绑卡url参数]:-----{}", preBindBarkUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝绑卡response参数]:-----{}", result);
        return (result);
    }

    /**
     * 有短验绑卡请求确认接口
     */
    public Map<String, String> bindCardConfirm(ConfirmBindCardDto confirmBindCardDto, TMerchant merchant) {
        String merchantAESKey = RandomUtil.getRandom(16);
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //请求确认绑卡地址
        String confirmBindBankcardUrl = YiBaoConfig.getConfirmBindBankcardUrl();

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", confirmBindCardDto.getOrderNo());
        dataMap.put("validatecode", confirmBindCardDto.getValidateCode());
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        Map<String, String> result = new HashMap<String, String>();

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(confirmBindBankcardUrl);
        String jsonStr = JSON.toJSONString(dataMap);

        logger.info("[易宝绑卡确认request参数]:-----{}", jsonStr);
        logger.info("[易宝绑卡确认url参数]:-----{}", confirmBindBankcardUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝绑卡确认response参数]:-----{}", result);

        return (result);
    }

    /**
     * @param bankCard
     * @return
     */
    public Map<String, String> unBindCard(TBankCard bankCard, TMerchant merchant) {
        String merchantAESKey = RandomUtil.getRandom(16);
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("identityid", MD5Util.Hash(bankCard.getUserId() + bankCard.getCardNo()).toString());
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("cardtop", bankCard.getCardNo().substring(0, 6));
        dataMap.put("cardlast", bankCard.getCardNo().substring(bankCard.getCardNo().length() - 4));
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        Map<String, String> result = new HashMap<String, String>();

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(YiBaoConfig.getDelBindBankcardUrl());
        String jsonStr = JSON.toJSONString(dataMap);

        logger.info("[易宝解绑确认request参数]:-----" + jsonStr);
        logger.info("[易宝解绑确认url参数]:-----" + YiBaoConfig.getDelBindBankcardUrl());
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant); //发送给易宝请求
        logger.info("[易宝解绑确认response参数]:-----" + result);

        return (result);
    }

    /**
     * 有短验充值请求接口
     */
    public Map<String, String> preYiBaoRecharge(PreRechargeDto preRechargeDto, TMerchant merchant) {
        Map<String, String> result = new HashMap<String, String>();
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //充值请求地址
        String preRechargeUrl = YiBaoConfig.getPreRechargeUrl();
        //充值回调地址
        String callbackRechargeUrl = YiBaoConfig.getCallbackRechargeUrl() + "/" + merchant.getId();

        String merchantAESKey = RandomUtil.getRandom(16);
        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", preRechargeDto.getOrderNo());
        dataMap.put("identityid", MD5Util.Hash(preRechargeDto.getUserId() + preRechargeDto.getCardNo()));
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("cardtop", preRechargeDto.getCardNo().substring(0, 6));
        dataMap.put("cardlast", preRechargeDto.getCardNo().substring(preRechargeDto.getCardNo().length() - 4));
        dataMap.put("amount", preRechargeDto.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dataMap.put("advicesmstype", "");
        dataMap.put("avaliabletime", "");
        dataMap.put("productname", YiBaoConfig.getProductName());
        dataMap.put("callbackurl", callbackRechargeUrl);
        dataMap.put("requesttime", DateUtils.toDateTimeString(new Date()));
        dataMap.put("terminalid", DateUtils.getFlowTime() + new Random().nextInt(999999));
        dataMap.put("registtime", DateUtils.toDateTimeString(new Date()));
        dataMap.put("lastloginterminalid", DateUtils.getFlowTime() + new Random().nextInt(999999));
        dataMap.put("issetpaypwd", "1");
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        String jsonStr = JSON.toJSONString(dataMap);
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(preRechargeUrl);

        logger.info("[易宝短信充值request参数]:-----{}", jsonStr);
        logger.info("[易宝短信充值url参数]:-----{}", preRechargeUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝短信充值response参数]:-----{}", JSON.toJSONString(result));
        result.put("reqMsg", jsonStr);
        return (result);
    }

    /**
     * 有短验提现请求接口
     */
    public Map<String, String> withdrawApplication(WithdrawDto withdrawDto, String userId, TMerchant merchant) {
        Map<String, String> result = new HashMap<String, String>();
        String merchantAESKey = RandomUtil.getRandom(16);
        //商户号
        String merchantno = merchant.getMerchantAccount();
        //商户私钥
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //提现请求地址
        String withdrawUrl = YiBaoConfig.getWithdrawUrl();
        //提现回调地址
        String callbackWithdrawUrl = YiBaoConfig.getCallbackWithdrawUrl() + "/" + merchant.getId();

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", withdrawDto.getOrderNo());
        dataMap.put("identityid", MD5Util.Hash(userId + withdrawDto.getCardNo()));
        dataMap.put("identitytype", "USER_ID");
        dataMap.put("cardtop", withdrawDto.getCardNo().substring(0, 6));
        dataMap.put("cardlast", withdrawDto.getCardNo().substring(withdrawDto.getCardNo().length() - 4));
        dataMap.put("amount", withdrawDto.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        dataMap.put("currency", "156");
        dataMap.put("drawtype", "NATRALDAY_URGENT");
        dataMap.put("callbackurl", callbackWithdrawUrl);
        dataMap.put("userip", IpUtils.getHost());
        dataMap.put("requesttime", DateUtils.toDateTimeString(new Date()));
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        String jsonStr = JSON.toJSONString(dataMap);
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(withdrawUrl);

        logger.info("[易宝提现request参数]:-----{}", jsonStr);
        logger.info("[易宝提现url参数]:-----{}", withdrawUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝短信提现response参数]:-----{}", result);
        result.put("reqMsg", jsonStr);
        return (result);
    }

    /**
     * 有短验充值请求确认接口
     */
    public Map<String, String> confirmYibaoRecharge(ConfirmRechargeDto confirmRechargeDto, TMerchant merchant) {
        Map<String, String> result = new HashMap<String, String>();
        String merchantAESKey = RandomUtil.getRandom(16);
        //商户号
        String merchantno = merchant.getMerchantAccount();
        //商户私钥
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //充值请求地址
        String confirmRechargeUrl = YiBaoConfig.getConfirmRechargeUrl();

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", confirmRechargeDto.getOrderNo());
        dataMap.put("validatecode", confirmRechargeDto.getValidateCode());
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(confirmRechargeUrl);
        String jsonStr = JSON.toJSONString(dataMap);

        logger.info("[易宝确认充值request参数]:-----{}", jsonStr);
        logger.info("[易宝短信充值url参数]:-----{}", confirmRechargeUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝确认充值response参数]:-----{}", JSON.toJSONString(result));
        result.put("reqMsg", jsonStr);
        return (result);
    }

    /**
     * 有短验充值请求重发短验接口
     */
    public Map<String, String> rechargeResendSms(String requestNo, TMerchant merchant) {
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        String reSendSmsRechargeUrl = YiBaoConfig.getReSendSmsRechargeUrl();

        String merchantAESKey = RandomUtil.getRandom(16);
        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", requestNo);
        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        Map<String, String> result = new HashMap<String, String>();
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(reSendSmsRechargeUrl);
        String jsonStr = JSON.toJSONString(dataMap);
        logger.info("[易宝充值短信重发request参数]:-----" + JSON.toJSONString(jsonStr));
        logger.info("[易宝充值短信重发url参数]:-----" + reSendSmsRechargeUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝充值短信重发response参数]:-----" + JSON.toJSONString(result));
        return (result);
    }

    /**
     * 充值查询
     */
    public Map<String, String> rechargeRecordQuery(String requestNo, TMerchant merchant) {
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //查询充值记录地址
        String rechargeRecordUrl = YiBaoConfig.getRechargeRecordUrl();
        String merchantAESKey = RandomUtil.getRandom(16);

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", requestNo);

        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        Map<String, String> result = new HashMap<String, String>();
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(rechargeRecordUrl);
        String jsonStr = JSON.toJSONString(dataMap);

        logger.info("[易宝充值查询request参数]:-----{}", jsonStr);
        logger.info("[易宝充值查询url参数]:-----{}", rechargeRecordUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝充值查询response参数]:-----{}", result);
        return (result);
    }


    /**
     * 提现查询
     */
    public Map<String, String> withdrawRecordQuery(TradeRecordDto recordRsDto, TMerchant merchant) {
        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        String merchantno = merchant.getMerchantAccount();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        //查询提现记录地址
        String withdrawRecordUrl = YiBaoConfig.getWithdrawRecordUrl();
        String merchantAESKey = RandomUtil.getRandom(16);

        dataMap.put("merchantno", merchantno);
        dataMap.put("requestno", recordRsDto.getOrderNo());
        dataMap.put("yborderid", recordRsDto.getOrderId());

        String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
        dataMap.put("sign", sign);

        Map<String, String> result = new HashMap<String, String>();
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(withdrawRecordUrl);
        String jsonStr = JSON.toJSONString(dataMap);

        logger.info("[易宝提现查询request参数]:-----{}", jsonStr);
        logger.info("[易宝提现查询url参数]:-----{}", withdrawRecordUrl);
        //发送给易宝请求
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝提现查询response参数]:-----{}", result);
        return (result);
    }

    /**
     * 查询用户绑卡列表
     *
     * @param userId   用户id
     * @param cardNo   银行卡号
     * @param merchant 商户账户信息
     * @return
     */
    public Map<String, String> getBindCardList(Long userId, String cardNo, TMerchant merchant) {
        Map<String, String> result = new HashMap<String, String>();
        String merchantAESKey = RandomUtil.getRandom(16);

        TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
        dataMap.put("merchantno", merchant.getMerchantAccount());
        dataMap.put("identityid", MD5Util.Hash(userId + cardNo));
        dataMap.put("identitytype", "USER_ID");

        String sign = EncryUtil.handleRSA(dataMap, merchant.getMerchantPrivateKey());
        dataMap.put("sign", sign);

        HttpClient httpClient = new HttpClient();
        String jsonStr = JSON.toJSONString(dataMap);
        PostMethod postMethod = new PostMethod(YiBaoConfig.getBindCardListUrl());
        logger.info("[易宝绑卡列表查询request参数]:-----{}", jsonStr);
        logger.info("[易宝绑卡列表查询url参数]:-----{}", YiBaoConfig.getBindCardListUrl());
        result = sendPost(result, httpClient, postMethod, jsonStr, merchantAESKey, merchant);
        logger.info("[易宝绑卡列表查询response参数]:-----{}", result);
        return (result);
    }


    public Map<String, String> yiBaoEbankRecharge(Long userId, String cardNo, TMerchant merchant) {
        Map<String, String> result = new HashMap<>(3);
        Map<String, String> params = new TreeMap<>();

        return result;
    }

    /**
     * 发送Post请求
     *
     * @param result         相应结果 Map<String, String>
     * @param httpClient
     * @param postMethod     请求方法
     * @param jsonStr        请求数据
     * @param merchantAESKey
     * @return
     */
    private Map sendPost(Map<String, String> result, HttpClient httpClient, PostMethod postMethod, String jsonStr, String merchantAESKey, TMerchant merchant) {
        String merchantno = merchant.getMerchantAccount();
        String yeepayPublicKey = merchant.getPublicKey();
        try {
            String data = AES.encryptToBase64(jsonStr, merchantAESKey);
            String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);

            NameValuePair[] datas = {new NameValuePair("merchantno", merchantno),
                    new NameValuePair("data", data),
                    new NameValuePair("encryptkey", encryptkey)};

            postMethod.setRequestBody(datas);
            int statusCode = httpClient.executeMethod(postMethod);
            byte[] responseByte = postMethod.getResponseBody();
            String responseBody = new String(responseByte, "UTF-8");
            result = parseHttpResponseBody(statusCode, responseBody, merchant);
        } catch (Exception e) {
            result.put("customError", e.getMessage());
            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }
        return result;
    }

    /**
     * 解析http请求返回
     */
    public Map<String, String> parseHttpResponseBody(int statusCode, String responseBody, TMerchant merchant) throws Exception {
        Map<String, String> result = new HashMap<String, String>();
        String yeepayPublicKey = merchant.getPublicKey();
        String merchantPrivateKey = merchant.getMerchantPrivateKey();
        if (statusCode != 200) {
            logger.info("请求失败,错误码 : " + statusCode);
            result.put("customError", "请求失败,,错误码！！" + statusCode);
            return (result);
        }
        Map<String, String> jsonMap = JSON.parseObject(responseBody,
                new TypeReference<TreeMap<String, String>>() {
                });
        if (jsonMap.containsKey("errorcode")) {
            logger.info("errormsg : " + JSON.toJSONString(jsonMap));
            result.put("errormsg", "[errorcode]:" + jsonMap.get("errorcode"));
            return (result);
        }
        String dataFromYeepay = formatString(jsonMap.get("data"));
        String encryptkeyFromYeepay = formatString(jsonMap.get("encryptkey"));
        boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay,
                yeepayPublicKey, merchantPrivateKey);
        if (!signMatch) {
            result.put("errormsg", "签名不匹配");
            return (result);
        }
        String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
        String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
        result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {
        });
        return (result);
    }

    /**
     * 格式化字符串
     */
    public static String formatString(String text) {
        return (text == null ? "" : text.trim());
    }

    /**
     * 获取网银支付请求地址
     *
     * @param params
     * @return
     */
    public static String getYeePayURL(Map<String, String> params) {
        logger.info("易宝网银支付入参:{}", JSONObject.toJSONString(params));
        String p0_Cmd = formatString(params.get("p0_Cmd"));
        String p1_MerId = formatString(params.get("p1_MerId"));
        String p2_Order = formatString(params.get("p2_Order"));
        String p3_Amt = formatString(params.get("p3_Amt"));
        String p4_Cur = formatString(params.get("p4_Cur"));
        String p5_Pid = formatString(params.get("p5_Pid"));
        String p6_Pcat = formatString(params.get("p6_Pcat"));
        String p7_Pdesc = formatString(params.get("p7_Pdesc"));
        String p8_Url = formatString(params.get("p8_Url"));
        String p9_SAF = formatString(params.get("p9_SAF"));
        String pa_MP = formatString(params.get("pa_MP"));
        String pb_ServerNotifyUrl = formatString(params.get("pb_ServerNotifyUrl"));
        String pd_FrpId = formatString(params.get("pd_FrpId"));
        String pm_Period = formatString(params.get("pm_Period"));
        String pn_Unit = formatString(params.get("pn_Unit"));
        String pr_NeedResponse = formatString(params.get("pr_NeedResponse"));
        String pt_UserName = formatString(params.get("pt_UserName"));
        String pt_PostalCode = formatString(params.get("pt_PostalCode"));
        String pt_Address = formatString(params.get("pt_Address"));
        String pt_TeleNo = formatString(params.get("pt_TeleNo"));
        String pt_Mobile = formatString(params.get("pt_Mobile"));
        String pt_Email = formatString(params.get("pt_Email"));
        String pt_LeaveMessage = formatString(params.get("pt_LeaveMessage"));
        String keyValue = formatString(params.get("key"));

        String[] strArr = new String[]{p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
                p8_Url, p9_SAF, pa_MP, pb_ServerNotifyUrl, pd_FrpId, pm_Period, pn_Unit, pr_NeedResponse,
                pt_UserName, pt_PostalCode, pt_Address, pt_TeleNo, pt_Mobile, pt_Email, pt_LeaveMessage};

        String hmac = DigestUtil.getHmac(strArr, keyValue);
        String hmac_safe = getHmac_safe(strArr, keyValue);
        try {
            p0_Cmd = URLEncoder.encode(p0_Cmd, "GBK");
            p1_MerId = URLEncoder.encode(p1_MerId, "GBK");
            p2_Order = URLEncoder.encode(p2_Order, "GBK");
            p3_Amt = URLEncoder.encode(p3_Amt, "GBK");
            p4_Cur = URLEncoder.encode(p4_Cur, "GBK");
            p5_Pid = URLEncoder.encode(p5_Pid, "GBK");
            p6_Pcat = URLEncoder.encode(p6_Pcat, "GBK");
            p7_Pdesc = URLEncoder.encode(p7_Pdesc, "GBK");
            p8_Url = URLEncoder.encode(p8_Url, "GBK");
            p9_SAF = URLEncoder.encode(p9_SAF, "GBK");
            pa_MP = URLEncoder.encode(pa_MP, "GBK");
            pb_ServerNotifyUrl = URLEncoder.encode(pb_ServerNotifyUrl, "GBK");
            pd_FrpId = URLEncoder.encode(pd_FrpId, "GBK");
            pm_Period = URLEncoder.encode(pm_Period, "GBK");
            pn_Unit = URLEncoder.encode(pn_Unit, "GBK");
            pr_NeedResponse = URLEncoder.encode(pr_NeedResponse, "GBK");
            pt_UserName = URLEncoder.encode(pt_UserName, "GBK");
            pt_PostalCode = URLEncoder.encode(pt_PostalCode, "GBK");
            pt_Address = URLEncoder.encode(pt_Address, "GBK");
            pt_TeleNo = URLEncoder.encode(pt_TeleNo, "GBK");
            pt_Mobile = URLEncoder.encode(pt_Mobile, "GBK");
            pt_Email = URLEncoder.encode(pt_Email, "GBK");
            pt_LeaveMessage = URLEncoder.encode(pt_LeaveMessage, "GBK");
            hmac = URLEncoder.encode(hmac, "GBK");
            hmac_safe = URLEncoder.encode(hmac_safe, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer payURL = new StringBuffer();

        payURL.append(YiBaoConfig.getYeePayUrl()).append("?");
        payURL.append("p0_Cmd=").append(p0_Cmd);
        payURL.append("&p1_MerId=").append(p1_MerId);
        payURL.append("&p2_Order=").append(p2_Order);
        payURL.append("&p3_Amt=").append(p3_Amt);
        payURL.append("&p4_Cur=").append(p4_Cur);
        payURL.append("&p5_Pid=").append(p5_Pid);
        payURL.append("&p6_Pcat=").append(p6_Pcat);
        payURL.append("&p7_Pdesc=").append(p7_Pdesc);
        payURL.append("&p8_Url=").append(p8_Url);
        payURL.append("&p9_SAF=").append(p9_SAF);
        payURL.append("&pa_MP=").append(pa_MP);
        payURL.append("&pb_ServerNotifyUrl=").append(pb_ServerNotifyUrl);
        payURL.append("&pd_FrpId=").append(pd_FrpId);
        payURL.append("&pm_Period=").append(pm_Period);
        payURL.append("&pn_Unit=").append(pn_Unit);
        payURL.append("&pr_NeedResponse=").append(pr_NeedResponse);
        payURL.append("&pt_UserName=").append(pt_UserName);
        payURL.append("&pt_PostalCode=").append(pt_PostalCode);
        payURL.append("&pt_Address=").append(pt_Address);
        payURL.append("&pt_TeleNo=").append(pt_TeleNo);
        payURL.append("&pt_Mobile=").append(pt_Mobile);
        payURL.append("&pt_Email=").append(pt_Email);
        payURL.append("&pt_LeaveMessage=").append(pt_LeaveMessage);
        payURL.append("&hmac=").append(hmac);
        payURL.append("&hmac_safe=").append(hmac_safe);
        logger.info("易宝网银支付请求地址:{}", payURL.toString());
        return (payURL.toString());
    }

    public static String getHmac_safe(String[] args, String key) {
        if (args == null || args.length == 0) {
            return (null);
        }
        StringBuffer hmac_safe = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            if (args[i].length() != 0 && !args[i].equals("")) {
                hmac_safe.append(args[i]).append("#");
            }

        }
        hmac_safe = hmac_safe.deleteCharAt(hmac_safe.length() - 1);
        return (DigestUtil.hmacSign(hmac_safe.toString(), key));
    }

    /**
     * 网银充值记录查询
     *
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static Map<String, String> queryByOrder(Map<String, String> params) {
        String p0_Cmd = "QueryOrdDetail";
        String p1_MerId = YiBaoConfig.getYeePayMerId();
        String p2_Order = params.get("p2_Order");
        String keyValue = YiBaoConfig.getYeePayKey();
        String pv_Ver = "3.0";
        String p3_ServiceType = params.get("p3_ServiceType");
        String[] strArr = {p0_Cmd, p1_MerId, p2_Order, pv_Ver, p3_ServiceType};

        String hmac = DigestUtil.getHmac(strArr, keyValue);
        String hmac_safe = getHmac_safe(strArr, keyValue);

        Map<String, String> queryParams = new HashMap<String, String>();
        queryParams.put("p0_Cmd", p0_Cmd);
        queryParams.put("p1_MerId", p1_MerId);
        queryParams.put("p2_Order", p2_Order);
        queryParams.put("pv_Ver", pv_Ver);
        queryParams.put("p3_ServiceType", p3_ServiceType);
        queryParams.put("hmac_safe", hmac_safe);
        queryParams.put("hmac", hmac);

        Map<String, String> queryResult = new HashMap<String, String>();
        String r0_Cmd = "";
        String r1_Code = "";
        String r2_TrxId = "";
        String r3_Amt = "";
        String r4_Cur = "";
        String r5_Pid = "";
        String r6_Order = "";
        String r8_MP = "";
        String rw_RefundRequestID = "";
        String rx_CreateTime = "";
        String ry_FinshTime = "";
        String rz_RefundAmount = "";
        String rb_PayStatus = "";
        String rc_RefundCount = "";
        String rd_RefundAmt = "";
        String hmacFromYeepay = "";
        String hmac_safeFromYeepay = "";
        String errorMsg = "";
        List responseList = null;

        try {
            responseList = HttpUtils.URLGet(YiBaoConfig.getYeePayQueryUrl(), queryParams);
            logger.info("查询网银充值记录结果：{}", responseList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (responseList == null) {
            errorMsg = "No data returned!";
        } else {
            Iterator iter = responseList.iterator();
            while (iter.hasNext()) {
                String temp = formatString((String) iter.next());
                if (temp.equals("")) {
                    continue;
                }
                int i = temp.indexOf("=");
                int j = temp.length();
                if (i >= 0) {
                    String tempKey = temp.substring(0, i);
                    String tempValue = null;
                    try {
                        tempValue = URLDecoder.decode(temp.substring(i + 1, j), "GBK");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if ("r0_Cmd".equals(tempKey)) {
                        r0_Cmd = tempValue;
                    } else if ("r1_Code".equals(tempKey)) {
                        r1_Code = tempValue;
                    } else if ("r2_TrxId".equals(tempKey)) {
                        r2_TrxId = tempValue;
                    } else if ("r3_Amt".equals(tempKey)) {
                        r3_Amt = tempValue;
                    } else if ("r4_Cur".equals(tempKey)) {
                        r4_Cur = tempValue;
                    } else if ("r5_Pid".equals(tempKey)) {
                        r5_Pid = tempValue;
                    } else if ("r6_Order".equals(tempKey)) {
                        r6_Order = tempValue;
                    } else if ("r8_MP".equals(tempKey)) {
                        r8_MP = tempValue;
                    } else if ("rw_RefundRequestID".equals(tempKey)) {
                        rw_RefundRequestID = tempValue;
                    } else if ("rx_CreateTime".equals(tempKey)) {
                        rx_CreateTime = tempValue;
                    } else if ("ry_FinshTime".equals(tempKey)) {
                        ry_FinshTime = tempValue;
                    } else if ("rz_RefundAmount".equals(tempKey)) {
                        rz_RefundAmount = tempValue;
                    } else if ("rb_PayStatus".equals(tempKey)) {
                        rb_PayStatus = tempValue;
                    } else if ("rc_RefundCount".equals(tempKey)) {
                        rc_RefundCount = tempValue;
                    } else if ("rd_RefundAmt".equals(tempKey)) {
                        rd_RefundAmt = tempValue;
                    } else if ("hmac".equals(tempKey)) {
                        hmacFromYeepay = tempValue;
                    } else if ("hmac_safe".equals(tempKey)) {
                        hmac_safeFromYeepay = tempValue;
                    }
                }
            }

            String[] stringArr = {r0_Cmd, r1_Code, r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r8_MP,
                    rw_RefundRequestID, rx_CreateTime, ry_FinshTime, rz_RefundAmount, rb_PayStatus,
                    rc_RefundCount, rd_RefundAmt};
            String localHmac = DigestUtil.getHmac(stringArr, keyValue);
            boolean ishmac_safe = false;
            try {
                ishmac_safe = CheckUtils.verifyCallbackHmac_safe(stringArr, hmac_safeFromYeepay);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!localHmac.equals(hmacFromYeepay) || !ishmac_safe) {
                errorMsg = "数据有误，签名验证通过";
            }
        }
        queryResult.put("r0_Cmd", r0_Cmd);
        queryResult.put("r1_Code", r1_Code);
        queryResult.put("r2_TrxId", r2_TrxId);
        queryResult.put("r3_Amt", r3_Amt);
        queryResult.put("r4_Cur", r4_Cur);
        queryResult.put("r5_Pid", r5_Pid);
        queryResult.put("r6_Order", r6_Order);
        queryResult.put("r8_MP", r8_MP);
        queryResult.put("rw_RefundRequestID", rw_RefundRequestID);
        queryResult.put("rx_CreateTime", rx_CreateTime);
        queryResult.put("ry_FinshTime", ry_FinshTime);
        queryResult.put("rz_RefundAmount", rz_RefundAmount);
        queryResult.put("rb_PayStatus", rb_PayStatus);
        queryResult.put("rc_RefundCount", rc_RefundCount);
        queryResult.put("rd_RefundAmt", rd_RefundAmt);
        queryResult.put("errorMsg", errorMsg);
        return (queryResult);
    }

}
