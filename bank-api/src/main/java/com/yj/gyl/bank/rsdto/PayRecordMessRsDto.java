package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hepei
 * @date 2017/12/28 11:38:31
 */
@Data
public class PayRecordMessRsDto {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "卡号")
    private String cardNo;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "身份证号")
    private String idCardNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "银行名称")
    private String bankName;

    @ApiModelProperty(value = "银行编码")
    private String bankCode;

    @ApiModelProperty(value = "开户行")
    private String bankBranch;

    @ApiModelProperty(value = "开户省份")
    private String province;

    @ApiModelProperty(value = "开户市")
    private String city;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "状态 0:处理中 1:成功 2:失败")
    private Integer status;

    @ApiModelProperty(value = "交易类型 0:充值 1:提现")
    private Integer type;

    @ApiModelProperty(value = "交易通道")
    private String channel;

    @ApiModelProperty(value = "交易来源")
    private String source;

}
