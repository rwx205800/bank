package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IYopTransferServiceApi;
import com.yj.gyl.bank.dto.YopTransferSendDto;
import com.yj.gyl.bank.handler.IYopHandler;
import com.yj.gyl.bank.rsdto.TransferSendQueryRsDto;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by renfei on 2018/6/27.
 */
@RestController
public class YopTransferController extends AbstractController implements IYopTransferServiceApi {
    @Autowired
    private IYopHandler yopHandler;

    @Override
    public CommonResponse transferSend(@RequestBody YopTransferSendDto transferDto) {
        return yopHandler.transferSend(transferDto,getUserId());
    }

    @Override
    public CommonResponse<TransferSendQueryRsDto> transferSendQuery(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam("orderNo") String orderNo) {
        return yopHandler.transferSendDetailQuery(orderNo,getUserId());
    }
}
