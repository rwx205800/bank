package com.yj.gyl.bank.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 */
@Data
public class ConfirmRechargeDto {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "支付请求的交易号")
    private String orderNo;

    @ApiModelProperty(value = "用户收到的短信验证码")
    private String validateCode;

}


