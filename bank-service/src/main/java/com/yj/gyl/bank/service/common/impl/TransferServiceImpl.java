package com.yj.gyl.bank.service.common.impl;

import com.alibaba.fastjson.JSONObject;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dao.TTransferRecordMapper;
import com.yj.gyl.bank.dto.TransferSend;
import com.yj.gyl.bank.model.TTransferRecord;
import com.yj.gyl.bank.model.TTransferRecordExample;
import com.yj.gyl.bank.rsdto.TransactionPayEnum;
import com.yj.gyl.bank.service.common.ITransferService;
import com.yj.gyl.bank.service.yibao.common.YiBaoConfig;
import com.yj.gyl.bank.service.yibao.util.CallbackUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by renfei on 2017/12/19.
 */
@Service
public class TransferServiceImpl implements ITransferService {
    private static final Logger logger = LoggerFactory.getLogger(TransferServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TTransferRecordMapper transferRecordMapper;

    @Override
    public List<TTransferRecord> listTransferRecord(Long projectId) {
        TTransferRecordExample example = new TTransferRecordExample();
        example.or().andProjectIdEqualTo(projectId).andStatusIn(new ArrayList<Integer>() {{
            //处理中
            add(0);
            //成功
            add(1);
        }});
        return transferRecordMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTransferRecord(Long projectId, BigDecimal amount, int status, String batchNo, String orderNo) {
        TTransferRecord transferRecord = new TTransferRecord();
        transferRecord.setProjectId(projectId);
        transferRecord.setAmount(amount);
        transferRecord.setStatus(status);
        transferRecord.setBatchNo(batchNo);
        transferRecord.setOrderNo(orderNo);
        transferRecord.setCreateTime(new Date());
        return transferRecordMapper.insert(transferRecord) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTransferRecord(TTransferRecord transferRecord) {
        TTransferRecordExample example = new TTransferRecordExample();
        example.or().andProjectIdEqualTo(transferRecord.getProjectId())
                .andBatchNoEqualTo(transferRecord.getBatchNo())
                .andOrderNoEqualTo(transferRecord.getOrderNo());
        transferRecordMapper.updateByExampleSelective(transferRecord, example);
    }

    @Override
    public CommonResponse transferSingle(BigDecimal amt, String orderId, String batchNo) {
        if (!YiBaoConfig.isPreFlag()) {
            return CommonResponse.builder().result(0).status(0).data("").msg("测试数据").build();
        }

        //需要参加签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] digestValues = {"cmd", "mer_Id", "batch_No", "order_Id", "amount", "account_Number", "hmacKey"};
        //验证返回参数签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] backDigestValues = {"cmd", "ret_Code", "mer_Id", "batch_No", "total_Amt", "total_Num", "r1_Code", "hmacKey"};

        String amount = amt.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String xml = "<data>\n" +
                "\t<cmd>TransferSingle</cmd>\n" +
                "\t<version>1.1</version>\n" +
                "\t<mer_Id>" + YiBaoConfig.getMerId() + "</mer_Id>\n" +
                "\t<group_Id>" + YiBaoConfig.getMerId() + "</group_Id>\n" +
                "\t<batch_No>" + batchNo + "</batch_No>\n" +
                "\t<order_Id>" + orderId + "</order_Id>\n" +
                "\t<bank_Code>" + YiBaoConfig.getBankCode() + "</bank_Code>\n" +
                "\t<cnaps></cnaps>\n" +
                "\t<bank_Name>" + YiBaoConfig.getBankName() + "</bank_Name>\n" +
                "\t<branch_Bank_Name>" + YiBaoConfig.getBankBranchName() + "</branch_Bank_Name>\n" +
                "\t<amount>" + amount + "</amount>\n" +
                "\t<account_Name>" + YiBaoConfig.getAccountName() + "</account_Name>\n" +
                "\t<account_Number>" + YiBaoConfig.getAccountNumber() + "</account_Number>\n" +
                "\t<province>" + YiBaoConfig.getProvince() + "</province>\n" +
                "\t<city>" + YiBaoConfig.getCity() + "</city>\n" +
                "\t<fee_Type>SOURCE</fee_Type>\n" +//手续费 商户承担
                "\t<payee_Email></payee_Email>\n" +
                "\t<payee_Mobile></payee_Mobile>\n" +
                "\t<leave_Word></leave_Word>\n" +
                "\t<abstractInfo></abstractInfo>\n" +
                "\t<remarksInfo></remarksInfo>\n" +
                "\t<urgency>0</urgency>\n" +//非加急
                "\t<hmac></hmac>\n" +
                "</data>\n" +
                "\t";

        CommonResponse resultData = commonPostData(xml, YiBaoConfig.getTransferSingleUrl(), digestValues, backDigestValues);

        if (resultData.getResult() == 0 && resultData.getStatus() == 0 && resultData.getData() != null) {
            Map xmlBackMap = (Map) resultData.getData();
            //系统返回码
            String retCode = xmlBackMap.get("ret_Code").toString();
            //打款状态码
            String r1Code = xmlBackMap.get("r1_Code").toString();
            //银行处理状态码
            String bankStatus = xmlBackMap.get("bank_Status").toString();
            //说明
            String errorMsg = xmlBackMap.get("error_Msg").toString();
            //解析
            return parseResult(retCode, r1Code, bankStatus, errorMsg);
        }

        return resultData;
    }

    @Override
    public CommonResponse transferSingle(TransferSend transferSend) {
        if (!YiBaoConfig.isPreFlag()) {
            return CommonResponse.builder().result(0).status(0).data("").msg("测试数据").build();
        }
        //需要参加签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] digestValues = {"cmd", "mer_Id", "batch_No", "order_Id", "amount", "account_Number", "hmacKey"};
        //验证返回参数签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] backDigestValues = {"cmd", "ret_Code", "mer_Id", "batch_No", "total_Amt", "total_Num", "r1_Code", "hmacKey"};

        String amount = transferSend.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        String xml = "<data>\n" +
                "\t<cmd>TransferSingle</cmd>\n" +
                "\t<version>1.1</version>\n" +
                "\t<mer_Id>" + YiBaoConfig.getMerId() + "</mer_Id>\n" +
                "\t<group_Id>" + YiBaoConfig.getMerId() + "</group_Id>\n" +
                "\t<batch_No>" + transferSend.getBatchNo() + "</batch_No>\n" +
                "\t<order_Id>" + transferSend.getOrderId() + "</order_Id>\n" +
                "\t<bank_Code>" + transferSend.getBankCode() + "</bank_Code>\n" +
                "\t<cnaps></cnaps>\n" +
                "\t<bank_Name>" + transferSend.getBankName() + "</bank_Name>\n" +
                "\t<branch_Bank_Name></branch_Bank_Name>\n" +
                "\t<amount>" + amount + "</amount>\n" +
                "\t<account_Name>" + transferSend.getAccountName() + "</account_Name>\n" +
                "\t<account_Number>" + transferSend.getAccountNumber() + "</account_Number>\n" +
                "\t<province>" + transferSend.getProvince() + "</province>\n" +
                "\t<city>" + transferSend.getCity() + "</city>\n" +
                "\t<fee_Type>SOURCE</fee_Type>\n" +//手续费 商户承担
                "\t<payee_Email></payee_Email>\n" +
                "\t<payee_Mobile></payee_Mobile>\n" +
                "\t<leave_Word></leave_Word>\n" +
                "\t<abstractInfo></abstractInfo>\n" +
                "\t<remarksInfo></remarksInfo>\n" +
                "\t<urgency>0</urgency>\n" +//非加急
                "\t<hmac></hmac>\n" +
                "</data>\n" +
                "\t";

        CommonResponse resultData = commonPostData(xml, YiBaoConfig.getTransferSingleUrl(), digestValues, backDigestValues);

        if (resultData.getResult() == 0 && resultData.getStatus() == 0 && resultData.getData() != null) {
            Map xmlBackMap = (Map) resultData.getData();
            //系统返回码
            String retCode = xmlBackMap.get("ret_Code").toString();
            //打款状态码
            String r1Code = xmlBackMap.get("r1_Code").toString();
            //银行处理状态码
            String bankStatus = xmlBackMap.get("bank_Status").toString();
            //说明
            String errorMsg = xmlBackMap.get("error_Msg").toString();
            //解析
            return parseResult(retCode, r1Code, bankStatus, errorMsg);
        }

        return resultData;
    }

    @Override
    public CommonResponse transAcountDetail(String orderNo, String batchNo, Integer pageSize) {
        if (!YiBaoConfig.isPreFlag()) {
            return CommonResponse.builder().result(0).status(0).data("").msg("测试数据").build();
        }
        //需要参加签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] digestValues = {"cmd", "mer_Id", "batch_No", "order_Id", "page_No", "hmacKey"};
        //验证返回参数签名的参数:其中(hmacKey)指的是商户自己的密钥
        String[] backDigestValues = {"cmd", "ret_Code", "mer_Id", "batch_No", "total_Num", "end_Flag", "hmacKey"};
        String xml = "<data>\n" +
                "\t<cmd>BatchDetailQuery</cmd>\n" +
                "\t<version>1.1</version>\n" +
                "\t<group_Id>" + YiBaoConfig.getMerId() + "</group_Id>\n" +
                "\t<mer_Id>" + YiBaoConfig.getMerId() + "</mer_Id>\n" +
                "\t<query_Mode>1</query_Mode>\n" +
                "\t<batch_No>" + batchNo + "</batch_No>\n" +
                "\t<order_Id>" + orderNo + "</order_Id>\n" +
                "\t<page_No>" + pageSize + "</page_No>\n" +
                "\t<hmac></hmac>\n" +
                "</data>\n" +
                "\t";

        CommonResponse resultData = commonPostData(xml, YiBaoConfig.getTransferSingleUrl(), digestValues, backDigestValues);
        if (resultData.getResult() == 0 && resultData.getStatus() == 0 && resultData.getData() != null) {
            Map xmlBackMap = (Map) resultData.getData();
            //系统返回码
            String retCode = xmlBackMap.get("ret_Code").toString();
            //打款状态码
            String r1Code = "";
            //银行处理状态码
            String bankStatus = "";
            //说明
            String errorMsg = "";
            if ("1".equals(retCode)) {
                List<Map> itemList = (List<Map>) xmlBackMap.get("items");
                //此处只查询一条记录的详情
                for (Map item : itemList) {
                    r1Code = item.get("r1_Code").toString();
                    bankStatus = item.get("bank_Status").toString();
                    errorMsg = StringUtils.hasText(item.get("fail_Desc").toString()) ? item.get("fail_Desc").toString() :
                            StringUtils.hasText(item.get("abstractInfo").toString()) ? item.get("abstractInfo").toString() :
                                    StringUtils.hasText(item.get("remarksInfo").toString()) ? item.get("remarksInfo").toString() : "";
                }
            }
            errorMsg = StringUtils.hasText(errorMsg) ? errorMsg : xmlBackMap.get("error_Msg") == null ? "未知错误" : xmlBackMap.get("error_Msg").toString();
            return parseResult(retCode, r1Code, bankStatus, errorMsg);
        }
        return resultData;
    }

    @Override
    public TTransferRecord getTransferRecord(Long userId, String orderNo) {
        TTransferRecordExample example = new TTransferRecordExample();
        example.or().andProjectIdEqualTo(userId).andOrderNoEqualTo(orderNo);
        List<TTransferRecord> list = transferRecordMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private CommonResponse commonPostData(String xml, String postUrl, String[] digestValues, String[] backDigestValues) {
        //商户密钥
        String hmacKey = YiBaoConfig.getHmacKey();
        CommonResponse resultData = new CommonResponse();
        Map xmlMap = new LinkedHashMap();
        Map xmlBackMap = new LinkedHashMap();
        List<Map> itemList = new ArrayList<>();
        Map itemMap = new LinkedHashMap();

        //第一步:将请求的数据和商户自己的密钥拼成一个字符串,
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            logger.error("请求文档解析错误");
            return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
        }
        Element rootEle = document.getRootElement();
        List list = rootEle.elements();
        for (int i = 0; i < list.size(); i++) {
            Element ele = (Element) list.get(i);
            String eleName = ele.getName();
            if (!eleName.equals("list")) {
                xmlMap.put(eleName, ele.getText().trim());
            } else {
                continue;
            }
        }

        String hmacStr = "";
        for (int i = 0; i < digestValues.length; i++) {
            if (digestValues[i].equals("hmacKey")) {
                hmacStr = hmacStr + hmacKey;
                continue;
            }
            hmacStr = hmacStr + xmlMap.get(digestValues[i]);
        }

        logger.info("签名之前的源数据为: {}", hmacStr);
        //第二步: 对请求的字符串进行MD5加密和数字证书签名
        logger.info("生成签名url: {}", YiBaoConfig.getSignUrl() + "sign?req=" + hmacStr);
        String signMessage = restTemplate.getForObject(YiBaoConfig.getSignUrl() + "sign?req=" + hmacStr, String.class);
        logger.info("生成签名: {}", signMessage);

        Element tag = rootEle.element("hmac");
        tag.setText(signMessage);
        document.setXMLEncoding("GBK");
        logger.info("完整xml请求报文: {}", document.asXML());
        logger.info("请求地址为:{}", postUrl);

        //第三步:发送https请求
        String responseMsg = CallbackUtils.httpRequest(postUrl, document.asXML(), "POST", "gbk", "text/xml ;charset=gbk", false);
        logger.info("服务器响应xml报文: {}", responseMsg);

        Document backDocument = null;
        try {
            backDocument = DocumentHelper.parseText(responseMsg);
        } catch (DocumentException e) {
            logger.error("文档解析错误");
            return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
        }
        Element backRootEle = backDocument.getRootElement();
        String hmacValue = backRootEle.elementText("hmac");
        logger.info("响应签名信息: {}", hmacValue);

        //第四步:对服务器响应报文进行验证签名
        if (hmacValue == null) {
            logger.error("服务器响应报文有误");
            return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
        }

        //第五步.将验签出来的结果数据与自己针对响应数据做MD5签名之后的数据进行比较是否相等
        List backlist = backRootEle.elements();
        for (int i = 0; i < backlist.size(); i++) {
            Element ele = (Element) backlist.get(i);
            String eleName = ele.getName();
            if (!eleName.equals("list")) {
                xmlBackMap.put(eleName, ele.getText().trim());
            } else {
                Iterator listI = ele.elementIterator();
                while (listI.hasNext()) {
                    ele = (Element) listI.next();
                    eleName = ele.getName();
                    if ("items".equalsIgnoreCase(eleName)) {
                        Iterator itemsI = ele.elementIterator();
                        while (itemsI.hasNext()) {
                            ele = (Element) itemsI.next();
                            eleName = ele.getName();
                            if ("item".equalsIgnoreCase(eleName)) {
                                Iterator itemI = ele.elementIterator();
                                while (itemI.hasNext()) {
                                    ele = (Element) itemI.next();
                                    eleName = ele.getName();
                                    itemMap.put(eleName, ele.getText().trim());
                                }
                                itemList.add(itemMap);
                            }
                        }
                        xmlBackMap.put("items", itemList);
                    }
                }
                continue;
            }
        }
        logger.info("代付请求response: {}", JSONObject.toJSONString(xmlBackMap));
        String backHmacStr = "";
        for (int i = 0; i < backDigestValues.length; i++) {
            if (backDigestValues[i].equals("hmacKey")) {
                backHmacStr = backHmacStr + hmacKey;
                continue;
            }
            String tempStr = (String) xmlBackMap.get(backDigestValues[i]);
            backHmacStr = backHmacStr + ((tempStr == null) ? "" : tempStr);
        }
        logger.info("提交返回源数据为: {}", backHmacStr);
        logger.info("验证签名url: {}", YiBaoConfig.getSignUrl() + "verify?req=" + backHmacStr + "&&sign=" + hmacValue);
        String verifyResult = restTemplate.getForObject(YiBaoConfig.getSignUrl() + "verify?req=" + backHmacStr + "&&sign=" + hmacValue, String.class);
        if ("SUCCESS".equals(verifyResult)) {
            logger.info("md5验签成功, 返回源数据:{}", JSONObject.toJSONString(xmlBackMap));
            resultData.setData(xmlBackMap);
        } else {
            logger.error("md5验签失败");
            return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
        }

        return resultData;
    }

    /**
     * 解析
     *
     * @param retCode    系统返回码
     * @param r1Code     打款状态码
     * @param bankStatus 银行处理状态码
     * @param errorMsg   说明
     * @return
     */
    private CommonResponse parseResult(String retCode, String r1Code, String bankStatus, String errorMsg) {
        if (!"1".equals(retCode) && !"9999".equals(retCode)) {
            //既不是1也不是9999	易宝未接收	账户金额未变
            return CommonResponse.builder().result(1).status(0).msg(errorMsg).build();
        }
        if ("9999".equals(retCode) || "0025".equals(r1Code)) {
            //9999 系统异常，出款情况不明，需要再次查询直到系统返回码不是9999
            ///0025 易宝已经接收了这笔打款，但未发至银行
            return CommonResponse.builder().result(0).status(1).msg(errorMsg).build();
        }
        if ("0026".equals(r1Code)) {
            if ("S".equalsIgnoreCase(bankStatus)) {
                //出款已成功到账
                return CommonResponse.build(TransactionPayEnum.GLOBAL_SUCCESS);
            }
            if ("F".equalsIgnoreCase(bankStatus)) {
                //出款失败但尚未退回商户账户
                return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
            }
            if ("W".equalsIgnoreCase(bankStatus) || "I".equalsIgnoreCase(bankStatus) || "U".equalsIgnoreCase(bankStatus)) {
                //W 出款尚未发到银行
                //I 出款已发至银行，银行正在处理
                //U 未知，易宝与银行对账失败，但此出款既可能成功也可能失败
                return CommonResponse.builder().result(0).status(1).msg(errorMsg).build();
            }
        }
        if ("0027".equals(r1Code) || "0028".equals(r1Code)) {
            //0027 易宝已经将款退回商户账户
            //0028 易宝已经退回此请求，出款金额退回商户账户，此笔出款未发至银行
            return CommonResponse.builder().result(1).status(0).msg(errorMsg).build();
        }
        if ("0029".equals(r1Code) || "0030".equals(r1Code)) {
            //0029 易宝已经接收此请求，但此请求有待商户登录易宝商户后台复核出款
            //0030 已经接收，但是具体出款情况不明，需要再次查询直到打款状态码不是0030
            return CommonResponse.builder().result(0).status(1).msg(errorMsg).build();
        }
        return CommonResponse.build(TransactionPayEnum.GLOBAL_FAIL);
    }

}
