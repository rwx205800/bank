package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hepei
 * @date 2017/12/26 13:33:12
 */
@Data
public class TransferRecordDto {
    @ApiModelProperty(value = "项目id")
    private Long projectId;

    @ApiModelProperty(value = "打款批次号")
    private String batchNo;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "页数")
    private Integer pageSize = 1;

    @ApiModelProperty(value = "交易来源")
    private String source;

}
