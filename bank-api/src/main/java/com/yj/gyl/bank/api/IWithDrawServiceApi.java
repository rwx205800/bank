package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.WithdrawDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
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
 * Created by chenhanning on 2017/9/14.
 */
@Api(value = "/WithDrawService", tags = "WithDrawService", description = "提现模块", hidden = true)
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IWithDrawServiceApi {

    /**
     * 资金提现
     *
     * @param withdrawDto
     * @return
     */
    @RequestMapping(value = "/i/transaction/withdraw", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "资金提现", httpMethod = "POST", tags = "WithDrawService")
    CommonResponse<TradeRecordRsDto> withdraw(@RequestBody WithdrawDto withdrawDto);

    /**
     * 提现记录查询
     *
     * @param orderNo 订单号
     * @return
     */
    @RequestMapping(value = "/i/transaction/withdrawRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "提现申请查询", notes = "提现申请查询", httpMethod = "GET")
    CommonResponse withdrawRecord(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam(value = "orderNo") String orderNo);

}
