package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * @author hepei
 * @date 2018/03/09 13:22:50
 */
@Data
public class YopTradeOrderDto {
    @ApiModelProperty("订单金额 两位小数")
    @NotBlank
    private String orderAmount;
}
