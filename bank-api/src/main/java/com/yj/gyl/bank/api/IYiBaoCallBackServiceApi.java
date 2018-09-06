package com.yj.gyl.bank.api;

import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by chenhanning on 2017/9/14.
 *
 * @author
 */
@Api(value = "/YiBaoCallBackService", tags = "YiBaoCallBackService", description = "易宝支付回调", hidden = true)
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IYiBaoCallBackServiceApi {

    @RequestMapping(value = "/n/transaction/yb/callbackRecharge/{merchantId}", method = RequestMethod.POST)
    @ApiOperation(hidden = true, value = "易宝充值确认回调接口", notes = "易宝充值回调接口", httpMethod = "POST")
    void callbackRecharge(@ApiParam(name = "merchantId", value = "商户id", required = true) @PathVariable("merchantId") Long merchantId) throws Exception;

    @RequestMapping(value = "/n/transaction/yb/callbackWithDraw/{merchantId}", method = RequestMethod.POST)
    @ApiOperation(hidden = true, value = "易宝提现确认回调接口", notes = "易宝提现回调接口", httpMethod = "POST")
    void callbackWithDraw(@ApiParam(name = "merchantId", value = "商户id", required = true) @PathVariable("merchantId") Long merchantId) throws Exception;

    /**
     * 易宝网银充值回调接口
     *
     * @throws Exception
     */
    @RequestMapping(value = "/n/transaction/yb/yeePayCallback", method = RequestMethod.POST)
    @ApiOperation(hidden = true, value = "易宝网银充值回调接口", notes = "易宝网银充值回调接口", httpMethod = "POST")
    void callbackEbankRecharge() throws Exception;


}
