package com.yj.gyl.bank.mq;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hepei
 * @date 2018/01/04 18:55:14
 */
@Data
public class TransactionPayMessage {
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "交易金额")
    private BigDecimal money;

    @ApiModelProperty(value = "交易类型")
    private Integer type;

    @ApiModelProperty(value = "交易单号")
    private String orderNo;

    @ApiModelProperty(value = "交易时间")
    private Date createTime;

    @ApiModelProperty(value = "交易来源")
    private String source;

    @ApiModelProperty(value = "交易结果 0:发起 1:成功 2:失败")
    private Integer status;
}
