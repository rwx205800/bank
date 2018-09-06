package com.yj.gyl.bank.handler.common;

/**
 * Created by renfei on 2017/12/18.
 */
public enum PaymentType {
    yibao("易宝", "YB");

    private String name;
    private String code;

    PaymentType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
