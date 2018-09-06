package com.yj.gyl.bank.handler.impl;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.handler.IPayHandler;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;
import com.yj.gyl.bank.service.common.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author hepei
 * @date 2017/12/28 11:04:44
 */
@Component
public class PayHandlerImpl implements IPayHandler {

    @Autowired
    private IPayService payService;

    @Override
    public CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(String orderNo) {
        if (!StringUtils.hasText(orderNo)) {
            return CommonResponse.<PayRecordMessRsDto>builder().result(1).status(1).msg("订单编号不能为空").build();
        }
        return payService.showOrderMessByOrderNo(orderNo);
    }
}
