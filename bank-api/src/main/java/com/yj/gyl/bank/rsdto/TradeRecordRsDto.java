package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRecordRsDto {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(value = "充值金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "支付请求的交易号")
    private String orderNo;

    @ApiModelProperty(value = "交易通道返回的唯一ID")
    private String orderId;

    @ApiModelProperty(value = "回调地址")
    private String responseUrl;

    @ApiModelProperty(value = "状态")
    private String status;

    @ApiModelProperty(value = "请求信息")
    private String reqMsg;

    @ApiModelProperty(value = "响应数据")
    private String data;

    @ApiModelProperty(value = "交易类型")
    private Integer type;

    @ApiModelProperty(value = "交易来源")
    private String source;

}
