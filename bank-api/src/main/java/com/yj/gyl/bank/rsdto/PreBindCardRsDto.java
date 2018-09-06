package com.yj.gyl.bank.rsdto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PreBindCardRsDto {

    @ApiModelProperty(value = "银行卡号")
    private String cardNo;

    @ApiModelProperty(value = "身份证号码")
    private String idCardNo;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户实名")
    private String userName;

    @ApiModelProperty(value = "市")
    private String city;

    @ApiModelProperty(value = "省份")
    private String province;

    @ApiModelProperty(value = "交易通道")
    private String channel;

    @ApiModelProperty(value = "交易来源")
    private String source;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "平台返回的唯一号")
    private String orderId;

    @ApiModelProperty(value = "农业银行")
    private String bankName;

    @ApiModelProperty(value = "返回数据 可为空")
    private String data;

    @ApiModelProperty(value = "创建时间")
    private long createTime;;

}
