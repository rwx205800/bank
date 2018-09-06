package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.ConfirmRechargeDto;
import com.yj.gyl.bank.dto.PreRechargeDto;
import com.yj.gyl.bank.dto.YeePayRechargeDto;
import com.yj.gyl.bank.rsdto.TradeRecordRsDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author hepei
 */
@Api(value = "/RechargeService", tags = "RechargeService", description = "充值模块",hidden = true)
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IRechargeServiceApi {

    /**
     * 资金充值(发送短信)
     *
     * @param preRechargeDto
     * @return
     */
    @RequestMapping(value = "/i/transaction/preRecharge", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "预充值(发送短信)", notes = "充值发送短信验证码", httpMethod = "POST", tags = "RechargeService")
    CommonResponse<TradeRecordRsDto> preRecharge(@RequestBody PreRechargeDto preRechargeDto);

    /**
     * 确认充值
     *
     * @param confirmRechargeDto
     * @return
     */
    @RequestMapping(value = "/i/transaction/confirmRecharge", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "确认充值", notes = "确认充值", httpMethod = "POST", tags = "RechargeService")
    CommonResponse<TradeRecordRsDto> confirmRecharge(@RequestBody ConfirmRechargeDto confirmRechargeDto);

    /**
     * 充值 重发短信验证码
     *
     * @param orderNo 充值请求号
     * @return
     */
    @RequestMapping(value = "/i/transaction/rechargeResendSMS", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "预充值(重发短信)", notes = "充值重发短信验证码", httpMethod = "POST", tags = "RechargeService")
    CommonResponse rechargeResendSMS(@ApiParam(name = "orderNo", value = "充值请求号", required = true) @RequestParam("orderNo") String orderNo);

    /**
     * 充值记录查询
     *
     * @param orderNo 充值请求号
     * @return
     */
    @RequestMapping(value = "/i/transaction/rechargeRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "充值记录查询", notes = "充值记录查询", httpMethod = "GET", tags = "RechargeService")
    CommonResponse rechargeRecord(@ApiParam(name = "orderNo", value = "充值请求号", required = true) @RequestParam("orderNo") String orderNo);

    /**
     * 网银充值
     *
     * @param rechargeDto 充值明细
     * @return
     */
    @RequestMapping(value = "/i/transaction/yeePayRecharge", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "网银充值", httpMethod = "POST", tags = "RechargeService")
    CommonResponse yeePayRecharge(@RequestBody YeePayRechargeDto rechargeDto);

    /**
     * 网银充值记录查询
     *
     * @param orderNo
     * @return
     */
    @RequestMapping(value = "/i/transaction/yeePayRechargeRecord", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "网银充值记录查询", httpMethod = "GET", tags = "RechargeService")
    CommonResponse yeePayRechargeRecord(@ApiParam(name = "orderNo", value = "充值请求号", required = true) @RequestParam("orderNo") String orderNo);

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    @RequestMapping(value = "/i/transaction/yeePayBankFrpList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true,value = "获取银行通道编码表", notes = "易宝网银支付", httpMethod = "GET", tags = "RechargeService")
    CommonResponse yeePayBankFrpList();

}
