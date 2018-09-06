package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by renfei on 2018/6/29.
 */
@Data
public class TransferSendQueryRsDto {
    @ApiModelProperty("订单号")
    private String orderId;
    @ApiModelProperty("出款金额")
    private String amount;
    @ApiModelProperty("状态 0:待确认 1：成功 2失败")
    private String status;
}
