package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.WithdrawDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;

/**
 * @author hepei
 * @date 2017/12/13 19:05:09
 */
public interface IWithDrawHandler {
    /**
     * 资金提现
     *
     * @param withdrawDto
     * @return
     */
    CommonResponse<TradeRecordRsDto> withdraw(WithdrawDto withdrawDto);

    /**
     * 提现记录查询
     *
     * @param orderNo    订单号
     * @return
     */
    CommonResponse withdrawRecord(String orderNo);
}
