package com.yj.gyl.bank.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TradeRecordDto {

    @ApiModelProperty(value = "交易订单号")
    private String orderNo;

    @ApiModelProperty(value = "交易通道的流水号")
    private String orderId;

}
