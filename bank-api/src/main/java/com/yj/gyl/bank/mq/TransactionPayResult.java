package com.yj.gyl.bank.mq;

import com.yj.base.alimq.IMessageTagEnum;
import com.yj.base.alimq.annotations.MessageTagEntity;

/**
 * @author hzm 2017-10-31
 */

public enum TransactionPayResult implements IMessageTagEnum {
    @MessageTagEntity(value = "充值结果", dto = TransactionPayMessage.class)
    TRANSACTION_PAY_RECHARGE_RESULT,
    @MessageTagEntity(value = "提现结果", dto = TransactionPayMessage.class)
    TRANSACTION_PAY_WITHDRAW_RESULT;

}
