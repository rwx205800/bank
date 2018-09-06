package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.TransferRecordDto;
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
@Api(value = "/TransferService", tags = "TransferService", description = "代付代发", hidden = true)
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.INNER)
public interface ITransferServiceApi {
    /**
     * 单笔打款(代付代发)
     *
     * @param projectId
     * @param amount
     * @return
     */
    @RequestMapping(value = "/i/transaction/transferSingle", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "单笔打款(代付代发)", notes = "单笔打款(代付代发)", httpMethod = "POST", tags = "TransferService")
    CommonResponse transferSingle(@ApiParam(value = "项目ID", required = true) @RequestParam("projectId") String projectId,
                                  @ApiParam(value = "金额", required = true) @RequestParam("amount") String amount);


    /**
     * 查询打款明细
     *
     * @param transferRecordDto
     * @return
     */
    @RequestMapping(value = "/i/transaction/transAcountDetail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "打款明细", notes = "打款明细", httpMethod = "POST", tags = "TransferService")
    CommonResponse transAcountDetail(@RequestBody TransferRecordDto transferRecordDto);
}
