package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CallbackDto implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "交易orderNo")
    private String orderNo;

    @ApiModelProperty(value = "操作结果 0：成功  1：业务错误(msg写失败描述)、 -1：系统异常 -2：服务拒绝访问")
    private int result;

    @ApiModelProperty(value = "说明信息")
    private String msg = "";

    @ApiModelProperty(value = "数据集JSON")
    private String dataJson;

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
