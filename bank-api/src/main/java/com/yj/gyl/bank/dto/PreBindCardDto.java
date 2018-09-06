package com.yj.gyl.bank.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PreBindCardDto {

    @ApiModelProperty(value = "银行卡号", example = "3231201213145646")
    private String cardNo;

    @ApiModelProperty(value = "身份证号码", example = "320282XXXXXXX")
    private String idCardNo;

    @ApiModelProperty(value = "用户id", example = "20000")
    private Long userId;

    @ApiModelProperty(value = "手机号", example = "15031536XXX")
    private String phone;

    @ApiModelProperty(value = "用户实名", example = "张三")
    private String userName;

    @ApiModelProperty(value = "市,可为空", example = "无锡")
    private String city;

    @ApiModelProperty(value = "省 可为空", example = "江苏")
    private String province;

    @ApiModelProperty(value = "银行卡名称 可为空", example = "农业银行")
    private String bankName;

    @ApiModelProperty(value = "交易来源")
    private String source;

}
