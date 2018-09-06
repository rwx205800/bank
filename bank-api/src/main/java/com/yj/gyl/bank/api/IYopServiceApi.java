package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.YopTradeOrderDto;
import com.yj.gyl.bank.rsdto.TradeOrderQueryRsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by renfei on 2018/6/27.
 */
@Api(value = "/YopService", tags = "YopService", description = "易宝B2B支付")
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IYopServiceApi {
    /**
     * 网银充值
     * @param tradeOrderDto 下单明细
     * @return
     */
    @RequestMapping(value = "/u/bank/yb/tradeOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "B2B网银充值下单", httpMethod = "POST", tags = "YopService")
    CommonResponse tradeOrder(@RequestBody YopTradeOrderDto tradeOrderDto);

    @RequestMapping(value = "/u/bank/yb/tradeOrderQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "B2B网银充值查询", httpMethod = "GET", tags = "YopService")
    CommonResponse<TradeOrderQueryRsDto> tradeOrderQuery(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam("orderNo") String orderNo);


}
