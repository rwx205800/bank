package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hepei
 * @date 2017/12/28 10:19:11
 */
@Api(tags = {"payService"}, description = "内部支付接口", hidden = true)
@RequestMapping(value = "v1")
@FeignClient(value = "gyl-bank")
@APIType(APITypeEnum.INNER)
public interface IPayServiceApi {
    /**
     * 通过订单编号获取订单详情
     *
     * @param orderNo 订单编号
     * @return
     */
    @RequestMapping(value = "/i/transaction/pay", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取订单详情", notes = "通过订单编号获取订单详情", httpMethod = "GET", tags = "payServiceApi")
    CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(@ApiParam(name = "orderNo", value = "订单编号", required = true) @RequestParam(value = "orderNo") String orderNo);


}
