package com.yj.gyl.bank.rsdto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ConfirmBindCardRsDto {

    @ApiModelProperty(value = "交易通道")
    private String channel;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "银行代号")
    private String bankCode;

    @ApiModelProperty(value = "交易来源")
    private String source;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCardNo;

    @ApiModelProperty(value = "创建时间")
    private long createTime;

}
