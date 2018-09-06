package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.ITransferServiceApi;
import com.yj.gyl.bank.dto.TransferRecordDto;
import com.yj.gyl.bank.handler.ITransferHandler;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by renfei on 2017/12/19.
 */
@RestController
public class TransferServiceController extends AbstractController implements ITransferServiceApi {
    @Autowired
    private ITransferHandler transferHandler;

    @Override
    public CommonResponse transferSingle(@ApiParam(value = "项目ID", required = true) @RequestParam("projectId") String projectId,
                                         @ApiParam(value = "金额", required = true) @RequestParam("amount") String amount) {
        return transferHandler.transferSingle(projectId, amount);
    }

    @Override
    public CommonResponse transAcountDetail(@RequestBody TransferRecordDto transferRecordDto) {
        return transferHandler.transAcountDetail(transferRecordDto);
    }
}
