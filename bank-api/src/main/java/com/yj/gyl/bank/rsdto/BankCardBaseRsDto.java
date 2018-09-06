package com.yj.gyl.bank.rsdto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by renfei on 2018/3/1.
 */
@Data
public class BankCardBaseRsDto {
    /**
     * 银行名称
     */
    @ApiModelProperty(value = "银行名称")
    private String name;

    /**
     * 银行简称
     */
    @ApiModelProperty(value = "code")
    private String code;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "imgUrl",hidden = true)
    private String imgUrl;

    @ApiModelProperty(value = "description")
    private String description;
}
