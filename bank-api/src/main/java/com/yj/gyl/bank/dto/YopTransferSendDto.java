package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by renfei on 2018/6/27.
 */
@Data
public class YopTransferSendDto {
    @ApiModelProperty("出款金额 元 2位小数")
    private String amount;
    @ApiModelProperty("收款人姓名")
    private String accountName;
    @ApiModelProperty("银行卡卡号")
    private String accountNumber;
    @ApiModelProperty("收款银行编号")
    private String bankCode;
    @ApiModelProperty("手续费方式 SOURCE：商户承担  TARGET：用户承担")
    private String feeType;
}
