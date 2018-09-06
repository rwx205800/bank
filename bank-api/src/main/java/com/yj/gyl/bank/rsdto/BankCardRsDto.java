package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BankCardRsDto {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "开户行")
    private String bankBranch;

    @ApiModelProperty(value = "开户省份")
    private String province;

    @ApiModelProperty(value = "开户市")
    private String city;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCardNo;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "银行卡图片", example = "https://sssssss")
    private String cardImg;
}

