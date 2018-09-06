package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IPayServiceApi;
import com.yj.gyl.bank.handler.IPayHandler;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hepei
 * @date 2017/12/28 10:50:09
 */
@RestController
public class PayController extends AbstractController implements IPayServiceApi {
    @Autowired
    private IPayHandler payHandler;

    @Override
    public CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(@ApiParam(name = "orderNo", value = "订单编号", required = true) @RequestParam(value = "orderNo") String orderNo) {
        return payHandler.showOrderMessByOrderNo(orderNo);
    }
}
