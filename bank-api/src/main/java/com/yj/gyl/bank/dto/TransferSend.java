package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by renfei on 2018/6/29.
 */
@Data
public class TransferSend {
    @ApiModelProperty("订单号")
    private String orderId;
    @ApiModelProperty("批次号")
    private String batchNo;
    @ApiModelProperty("出款金额 元 2位小数")
    private BigDecimal amount;
    @ApiModelProperty("收款人姓名")
    private String accountName;
    @ApiModelProperty("银行卡卡号")
    private String accountNumber;
    @ApiModelProperty("收款银行编号")
    private String bankCode;
    @ApiModelProperty("银行名称")
    private String bankName;
    @ApiModelProperty("省份")
    private String province;
    @ApiModelProperty("城市")
    private String city;
}
