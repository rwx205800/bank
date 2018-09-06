package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.YopTradeOrderDto;
import com.yj.gyl.bank.dto.YopTransferSendDto;
import com.yj.gyl.bank.rsdto.TradeOrderQueryRsDto;
import com.yj.gyl.bank.rsdto.TransferSendQueryRsDto;

/**
 * Created by renfei on 2018/6/27.
 */
public interface IYopHandler {

    CommonResponse tradeOrder(YopTradeOrderDto tradeOrderDto,Long userId);

    CommonResponse transferSend(YopTransferSendDto transferDto,Long userId);

    CommonResponse<TradeOrderQueryRsDto> tradeOrderQuery(String orderNo, Long userId);

    CommonResponse<TransferSendQueryRsDto> transferSendQuery(String orderNo, Long userId);

    CommonResponse<TransferSendQueryRsDto> transferSendDetailQuery(String orderNo, Long userId);
}
