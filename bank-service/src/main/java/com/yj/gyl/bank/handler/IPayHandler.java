package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;

/**
 * @author hepei
 * @date 2017/12/28 11:04:14
 */
public interface IPayHandler {
    /**
     * 根据订单编号查询订单详情
     *
     * @param orderNo 订单编号
     * @return
     */
    CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(String orderNo);
}
