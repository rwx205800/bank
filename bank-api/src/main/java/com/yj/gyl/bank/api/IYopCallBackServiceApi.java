package com.yj.gyl.bank.api;

import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by renfei on 2018/6/27.
 */
@Api(value = "/YopCallBackService", tags = "YopCallBackService", description = "易宝B2B支付回调")
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IYopCallBackServiceApi {
//    @RequestMapping(value = "/n/bank/yb/tradeOrderCallbackForPage", method = RequestMethod.POST)
//    @ApiOperation(value = "B2B网银充值回调", notes = "易宝网银充值回调接口", httpMethod = "POST")
//    void tradeOrderCallbackForPage() throws Exception;

    /**
     * 易宝网银充值回调接口
     * @throws Exception
     */
    @RequestMapping(value = "/n/bank/yb/tradeOrderCallback", method = RequestMethod.POST)
    @ApiOperation(value = "B2B网银充值回调", notes = "易宝网银充值回调接口", httpMethod = "POST")
    void tradeOrderCallback() throws Exception;

    @RequestMapping(value = "/n/bank/yb/transferSendCallback", method = RequestMethod.POST)
    @ApiOperation(value = "B2B网银代付打发回调", notes = "B2B网银代付打发回调", httpMethod = "POST")
    void transferSendCallback() throws Exception;

}
