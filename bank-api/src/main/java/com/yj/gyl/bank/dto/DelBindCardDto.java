package com.yj.gyl.bank.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DelBindCardDto {

    @ApiModelProperty(value = "银行卡Id")
    private String cardNo;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "交易来源")
    private String source;
}
