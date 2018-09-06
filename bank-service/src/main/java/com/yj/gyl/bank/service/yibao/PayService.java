package com.yj.gyl.bank.service.yibao;

import com.alibaba.fastjson.JSONObject;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.rsdto.PreBindCardRsDto;
import com.yj.gyl.bank.dto.*;
import com.yj.gyl.bank.handler.common.TradeTypeEnum;
import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.rsdto.ConfirmBindCardRsDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import com.yj.gyl.bank.service.IPayService;
import com.yj.gyl.bank.service.yibao.common.YiBaoConfig;
import com.yj.gyl.bank.service.yibao.common.YiBaoService;
import com.yj.gyl.bank.service.yibao.util.YiBaoInfoUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by renfei on 2017/12/18.
 */
@Service
public class PayService extends YiBaoService implements IPayService {
    private static final Logger logger = LoggerFactory.getLogger(PayService.class);

    /**
     * 待验证
     */
    private static final String TO_VALIDATE = "TO_VALIDATE";
    /**
     * 绑卡成功
     */
    private static final String BIND_SUCCESS = "BIND_SUCCESS";
    /**
     * 待处理
     */
    private static final String PROCESSING = "PROCESSING";

    @Override
    public CommonResponse<PreBindCardRsDto> preBindCard(PreBindCardDto preBindCardDto, TMerchant merchant) {
        CommonResponse<PreBindCardRsDto> commonResponse = new CommonResponse<>();
        logger.info("【易宝绑卡】:{}", preBindCardDto);
        //预绑卡短信
        Map<String, String> result = bindCardRequest(preBindCardDto, merchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            //系统异常
            logger.info("易宝短信绑卡发送异常:" + result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            logger.info("易宝短信预绑卡业务异常:" + result.get("errormsg"));
            if (result.get("errorcode") != null && "TZ1001004".equals(result.get("errorcode").toString())) {
                commonResponse.setMsg("验证码已发送");
            } else {
                commonResponse.setMsg(result.get("errormsg"));
            }
            commonResponse.setStatus(1);
//            commonResponse.setMsg(result.get("errormsg"));
        } else if (TO_VALIDATE.equals(result.get("status").toString())) {
            //短信发送成功 待验证
            PreBindCardRsDto preBindCardRsDto = new PreBindCardRsDto();
            org.springframework.beans.BeanUtils.copyProperties(preBindCardDto, preBindCardRsDto);
            preBindCardRsDto.setOrderNo(result.get("requestno").toString());
            preBindCardRsDto.setOrderId(result.get("yborderid").toString());
            preBindCardRsDto.setData(JSONObject.toJSONString(result));
            commonResponse.setMsg("待短信验证");
            commonResponse.setData(preBindCardRsDto);
        } else {
            commonResponse.setStatus(1);
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
        }

        return commonResponse;
    }

    @Override
    public CommonResponse<ConfirmBindCardRsDto> confirmBindCard(ConfirmBindCardDto confirmBindCardDto, TMerchant merchant) {
        CommonResponse<ConfirmBindCardRsDto> commonResponse = new CommonResponse<>();
        logger.info("【易宝确认绑卡】:" + JSONObject.toJSONString(confirmBindCardDto));
        Map<String, String> result = bindCardConfirm(confirmBindCardDto, merchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            //系统错误
            logger.info("易宝系统绑卡异常:" + result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg").toString());
        } else if (BIND_SUCCESS.equals(result.get("status").toString())) {
            //绑卡成功
            ConfirmBindCardRsDto confirmBindCardRsDto = new ConfirmBindCardRsDto();
            org.springframework.beans.BeanUtils.copyProperties(confirmBindCardDto, confirmBindCardRsDto);
            confirmBindCardRsDto.setBankCode(result.get("bankcode").toString());
            commonResponse.setMsg("绑卡成功");
            commonResponse.setData(confirmBindCardRsDto);
        } else {
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
        }

        return commonResponse;
    }

    @Override
    public CommonResponse delBindCard(TBankCard bankCard, TMerchant tMerchant) {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> result = unBindCard(bankCard, tMerchant); //绑卡确认
        if (result.get("customError") != null && !"".equals(result.get("customError"))) { //系统错误
            logger.info("易宝系统解绑异常:" + result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg("易宝系统解绑异常");
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) { //业务错误
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg").toString());
        } else if ("SUCCESS".equals(result.get("status").toString())) { //绑卡成功
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
            commonResponse.setData(result);
        } else {
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg("参数异常");
        }
        return commonResponse;
    }

    @Override
    public CommonResponse getAllBindCards(Long userId) {
        return null;
    }

    @Override
    public CommonResponse<TradeRecordRsDto> preRecharge(PreRechargeDto preRechargeDto, TMerchant tMerchant) {
        CommonResponse<TradeRecordRsDto> commonResponse = new CommonResponse<>();
        logger.info("[易宝预充值入参]: {}", JSONObject.toJSONString(preRechargeDto));
        Map<String, String> result = preYiBaoRecharge(preRechargeDto, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            logger.info("[易宝短信预充值发送异常]: {}", result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            logger.info("[易宝短信预充值业务异常]: {}", result.get("errormsg"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg"));
        } else if (TO_VALIDATE.equals(result.get("status").toString())) {
            //短信发送成功 待验证
            TradeRecordRsDto tradeRecordRsDto = new TradeRecordRsDto();
            org.springframework.beans.BeanUtils.copyProperties(preRechargeDto, tradeRecordRsDto);
            tradeRecordRsDto.setOrderNo(result.get("requestno").toString());
            tradeRecordRsDto.setStatus(result.get("status").toString());
            tradeRecordRsDto.setReqMsg(result.get("reqMsg").toString());
            tradeRecordRsDto.setData(JSONObject.toJSONString(result));
            commonResponse.setMsg("短信已发送,请注意查收");
            commonResponse.setData(tradeRecordRsDto);
        } else {
            logger.info("[易宝短信预充值系统异常]");
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
        }

        return commonResponse;
    }

    @Override
    public CommonResponse<TradeRecordRsDto> confirmRecharge(ConfirmRechargeDto confirmRechargeDto, TMerchant tMerchant) {
        CommonResponse<TradeRecordRsDto> commonResponse = new CommonResponse<>();
        logger.info("[易宝充值确认入参]: {}", JSONObject.toJSONString(confirmRechargeDto));
        Map<String, String> result = confirmYibaoRecharge(confirmRechargeDto, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            //短信结果
            logger.info("[易宝短信充值确认异常]: {}", result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            logger.info("[易宝短信充值确认异常]: {}", result.get("errormsg"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg"));
        } else if (PROCESSING.equals(result.get("status").toString())) {
            TradeRecordRsDto tradeRecordRsDto = new TradeRecordRsDto();
            org.springframework.beans.BeanUtils.copyProperties(confirmRechargeDto, tradeRecordRsDto);
            tradeRecordRsDto.setOrderId(result.get("yborderid").toString());
            tradeRecordRsDto.setStatus(result.get("status").toString());
            tradeRecordRsDto.setData(JSONObject.toJSONString(result));
            tradeRecordRsDto.setReqMsg(result.get("reqMsg").toString());
            commonResponse.setMsg("充值处理中");
            commonResponse.setData(tradeRecordRsDto);
        } else {
            logger.info("[易宝短信充值确认系统异常]");
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
        }
        return commonResponse;
    }

    @Override
    public CommonResponse rechargeResendSMS(String requestNo, TMerchant tMerchant) {
        CommonResponse commonResponse = new CommonResponse<>();
        logger.info("[充值重发短信验证码] 充值请求号:{}", requestNo);
        Map<String, String> result = rechargeResendSms(requestNo, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            //短信结果
            logger.info("[充值重发短信验证码异常]: {}", result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            logger.info("[充值重发短信验证码异常]: {}", result.get("errormsg"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg"));
        } else if (TO_VALIDATE.equals(result.get("status").toString())) {
            //短信发送成功 待验证
            commonResponse.setMsg("充值短信重发成功");
            commonResponse.setData(result);
        } else {
            logger.info("[充值重发短信验证码系统异常]");
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg("参数异常");
        }

        return commonResponse;
    }

    @Override
    public CommonResponse rechargeRecord(String requestNo, TMerchant tMerchant, Date payTime) {
        CommonResponse commonResponse = new CommonResponse();
        if (StringUtils.isBlank(requestNo)) {
            logger.info("[易宝充值查询异常]: 充值请求号不能为空");
            commonResponse.setMsg("失败");
            commonResponse.setData("充值请求号不能为空");
            return commonResponse;
        }
        Map<String, String> result = rechargeRecordQuery(requestNo, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            logger.info("[易宝充值查询，异常]: {}", result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            logger.info("[易宝充值查询，业务异常]: {}", result.get("errormsg"));
            result.put("payTime", String.valueOf(payTime.getTime()));
            commonResponse.setMsg(result.get("errormsg"));
            commonResponse.setData(result);
        } else {
            commonResponse.setMsg("成功");
            result.put("payTime", String.valueOf(payTime.getTime()));
            commonResponse.setData(result);
        }

        return commonResponse;
    }

    @Override
    public CommonResponse<TradeRecordRsDto> withdraw(WithdrawDto withdrawDto, TMerchant merchant) {
        CommonResponse<TradeRecordRsDto> commonResponse = new CommonResponse<>();
       /* if (!YiBaoConfig.isPreFlag()) {
            //短信发送成功 待验证
            TradeRecordRsDto tradeRecordRsDto = new TradeRecordRsDto();
            org.springframework.beans.BeanUtils.copyProperties(withdrawDto, tradeRecordRsDto);
            tradeRecordRsDto.setOrderId("yborderid");
            tradeRecordRsDto.setOrderNo(withdrawDto.getOrderNo());
            tradeRecordRsDto.setStatus("1");
            tradeRecordRsDto.setReqMsg("reqMsg");
            //交易类型（0:充值  1:提现）
            tradeRecordRsDto.setType(TradeTypeEnum.withDraw.getValue());
            commonResponse.setMsg("(测试。。。)");
            commonResponse.setData(tradeRecordRsDto);
            return commonResponse;
        }*/
        logger.info("【易宝提现】:" + JSONObject.toJSONString(withdrawDto));
        Map<String, String> result = withdrawApplication(withdrawDto, withdrawDto.getUserId().toString(), merchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            logger.info("易宝提现异常:" + result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg").toString());
        } else if (PROCESSING.equals(result.get("status").toString())) {
            //短信发送成功 待验证
            TradeRecordRsDto tradeRecordRsDto = new TradeRecordRsDto();
            org.springframework.beans.BeanUtils.copyProperties(withdrawDto, tradeRecordRsDto);
            tradeRecordRsDto.setOrderId(result.get("yborderid").toString());
            tradeRecordRsDto.setOrderNo(result.get("requestno").toString());
            tradeRecordRsDto.setStatus(result.get("status").toString());
            tradeRecordRsDto.setReqMsg(result.get("reqMsg").toString());
            //交易类型（0:充值  1:提现）
            tradeRecordRsDto.setType(TradeTypeEnum.withDraw.getValue());
            commonResponse.setMsg("易宝处理中");
            commonResponse.setData(tradeRecordRsDto);
        } else {
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(YiBaoInfoUtils.getInfo(result.get("status").toString()));
        }

        return commonResponse;
    }

    @Override
    public CommonResponse withdrawRecord(TradeRecordDto recordRsDto, TMerchant tMerchant) {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> result = withdrawRecordQuery(recordRsDto, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            //短信结果
            logger.info("易宝提现查询异常:" + result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            //业务错误
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg").toString());
        } else {
            commonResponse.setMsg("成功");
            commonResponse.setData(result);
        }

        return commonResponse;
    }

    @Override
    public CommonResponse queryBindCardList(Long userId, String cardNo, TMerchant tMerchant) {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> result = getBindCardList(userId, cardNo, tMerchant);
        if (result.get("customError") != null && !"".equals(result.get("customError"))) {
            logger.info("易宝绑卡列表查询异常:{}", result.get("customError"));
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("customError"));
        } else if (result.get("errormsg") != null && !"".equals(result.get("errormsg"))) {
            commonResponse.setStatus(-1);
            commonResponse.setResult(1);
            commonResponse.setMsg(result.get("errormsg").toString());
        } else {
            commonResponse.setMsg("成功");
            commonResponse.setData(result);
        }

        return commonResponse;
    }

    /**
     * 获取网银支付URL
     *
     * @param rechargeDto
     * @return
     */
    @Override
    public CommonResponse getYeePayUrl(YeePayRechargeDto rechargeDto) {
        CommonResponse commonResponse = new CommonResponse();

        Map<String, String> map = new HashMap<>();
        // 业务类型
        map.put("p0_Cmd", "Buy");
        // 商户编号
        map.put("p1_MerId", YiBaoConfig.getYeePayMerId());
        // 商户订单号
        map.put("p2_Order", rechargeDto.getOrderNO());
        // 支付金额
        map.put("p3_Amt", rechargeDto.getAmount().toString());
        // 交易币种
        map.put("p4_Cur", rechargeDto.getCurrency());
        // 商品名称
        map.put("p5_Pid", rechargeDto.getGoodName());
        map.put("p6_Pcat", "");
        map.put("p7_Pdesc", "");
        // 回调地址
        map.put("p8_Url", YiBaoConfig.getYeePayCallBack());
        map.put("p9_SAF", "");
        map.put("pa_MP", "");
        map.put("pb_ServerNotifyUrl", "");
        // 支付通道编码
        map.put("pd_FrpId", rechargeDto.getFrpCode());
        // 订单有效期
        map.put("pm_Period", rechargeDto.getPeriod().toString());
        // 订单有效期单位
        map.put("pn_Unit", rechargeDto.getUnitOfDate());
        map.put("pr_NeedResponse", "");
        // 用户姓名
        map.put("pt_UserName", rechargeDto.getUserName());
        // 身份证号
        map.put("pt_PostalCode", rechargeDto.getIdCardNO());
        map.put("pt_Address", "");
        // 银行卡号
        map.put("pt_TeleNo", rechargeDto.getBankCardNo());
        // 手机号
        map.put("pt_Mobile", rechargeDto.getPhone());
        map.put("pt_Email", "");
        map.put("pt_LeaveMessage", "");
        map.put("key", YiBaoConfig.getYeePayKey());

        //支付URL
        String url = getYeePayURL(map);
        commonResponse.setData(url);
        commonResponse.setMsg("获取支付地址成功");
        return commonResponse;
    }

    /**
     * 网银充值订单查询
     *
     * @param orderNo
     * @return
     */
    @Override
    public CommonResponse yeePayRechargeRecord(String orderNo) {
        CommonResponse commonResponse = new CommonResponse();
        Map<String, String> params = new HashMap<>();
        params.put("p2_Order", orderNo);
        // 默认值
        params.put("p3_ServiceType", "2");
        Map<String, String> result = queryByOrder(params);
        if (org.springframework.util.StringUtils.hasText(result.get("errorMsg"))) {
            commonResponse.setResult(1);
            commonResponse.setStatus(1);
            commonResponse.setMsg(result.get("errorMsg"));
            return commonResponse;
        }
        if (!"1".equals(result.get("r1_Code"))) {
            commonResponse.setResult(1);
            commonResponse.setStatus(1);
            commonResponse.setMsg("查询订单有误");
        }
        commonResponse.setData(result);
        return commonResponse;
    }
}
