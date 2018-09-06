package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IWithDrawServiceApi;
import com.yj.gyl.bank.dto.WithdrawDto;
import com.yj.gyl.bank.handler.IWithDrawHandler;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hepei
 * @date 2017/12/13 17:28:13
 */
@RestController
public class WithDrawController extends AbstractController implements IWithDrawServiceApi {

    @Autowired
    private IWithDrawHandler withDrawHandler;

    @Override
    public CommonResponse<TradeRecordRsDto> withdraw(@RequestBody WithdrawDto withdrawDto) {
        return withDrawHandler.withdraw(withdrawDto);
    }

    @Override
    public CommonResponse withdrawRecord(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam("orderNo") String orderNo) {
        return withDrawHandler.withdrawRecord(orderNo);
    }

}
