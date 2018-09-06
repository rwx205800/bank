package com.yj.gyl.bank.service.yibao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.client.YopClient3;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.config.ConfigUtils;
import com.yeepay.g3.sdk.yop.config.SDKConfig;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigestAlgEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalEnvelopeDTO;
import com.yeepay.g3.sdk.yop.encrypt.DigitalSignatureDTO;
import com.yeepay.g3.sdk.yop.error.YopSubError;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yeepay.g3.sdk.yop.utils.InternalConfig;
import com.yeepay.g3.sdk.yop.utils.JsonUtils;
import com.yeepay.g3.yop.sdk.api.StdApi;
import com.yj.gyl.bank.dto.YopTransferSendDto;
import com.yj.gyl.bank.model.YopRequestChild;
import com.yj.gyl.bank.service.yibao.common.YopConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;

public class YeepayService {
    private static final Logger logger = LoggerFactory.getLogger(YeepayService.class);
    //接口参数
    public static final String[] TRADEORDER = {"parentMerchantNo", "merchantNo", "orderId", "orderAmount", "timeoutExpress", "requestDate", "redirectUrl", "notifyUrl", "goodsParamExt", "paymentParamExt", "industryParamExt", "memo", "riskParamExt", "csUrl", "fundProcessType", "divideDetail", "divideNotifyUrl"};
    public static final String[] ORDERQUERY = {"parentMerchantNo", "merchantNo", "orderId", "uniqueOrderNo"};
    public static final String[] REFUND = {"parentMerchantNo", "merchantNo", "orderId", "uniqueOrderNo", "refundRequestId", "refundAmount", "description", "memo", "notifyUrl", "accountDivided"};
    public static final String[] REFUNDQUERY = {"parentMerchantNo", "merchantNo", "refundRequestId", "orderId", "uniqueRefundNo"};
    public static final String[] MULTIORDERQUERY = {"status", "parentMerchantNo", "merchantNo", "requestDateBegin", "requestDateEnd", "pageNo", "pageSize"};
    public static final String[] ORDERCLOSE = {"orderId", "parentMerchantNo", "merchantNo", "uniqueOrderNo", "description"};
    public static final String[] DIVIDEORDER = {"parentMerchantNo", "merchantNo", "divideRequestId", "orderId", "uniqueOrderNo", "divideDetail", "infoParamExt", "contractNo"};
    public static final String[] DIVIDEORDERQUERY = {"parentMerchantNo", "merchantNo", "divideRequestId", "orderId", "uniqueOrderNo"};
    public static final String[] FULLSETTLE = {"parentMerchantNo", "merchantNo", "orderId", "uniqueOrderNo"};
    public static final String[] CERTORDER = {"merchantNo", "requestNo", "bankCardNo", "idCardNo", "userName", "authType", "requestTime", "remark"};
    public static final String[] CERTORDERORDER = {"merchantNo", "requestNo", "ybOrderId"};

    //支付方式
    public static final String[] CASHIER = {"merchantNo", "token", "timestamp", "directPayType", "cardType", "userNo", "userType", "ext"};
    public static final String[] APICASHIER = {"token", "payTool", "payType", "userNo", "userType", "appId", "openId", "payEmpowerNo", "merchantTerminalId", "merchantStoreNo", "userIp", "version"};

    //获取对账类型
    public static final String TRADEDAYDOWNLOAD = "tradedaydownload";
    public static final String TRADEMONTHDOWNLOAD = "trademonthdownload";
    public static final String REMITDAYDOWNLOAD = "remitdaydownload";

    //标准收银台——拼接支付链接
    public static String getUrl(Map<String, String> paramValues) throws UnsupportedEncodingException {
        StringBuffer url = new StringBuffer();
        url.append(YopConfig.getCashier());
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println(paramValues);
        for (int i = 0; i < CASHIER.length; i++) {
            String name = CASHIER[i];
            String value = paramValues.get(name);
            if (i != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(name + "=").append(value);
        }
        System.out.println("stringbuilder:" + stringBuilder);
        String sign = getSign(stringBuilder.toString());
        url.append("?sign=" + sign + "&" + stringBuilder);
        return url.toString();
    }

    //获取密钥P12
    public static PrivateKey getSecretKey() {
        PrivateKey isvPrivateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
        return isvPrivateKey;
    }

    //获取公钥
    public static PublicKey getPublicKey() {
        PublicKey isvPublicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
        return isvPublicKey;
    }

    //获取sign
    public static String getSign(String stringBuilder) {
        String appKey = "OPR:" + YopConfig.getMerchantNo();

        PrivateKey isvPrivateKey = getSecretKey();

        DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
        digitalSignatureDTO.setAppKey(appKey);
        digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
        digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
        digitalSignatureDTO.setPlainText(stringBuilder.toString());
        String sign = DigitalEnvelopeUtils.sign0(digitalSignatureDTO, isvPrivateKey);
        return sign;
    }

    /**
     * 请求YOP接口
     * params 请求参数,parentMerchantNo除外
     * uri 请求yop的应用URI地址
     * paramSign 请求参数的验签顺序
     *
     * @throws IOException
     */
    public static Map<String, String> requestYop(Map<String, String> params, String uri, String[] paramSign, String path) throws IOException {
        Map<String, String> result = new HashMap<String, String>();
        String BASE_URL = YopConfig.getBaseURL();
        String parentMerchantNo = YopConfig.getParentMerchantNo();
        String merchantNo = YopConfig.getMerchantNo();
        params.put("parentMerchantNo", parentMerchantNo);
        params.put("merchantNo", merchantNo);
        String privatekey = YopConfig.getPrivatekey();

        //YopRequest request = new YopRequest("OPR:"+merchantNo,"");
        YopRequest request = new YopRequest("OPR:" + merchantNo, path + "/src/config/yop_sdk_config_default.json", BASE_URL);

        for (int i = 0; i < paramSign.length; i++) {
            String key = paramSign[i];
            request.addParam(key, params.get(key));
        }
        System.out.println(request.getParams());

        YopResponse response = YopClient3.postRsa(uri, request);

        System.out.println(response.toString());
        if ("FAILURE".equals(response.getState())) {
            if (response.getError() != null)
                result.put("code", response.getError().getCode());
            result.put("message", response.getError().getMessage());
            return result;
        }
        if (response.getStringResult() != null) {
            result = parseResponse(response.getStringResult());
        }

        return result;
    }

    public static String getStatus(String transferStatus) {
        if ("0025".equals(transferStatus)) { // 易宝已经接收了这笔打款，但未发至银行
            return "WAIT";//等待 不做任何处理
        } else if ("0026".equals(transferStatus)) {//易宝已经打款到银行
            return "WAIT";
        } else if ("0027".equals(transferStatus)) {//易宝已经将款退回商户账户
            return "FAIL";
        } else if ("0028".equals(transferStatus)) {//易宝已经退回此请求，出款金额退回商户账户，此笔出款未发至银行
            return "FAIL";
        } else if ("0029".equals(transferStatus)) { //易宝已经接收此请求，但此请求有待商户登录易宝商户后台复核出款
            return "WAIT";
        } else if ("0030".equals(transferStatus)) { //已经接收，但是具体出款情况不明，需要易宝支付有限公司再次查询直到打款状态码不是0030
            return "WAIT";
        }
        return "FAIL";
    }

    public static String getStatus(String transferStatus, String bankStatus) {
        if ("0025".equals(transferStatus)) { // 易宝已经接收了这笔打款，但未发至银行
            return "WAIT";//等待 不做任何处理
        } else if ("0026".equals(transferStatus)) {//易宝已经打款到银行
            if ("S".equals(bankStatus)) {
                return "SUCCESS";
            } else if ("I".equals(bankStatus)) { //出款已发至银行，银行正在处理
                return "WAIT";
            } else if ("F".equals(bankStatus)) {
                return "FAIL";
            } else if ("W".equals(bankStatus)) { //出款尚未发到银行
                return "WAIT";
            } else if ("U".equals(bankStatus)) { //未知 此出款既可能成功也可能失败
                return "WAIT";
            }
        } else if ("0027".equals(transferStatus)) {//易宝已经将款退回商户账户
            return "FAIL";
        } else if ("0028".equals(transferStatus)) {//易宝已经退回此请求，出款金额退回商户账户，此笔出款未发至银行
            return "FAIL";
        } else if ("0029".equals(transferStatus)) { //易宝已经接收此请求，但此请求有待商户登录易宝商户后台复核出款
            return "WAIT";
        } else if ("0030".equals(transferStatus)) { //已经接收，但是具体出款情况不明，需要易宝支付有限公司再次查询直到打款状态码不是0030
            return "WAIT";
        }
        return "WAIT";
    }

    public static String format(String text) {
        return text == null ? "" : text.trim();
    }

    public static Map<String, Object> getParams(YopTransferSendDto transferDto, String orderId, String batchNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("customerNumber", format(YopConfig.getMerchantNo()));
        params.put("groupNumber", format(YopConfig.getParentMerchantNo()));
        params.put("batchNo", batchNo);
        params.put("orderId", orderId);
        params.put("amount", format(new BigDecimal(transferDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
        params.put("product", "");
        params.put("urgency", "");
        params.put("accountName", format(transferDto.getAccountName()));
        params.put("accountNumber", format(transferDto.getAccountNumber()));
        params.put("bankCode", format(transferDto.getBankCode()));
        params.put("bankName", "");
        params.put("bankBranchName", "");
        params.put("provinceCode", "");
        params.put("cityCode", "");
        params.put("feeType", format(transferDto.getFeeType()));
        params.put("desc", "");
        params.put("leaveWord", "");
        params.put("abstractInfo", "");
        return params;
    }


    public static Map<String, Object> yeepayYOP(Map<String, Object> map, String Uri) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, YopSubError> erresult = new HashMap<String, YopSubError>();
        YopRequest yoprequest = new YopRequest("OPR:" + YopConfig.getMerchantNo());
        Set<Map.Entry<String, Object>> entry = map.entrySet();
        for (Map.Entry<String, Object> s : entry) {
            yoprequest.addParam(s.getKey(), s.getValue());
        }
        logger.info("【请求YOP入参:{}】", JSONObject.toJSONString(yoprequest));
        //向YOP发请求
        YopResponse yopresponse = YopClient3.postRsa(Uri, yoprequest);
        logger.info("【请求YOP之后的结果：{}】", JSONObject.toJSONString(yopresponse));
//        	对结果进行处理
        if ("FAILURE".equals(yopresponse.getState())) {
            if (yopresponse.getError() != null) {
                result.put("errorcode", yopresponse.getError().getCode());
                result.put("errormsg", yopresponse.getError().getMessage());
                if (yopresponse.getError().getSubErrors() != null && yopresponse.getError().getSubErrors().size() > 0) {
                    erresult.put("errorDetails", yopresponse.getError().getSubErrors().get(0));
                } else {
                    erresult.put("errorDetails", null);
                }
                logger.error("【错误明细：{}】", JSONObject.toJSONString(yopresponse.getError().getSubErrors()));
                result.putAll(erresult);
                logger.error("【系统处理异常结果：{}】", result);
            }
            return result;
        }
        //成功则进行相关处理
        if (yopresponse.getStringResult() != null) {
            result = parseObjectResponse(yopresponse.getStringResult());
        }
        return result;
    }

    //将获取到的response转换成json格式
    public static Map<String, Object> parseObjectResponse(String yopresponse) {

        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap = JSON.parseObject(yopresponse,
                new TypeReference<TreeMap<String, Object>>() {
                });
        System.out.println("将response转化为map格式之后: " + jsonMap);
        return jsonMap;
    }


    //将获取到的response解密完成的json转换成map
    public static Map<String, String> parseResponse(String response) {

        Map<String, String> jsonMap = new HashMap<>();
        jsonMap = JSON.parseObject(response,
                new TypeReference<TreeMap<String, String>>() {
                });

        return jsonMap;
    }

    /**
     * 支付成功，页面回调验签
     *
     * @param responseMap
     * @return
     */
    public static boolean verifyCallback(Map<String, String> responseMap) {
        boolean flag = false;
        String merchantNo = responseMap.get("merchantNo");
        String parentMerchantNo = responseMap.get("parentMerchantNo");
        String orderId = responseMap.get("orderId");
        String signResp = responseMap.get("sign");
        String s = "merchantNo=" + merchantNo + "&parentMerchantNo=" + parentMerchantNo + "&orderId=" + orderId;
        System.out.println("s====" + s);
        String appKey = "OPR:" + YopConfig.getMerchantNo();
        PublicKey isvPublicKey = getPublicKey();
        DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
        digitalSignatureDTO.setAppKey(appKey);
        digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
        digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
        digitalSignatureDTO.setPlainText(s.toString());
        digitalSignatureDTO.setSignature(signResp);
        try {
            DigitalEnvelopeUtils.verify0(digitalSignatureDTO, isvPublicKey);
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return flag;
    }

    /**
     * 异步回调验签
     *
     * @param response
     * @return
     */
    public static Map<String, String> callback(String response) {
        DigitalEnvelopeDTO dto = new DigitalEnvelopeDTO();
        dto.setCipherText(response);
        Map<String, String> jsonMap = new HashMap<>();
        try {
            PrivateKey privateKey = InternalConfig.getISVPrivateKey(CertTypeEnum.RSA2048);
            PublicKey publicKey = InternalConfig.getYopPublicKey(CertTypeEnum.RSA2048);
            dto = DigitalEnvelopeUtils.decrypt(dto, privateKey, publicKey);
            System.out.println(dto.getPlainText());
            jsonMap = parseResponse(dto.getPlainText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonMap;
    }

    /**
     * 下载对账单
     *
     * @param params
     * @param path
     * @return
     */
    public static String yosFile(Map<String, String> params, String path) {
        StdApi apidApi = new StdApi();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        String merchantNo = YopConfig.getMerchantNo();
        String method = params.get("method");
        String dateday = params.get("dateday");
        String datemonth = params.get("datemonth");
        String dataType = params.get("dataType");

        String fileName = "";
        String filePath = "";
        try {
            if (method.equals(YeepayService.TRADEDAYDOWNLOAD)) {
                System.out.println("1");
                inputStream = apidApi.tradeDayBillDownload(merchantNo, dateday);
                fileName = "tradeday-" + dateday + ".csv";

            } else if (method.equals(YeepayService.TRADEMONTHDOWNLOAD)) {
                System.out.println("2");
                inputStream = apidApi.tradeMonthBillDownload(merchantNo, datemonth);
                fileName = "trademonth-" + datemonth + ".csv";

            } else if (method.equals(YeepayService.REMITDAYDOWNLOAD)) {
                System.out.println("2");
                inputStream = apidApi.remitDayBillDownload(merchantNo, dateday, dataType);
                fileName = "remitday-" + dataType + "-" + dateday + ".csv";

            }
            filePath = path + File.separator + fileName;
            System.out.println("filePath=====" + filePath);
            outputStream = new FileOutputStream(new File(filePath));
            byte[] bs = new byte[1024];
            int readNum;
            while ((readNum = inputStream.read(bs)) != -1) {
                outputStream.write(bs, 0, readNum);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return null;
        } finally {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }


    public static Map<String, String> requestYOP(Map<String, String> params, String uri, String[] paramSign) throws IOException {
        Map<String, String> result = new HashMap<String, String>();
//		String BASE_URL = getUrl("baseURL");
        String parentMerchantNo = YopConfig.getParentMerchantNo();
        String merchantNo = YopConfig.getMerchantNo();
        params.put("parentMerchantNo", parentMerchantNo);
        params.put("merchantNo", merchantNo);
        //第三种方式，传appkey和privatekey
        //String privatekey=Configuration.getInstance().getValue("privatekey");
//        YopRequest request = new YopRequest("OPR:"+merchantNo,privatekey);

        //第一种方式，不传参数
//        YopRequest request  =  new YopRequest();
        /**
         * 第二种方式：只传appkey
         */
       /* ArrayList filenames = new ArrayList();
        URL ex = ConfigUtils.getContextClassLoader().getResource("config");
        logger.info("ex:{}",JSONObject.toJSONString(ex));
        if(ex != null) {
            File file = null;
            try {
                logger.info("extoURI:{}",ex.toURI());
                file = new File(ex.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            if(file.isDirectory()) {
                InputStream in = ex.openStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                String resource;
                while((resource = br.readLine()) != null) {
                    filenames.add(resource);
                }
            } else {
                logger.warn("Sdk config directory is a file.");
            }
        }
        logger.info("【filenames：{}】",JSONObject.toJSONString(filenames));
        String resource = "config/yop_sdk_config_default.json";
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/yop_sdk_config_default.json");
        if (in == null){
            in = ConfigUtils.class.getResourceAsStream("/" + resource);
        }
        SDKConfig config = (SDKConfig) JsonUtils.loadFrom(in, SDKConfig.class);
        logger.info("config 查询：{}",JSONObject.toJSONString(config));*/


        YopRequestChild requestChild = new YopRequestChild();
        ClassPathResource resource = new ClassPathResource("config/yop_sdk_config_default.json");
        InputStream inputStream = resource.getInputStream();
        SDKConfig config = (SDKConfig)JsonUtils.loadFrom(inputStream, SDKConfig.class);

        logger.info("【易宝拼接参数：{}】",JSONObject.toJSONString(params));
        logger.info("【易宝调用请求：{}】", uri);
        YopRequest request = new YopRequest("OPR:" + merchantNo);

        for (int i = 0; i < paramSign.length; i++) {
            String key = paramSign[i];
            request.addParam(key, params.get(key));
        }
        logger.info("【易宝调用参数：{}】", JSONObject.toJSONString(request));
        YopResponse response = YopClient3.postRsa(uri, request);
        logger.info("【易宝返回结果：{}】", JSONObject.toJSONString(response));
        if ("FAILURE".equals(response.getState())) {
            if (response.getError() != null)
                result.put("code", response.getError().getCode());
            result.put("message", response.getError().getMessage());
            return result;
        }
        if (response.getStringResult() != null) {
            result = parseResponse(response.getStringResult());
        }

        return result;
    }


}
