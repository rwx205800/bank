package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.YopTransferSendDto;
import com.yj.gyl.bank.rsdto.TransferSendQueryRsDto;
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
 * Created by chenhanning on 2017/10/23.
 */
@Api(value = "/YopTransferService", tags = "YopTransferService", description = "代付代发(提现)")
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IYopTransferServiceApi {
    @RequestMapping(value = "/u/bank/transferSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "单笔打款(提现)", notes = "单笔打款(提现)", httpMethod = "POST", tags = "YopTransferService")
    CommonResponse transferSend(@RequestBody YopTransferSendDto transferDto);

    @RequestMapping(value = "/u/bank/yb/transferSendQuery", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "单笔打款(提现)查询", httpMethod = "GET", tags = "YopTransferService")
    CommonResponse<TransferSendQueryRsDto> transferSendQuery(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam("orderNo") String orderNo);
}
