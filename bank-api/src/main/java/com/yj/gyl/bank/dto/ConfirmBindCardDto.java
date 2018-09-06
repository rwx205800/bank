package com.yj.gyl.bank.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfirmBindCardDto {

    @ApiModelProperty(value = "绑卡请求的交易号")
    private String orderNo;

    @ApiModelProperty(value = "用户收到的短信验证码")
    private String validateCode;

    @ApiModelProperty(value = "交易通道")
    private String channel;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "交易来源")
    private String source;
}
