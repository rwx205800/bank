package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by renfei on 2018/6/29.
 */
@Data
public class TradeOrderQueryRsDto {
    @ApiModelProperty("订单号")
    private String orderId;
    @ApiModelProperty(value = "1）PROCESSING 处理中（非终态）\n" +
            "2）SUCCESS 订单成功（终态）\n" +
            "3）CLOSED 订单关闭（终态）\n" +
            "4）TIME_OUT 订单超时（终态）\n" +
            "5）REJECT 订单拒绝（终态）\n" +
            "6）REPEALED 订单撤销（分账订单退款后\n" +
            "查询）\n" +
            "7）REVOKED 订单取消（网银订单）\n" +
            "8）REVERSAL冲正")
    private String status;
    @ApiModelProperty("支付金额")
    private String amount;
    @ApiModelProperty("支付完成时间")
    private String paySuccessDate;
}
