package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IYopServiceApi;
import com.yj.gyl.bank.dto.YopTradeOrderDto;
import com.yj.gyl.bank.handler.IYopHandler;
import com.yj.gyl.bank.rsdto.TradeOrderQueryRsDto;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by renfei on 2018/6/27.
 */
@RestController
public class YopServiceController extends AbstractController implements IYopServiceApi{
    @Autowired
    private IYopHandler yopHandler;

    @Override
    public CommonResponse tradeOrder(@RequestBody @Valid YopTradeOrderDto tradeOrderDto) {
        return yopHandler.tradeOrder(tradeOrderDto,316929l);
    }

    @Override
    public CommonResponse<TradeOrderQueryRsDto> tradeOrderQuery(@ApiParam(name = "orderNo", value = "订单号", required = true) @RequestParam("orderNo") String orderNo) {
        return yopHandler.tradeOrderQuery(orderNo,getUserId());
    }
}
