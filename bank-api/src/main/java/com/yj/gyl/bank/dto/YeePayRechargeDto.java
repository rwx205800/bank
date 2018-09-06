package com.yj.gyl.bank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hepei
 * @date 2018/03/09 13:22:50
 */
@Data
public class YeePayRechargeDto {

    @ApiModelProperty(value = "用户id", example = "1234")
    private Long userId;

    @ApiModelProperty(value = "用户姓名", example = "王小二")
    private String userName;

    @ApiModelProperty(value = "身份证", example = "112233######")
    private String idCardNO;

    @ApiModelProperty(value = "手机号", example = "157#######")
    private String phone;

    @ApiModelProperty(value = "银行卡号", example = "62220###########")
    private String bankCardNo;

    @ApiModelProperty(value = "订单编号", example = "WX102029029")
    private String orderNO;

    @ApiModelProperty(value = "支付金额", example = "10.00")
    private BigDecimal amount;

    @ApiModelProperty(value = "交易币种", example = "CNY")
    private String currency = "CNY";

    @ApiModelProperty(value = "商品名称", example = "满月红")
    private String goodName;

    @ApiModelProperty(value = "响应地址")
    private String responseUrl;

    @ApiModelProperty(value = "支付通道编码（默认：网银一键支付）")
    private String frpCode = "YJZF-NET-B2C";

    @ApiModelProperty(value = "订单有效期（默认：一天）")
    private Integer period = 1;

    @ApiModelProperty(value = "订单有效期单位（默认：day）", example = "year、month、day、hour、minute、second")
    private String unitOfDate = "day";

    @ApiModelProperty(value = "交易来源")
    private String source;
}
