package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.api.IBankCardServiceApi;
import com.yj.gyl.bank.dto.BindCardDto;
import com.yj.gyl.bank.dto.ConfirmBindCardDto;
import com.yj.gyl.bank.dto.DelBindCardDto;
import com.yj.gyl.bank.dto.PreBindCardDto;
import com.yj.gyl.bank.handler.IBankCardHandler;
import com.yj.gyl.bank.rsdto.*;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hepei
 * @date 2017/12/13 17:26:25
 */
@RestController
public class BankCardController extends AbstractController implements IBankCardServiceApi {
//
    @Autowired
    private IBankCardHandler bankCardHandler;

    @Override
    public CommonResponse cardNoCheck(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source, @ApiParam(name = "cardNo", value = "卡号", required = true) @RequestParam(value = "cardNo") String cardNo) {
        return bankCardHandler.cardNoCheck(userId, source, cardNo);
    }

    @Override
    public CommonResponse<BankCardRsDto> cardNoInfo(@ApiParam(name = "userId", value = "userId", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "来源", required = true) @RequestParam(value = "source") String source, @ApiParam(name = "cardNo", value = "卡号", required = true) @RequestParam(value = "cardNo") String cardNo) {
        return bankCardHandler.cardNoInfo(userId, source, cardNo);
    }

    @Override
    public CommonResponse<BankCardRsDto> bindCardCheck(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "source", required = true) @RequestParam(value = "source") String source) {
        return bankCardHandler.bindCardCheck(userId, source);
    }

    @Override
    public CommonResponse<PreBindCardRsDto> preBindCard(@RequestBody PreBindCardDto preBindCardDto) {
        return bankCardHandler.preBindCard(preBindCardDto);
    }

    @Override
    public CommonResponse<ConfirmBindCardRsDto> confirmBindCard(@RequestBody ConfirmBindCardDto confirmBindCardDto) {
        return bankCardHandler.confirmBindCard(confirmBindCardDto);
    }

    @Override
    public CommonResponse delBindCard(@RequestBody DelBindCardDto delBindCardDto) {
        return bankCardHandler.delBindCard(delBindCardDto);
    }

    @Override
    public CommonResponse<List<BankCardRsDto>> getAllBindCards(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId, @ApiParam(name = "source", value = "source", required = true) @RequestParam(value = "source") String source) {
        return bankCardHandler.getAllBindCards(userId, source);
    }

    @Override
    public CommonResponse<List<BankCardsRsDto>> getBankCards() {
        return bankCardHandler.getBankCards();
    }

    @Override
    public CommonResponse<List<BankCardBaseRsDto>> bankCard() {
        return bankCardHandler.bankCard();
    }

    @Override
    public CommonResponse<UserInfoRsDto> getUserInfo(@ApiParam(name = "userId", value = "用户id", required = true) @RequestParam(value = "userId") Long userId) {
        return bankCardHandler.getUserInfo(userId);
    }

    @Override
    public CommonResponse queryBindCardList(@RequestParam(value = "userId") Long userId,
                                            @RequestParam(value = "cardNo") String cardNo,
                                            @RequestParam(value = "source") String source) {
        return bankCardHandler.queryBindCardList(userId, cardNo, source);
    }

    @Override
    public CommonResponse saveBankCard(@RequestBody BindCardDto bindCardDto) {
        return bankCardHandler.saveBankCard(bindCardDto);
    }
}
