package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawDto {

    @ApiModelProperty(value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "响应地址")
    private String responseUrl;

    @ApiModelProperty(value = "交易来源")
    private String source;

}
