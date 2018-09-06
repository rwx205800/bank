package com.yj.gyl.bank.handler.common;

/**
 * @author hepei
 * @date 2017/12/19 16:31:03
 */
public enum TradeTypeEnum {
    recharge("充值", 0),
    withDraw("提现", 1);
    private String type;
    private Integer value;

    TradeTypeEnum(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
    public String getType() {
        return type;
    }
}
