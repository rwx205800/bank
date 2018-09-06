package com.yj.gyl.bank.handler;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dto.BindCardDto;
import com.yj.gyl.bank.dto.ConfirmBindCardDto;
import com.yj.gyl.bank.dto.DelBindCardDto;
import com.yj.gyl.bank.dto.PreBindCardDto;
import com.yj.gyl.bank.rsdto.*;

import java.util.List;

/**
 * @author hepei
 * @date 2017/12/13 19:04:32
 */
public interface IBankCardHandler {

    /**
     * 绑卡(发送短信)
     *
     * @param preBindCardDto
     * @return
     */
    CommonResponse<PreBindCardRsDto> preBindCard(PreBindCardDto preBindCardDto);

    /**
     * 确认绑卡
     *
     * @param confirmBindCardDto
     * @return
     */
    CommonResponse<ConfirmBindCardRsDto> confirmBindCard(ConfirmBindCardDto confirmBindCardDto);

    /**
     * 获取绑卡信息
     *
     * @param userId
     * @return
     */
    CommonResponse<List<BankCardRsDto>> getAllBindCards(Long userId, String source);

    CommonResponse<List<BankCardsRsDto>> getBankCards();

    CommonResponse<List<BankCardBaseRsDto>> bankCard();

    /**
     * 解绑
     *
     * @param delBindCardDto
     * @return
     */
    CommonResponse delBindCard(DelBindCardDto delBindCardDto);

    /**
     * 是否首次绑卡
     *
     * @param userId
     * @return
     */
    CommonResponse<BankCardRsDto> bindCardCheck(Long userId, String source);

    CommonResponse cardNoCheck(Long userId, String source, String cardNo);

    CommonResponse<BankCardRsDto> cardNoInfo(Long userId, String source, String cardNo);

    CommonResponse<UserInfoRsDto> getUserInfo(Long userId);

    /**
     * 查询支付渠道的绑卡列表
     *
     * @param userId 用户id
     * @param cardNo 银行卡号
     * @param source 交易来源（appid）
     * @return
     */
    CommonResponse queryBindCardList(Long userId, String cardNo, String source);

    CommonResponse saveBankCard(BindCardDto bindCardDto);
}
