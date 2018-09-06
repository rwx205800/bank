package com.yj.gyl.bank.api;

import com.yj.base.common.CommonResponse;
import com.yj.base.common.swagger.APIType;
import com.yj.base.common.swagger.APITypeEnum;
import com.yj.gyl.bank.dto.BindCardDto;
import com.yj.gyl.bank.dto.ConfirmBindCardDto;
import com.yj.gyl.bank.dto.DelBindCardDto;
import com.yj.gyl.bank.dto.PreBindCardDto;
import com.yj.gyl.bank.rsdto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by chenhanning on 2017/9/14.
 */
@Api(value = "/BankCardService", tags = "BankCardService", description = "银行卡模块", hidden = true)
@FeignClient(value = "gyl-bank")
@RequestMapping("v1")
@APIType(APITypeEnum.PUBLIC)
public interface IBankCardServiceApi {

    @RequestMapping(value = "/i/transaction/cardNoCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "是否绑过该卡", httpMethod = "GET", tags = "BankCardService")
    CommonResponse cardNoCheck(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source, @ApiParam(name = "cardNo", value = "卡号", required = true) @RequestParam(value = "cardNo") String cardNo);

    @RequestMapping(value = "/i/transaction/cardNoInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "查询银行卡名称", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<BankCardRsDto> cardNoInfo(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source, @ApiParam(name = "cardNo", value = "卡号", required = true) @RequestParam(value = "cardNo") String cardNo);

    @RequestMapping(value = "/i/transaction/bindCardCheck", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "是否首次绑卡", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<BankCardRsDto> bindCardCheck(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source);

    @RequestMapping(value = "/i/transaction/preBindCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "绑卡(发送短信)", httpMethod = "POST", tags = "BankCardService")
    CommonResponse<PreBindCardRsDto> preBindCard(@ApiParam(value = "绑卡(发送短信)", required = true) @RequestBody PreBindCardDto preBindCardDto);

    @RequestMapping(value = "/i/transaction/confirmBindCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "确认绑卡", httpMethod = "POST", tags = "BankCardService")
    CommonResponse<ConfirmBindCardRsDto> confirmBindCard(@RequestBody ConfirmBindCardDto confirmBindCardDto);

    @RequestMapping(value = "/i/transaction/delBindCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "删除绑卡", httpMethod = "POST", tags = "BankCardService")
    CommonResponse delBindCard(@RequestBody DelBindCardDto delBindCardDto);

    @RequestMapping(value = "/i/transaction/getAllBindCards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取绑卡信息", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<List<BankCardRsDto>> getAllBindCards(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source);

    @RequestMapping(value = "/u/transaction/getBankCards", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取银行卡列表", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<List<BankCardsRsDto>> getBankCards();

    @RequestMapping(value = "/n/transaction/bankCard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取渠道可绑银行卡列表", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<List<BankCardBaseRsDto>> bankCard();

    @RequestMapping(value = "/i/transaction/getUserInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取用户实名信息", httpMethod = "GET", tags = "BankCardService")
    CommonResponse<UserInfoRsDto> getUserInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId);

    @RequestMapping(value = "/i/transaction/queryBindCardList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "获取支付渠道绑卡列表", httpMethod = "GET", tags = "BankCardService")
    CommonResponse queryBindCardList(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId,
                                     @ApiParam(name = "cardNo", value = "银行卡号", required = true) @RequestParam(value = "cardNo") String cardNo,
                                     @ApiParam(name = "source", value = "交易来源", required = true) @RequestParam(value = "source") String source);


    @RequestMapping(value = "/i/transaction/saveBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(hidden = true, value = "保存绑卡信息", httpMethod = "POST", tags = "BankCardService")
    CommonResponse saveBankCard(@RequestBody BindCardDto bindCardDto);
}
