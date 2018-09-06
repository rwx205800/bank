package com.yj.gyl.bank.handler.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yj.base.common.CommonResponse;
import com.yj.base.common.ConstantEnum;
import com.yj.user.api.IUserApi;
import com.yj.user.dto.AppHeader;
import com.yj.user.rsdto.UserRs;
import com.yj.user.util.RequestUtil;
import com.yj.gyl.bank.dto.BindCardDto;
import com.yj.gyl.bank.dto.ConfirmBindCardDto;
import com.yj.gyl.bank.dto.DelBindCardDto;
import com.yj.gyl.bank.dto.PreBindCardDto;
import com.yj.gyl.bank.handler.IBankCardHandler;
import com.yj.gyl.bank.handler.common.PaymentType;
import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TBankCardBase;
import com.yj.gyl.bank.model.TBankCardMerchant;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.rsdto.*;
import com.yj.gyl.bank.service.IPayService;
import com.yj.gyl.bank.service.PayClientServiceFactor;
import com.yj.gyl.bank.service.common.IBankCardBaseService;
import com.yj.gyl.bank.service.common.IBankCardMerchantService;
import com.yj.gyl.bank.service.common.IBankCardService;
import com.yj.gyl.bank.service.common.IMerchantService;
import com.yj.gyl.bank.util.SecretUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author hepei
 * @date 2017/12/13 19:05:57
 */
@Component
public class BankCardHandlerImpl implements IBankCardHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IBankCardMerchantService bankCardMerchantService;

    @Autowired
    private IBankCardService bankCardService;

    @Autowired
    private IMerchantService merchantService;

    @Autowired
    private com.yj.gyl.bank.service.common.IPayService payService;

    @Autowired
    private IUserApi userApi;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private IBankCardBaseService bankCardBaseService;

    @Override
    public CommonResponse<PreBindCardRsDto> preBindCard(PreBindCardDto preBindCardDto) {
        logger.info("【预绑卡入参：{}】", JSONObject.toJSONString(preBindCardDto));
        if (StringUtils.isEmpty(preBindCardDto.getCardNo())) {
            logger.error("【预绑卡--银行卡号为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_CARDNO_EMPTY);
        }
        if (StringUtils.isEmpty(preBindCardDto.getUserId())) {
            logger.error("【预绑卡--用户id为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_USERID_EMPTY);
        }
        if (StringUtils.isEmpty(preBindCardDto.getPhone())) {
            logger.error("【预绑卡--手机号为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(preBindCardDto.getSource())) {
            logger.error("【预绑卡--交易来源为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SOURCE_EMPTY);
        }
        if (StringUtils.isEmpty(preBindCardDto.getIdCardNo())) {
            logger.error("【预绑卡--身份证号码为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_IDCARDNO_EMPTY);
        }
        if (StringUtils.isEmpty(preBindCardDto.getUserName())) {
            logger.error("【预绑卡--真实姓名为空】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_USERNAME_EMPTY);
        }
        TMerchant merchant = merchantService.getMerchantBySource(preBindCardDto.getSource());//确定是哪个交易通道
        if (merchant == null) {
            logger.error("【预绑卡--商户号未配置】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_MERCHANT_SOURCE_NOT_SETTING);
        }
        TBankCard bankCard = bankCardService.checkCardNo(preBindCardDto.getUserId(), preBindCardDto.getCardNo());
        if (bankCard != null) {
            //根据用户id 以及merchantId查询是否绑过卡
            TBankCardMerchant tbankCardMerchant = bankCardMerchantService.getMerchant(bankCard.getId(), merchant.getId(), preBindCardDto.getSource());
            if (tbankCardMerchant != null) {
                logger.error("【预绑卡--该卡已绑定】");
                return CommonResponse.build(TransactionPayEnum.YB_PAY_CARD_USED);
            }
        }
        TBankCard tBankCard = bankCardService.checkUserCard(preBindCardDto.getUserId(), false);//获取之前绑卡成功过的卡
        if (tBankCard != null) {
            String idCardNo = SecretUtil.decrypt(tBankCard.getIdCardNo());//身份证
            if (!idCardNo.equals(preBindCardDto.getIdCardNo())) {
                logger.error("【预绑卡--校验银东用户实名信息有误】");
                return CommonResponse.build(TransactionPayEnum.YB_PAY_USER_INFO_IDENTITY);
            }
        }
        PaymentType paymentType = payService.getChannelByChannel(merchant.getChannel());
        if (paymentType == null) {
            logger.info("【预绑卡--交易通道错误】");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_PREBINDCARD_CHANNEL_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse<PreBindCardRsDto> commonResponse = payClient.preBindCard(preBindCardDto, merchant);
        logger.info("【预绑卡--预绑卡结果】------:" + JSONObject.toJSONString(commonResponse));
        if (commonResponse != null && commonResponse.getResult() == 0 && commonResponse.getStatus() == 0) { //绑卡成功 渠道参数
            PreBindCardRsDto preBindCardDTO = commonResponse.getData();
            preBindCardDTO.setChannel(paymentType.getCode());
            commonResponse.setData(preBindCardDTO);
            bankCardService.saveBankCard(preBindCardDTO);
        }
        return commonResponse;
    }

    @Override
    public CommonResponse<ConfirmBindCardRsDto> confirmBindCard(ConfirmBindCardDto confirmBindCardDto) {
        logger.info("confirmBindCard:" + JSONObject.toJSONString(confirmBindCardDto));
        if (StringUtils.isEmpty(confirmBindCardDto.getValidateCode())) {
            logger.info("用户收到短信验证码为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_VALIDATECODE);
        }
        if (StringUtils.isEmpty(confirmBindCardDto.getCardNo())) {
            logger.info("请填写卡号");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_CARD_EMPTY);
        }
        if (StringUtils.isEmpty(confirmBindCardDto.getChannel())) {
            logger.info("请填写交易通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_CHANNEL_EMPTY);
        }
        if (StringUtils.isEmpty(confirmBindCardDto.getUserId())) {
            logger.info("请填写用户ID");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_USERID_EMPTY);
        }
        if (StringUtils.isEmpty(confirmBindCardDto.getOrderNo())) {
            logger.info("请填写订单号");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_ORDERNO_EMPTY);
        }
        if (StringUtils.isEmpty(confirmBindCardDto.getSource())) {
            logger.info("交易来源为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_SOURCE_EMPTY);
        }
        PaymentType paymentType = payService.getChannelByChannel(confirmBindCardDto.getChannel());
        if (paymentType == null) {
            logger.info("交易通道错误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_CHANNEL_ERROR);
        }
        TMerchant merchant = merchantService.getMerchantBySource(confirmBindCardDto.getSource(), confirmBindCardDto.getChannel());
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_MERCHANT_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse<ConfirmBindCardRsDto> commonResponse = payClient.confirmBindCard(confirmBindCardDto, merchant);
        logger.info("[确认绑卡response]" + JSONObject.toJSONString(commonResponse.getData()));
        if (commonResponse != null && commonResponse.getResult() == 0) {
            ConfirmBindCardRsDto confirmBindCardDTO = commonResponse.getData();
            TBankCard tBankCard = bankCardService.saveBankCardMerchant(confirmBindCardDTO, merchant.getId());
            confirmBindCardDTO.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
            confirmBindCardDTO.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
            confirmBindCardDTO.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));
            commonResponse.setData(confirmBindCardDTO);
        }

        return commonResponse;
    }

    @Override
    public CommonResponse<List<BankCardRsDto>> getAllBindCards(Long userId, String source) {
        if (StringUtils.isEmpty(source)) {
            logger.info("来源为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_GET_BIND_CARD_SOURCE_EMPYT);
        }
        List<BankCardRsDto> bankCardList = new ArrayList<>();
        TMerchant merchant = merchantService.getMerchantBySource(source);//确定是哪个交易通道
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_GET_BIND_CARD_SETTING);
        }
        List<TBankCardMerchant> list = bankCardMerchantService.getBankCardMerchant(userId, merchant.getId(), source);
        if (list != null && list.size() > 0) {
            for (TBankCardMerchant bankCardMerchant : list) {
                BankCardRsDto rsDto = new BankCardRsDto();
                TBankCard tBankCard = bankCardService.getUserBankCardById(bankCardMerchant.getBankCardId());
                BeanUtils.copyProperties(tBankCard, rsDto);
                rsDto.setBankCode(bankCardMerchant.getBankCode());
                rsDto.setCardNo(SecretUtil.decrypt(tBankCard.getCardNo()));
                rsDto.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
                rsDto.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));
                rsDto.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
                if (!StringUtils.isEmpty(bankCardMerchant.getBankCode())) {
                    TBankCardBase tBankCardBase = bankCardBaseService.getBankCardBase(merchant.getId(), bankCardMerchant.getBankCode());
                    if (tBankCardBase != null) {
                        rsDto.setDescription(tBankCardBase.getDescription());
                        rsDto.setCardImg(tBankCardBase.getImgUrl());
                    }
                }
                bankCardList.add(rsDto);
            }
        }
        return CommonResponse.buildWithData(TransactionPayEnum.GLOBAL_SUCCESS, bankCardList);
    }

    @Override
    public CommonResponse<List<BankCardsRsDto>> getBankCards() {
        logger.info("【获取银行卡列表:{}】");
        UserRs userRs = userApi.getCurUser();
        if (userRs == null || userRs.getStatus() != 0) {
            logger.error("【获取银行卡列表:{}】", "用户未登录");
            return CommonResponse.build(ConstantEnum.GLOBAL_NO_LOGIN);
        }
        AppHeader appHeader = RequestUtil.getAppHeader(this.request);
        String source = String.valueOf(appHeader.getAppid());
        List<TBankCard> list = bankCardService.getAllBindCards(userRs.getUser().getId());
        List<BankCardsRsDto> cardsRsDtos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (TBankCard tBankCard : list) {
                BankCardsRsDto rsDto = new BankCardsRsDto();
                BeanUtils.copyProperties(tBankCard, rsDto);
                rsDto.setCardNo(SecretUtil.decrypt(tBankCard.getCardNo()));
                rsDto.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
                rsDto.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));
                rsDto.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
                TBankCardMerchant merchant = bankCardMerchantService.getBankCardMerchantByBankCardId(tBankCard.getId(), source);
                if (merchant == null) {
                    rsDto.setBind(false);
                    cardsRsDtos.add(rsDto);
                }
//                else {
//                    rsDto.setBind(true);
//                    rsDto.setBankCode(merchant.getBankCode());
//                }
//                cardsRsDtos.add(rsDto);
            }
        }
        return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, cardsRsDtos);
    }

    @Override
    public CommonResponse<List<BankCardBaseRsDto>> bankCard() {
        logger.info("【获取银行卡列表:{}】");
        AppHeader appHeader = RequestUtil.getAppHeader(this.request);
        String source = String.valueOf(appHeader.getAppid());
        TMerchant merchant = merchantService.getMerchantBySource(source);//确定是哪个交易通道
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_GET_BIND_CARD_SETTING);
        }
        List<TBankCardBase> list = bankCardBaseService.getBankCardBase(merchant.getId());
        List<BankCardBaseRsDto> cardsRsDtos = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (TBankCardBase tBankCardBase : list) {
                BankCardBaseRsDto rsDto = new BankCardBaseRsDto();
                BeanUtils.copyProperties(tBankCardBase, rsDto);
                cardsRsDtos.add(rsDto);
            }
        }
        return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, cardsRsDtos);
    }

    @Override
    public CommonResponse delBindCard(DelBindCardDto delBindCardDto) {
        logger.info("[易宝绑卡删除]------：" + JSONObject.toJSONString(delBindCardDto));
        if (StringUtils.isEmpty(delBindCardDto.getCardNo())) {
            logger.info("请填写卡号");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_UNBINDCARD_CARD_EMPTY);
        }
        if (StringUtils.isEmpty(delBindCardDto.getUserId())) {
            logger.info("请填写用户ID");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_UNBINDCARD_USERID_EMPTY);
        }
        TBankCard tBankCard = bankCardService.getUserBankCard(delBindCardDto.getUserId(), delBindCardDto.getCardNo());
        if (tBankCard == null) {
            logger.info("银行卡信息有误");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_UNBINDCARD_CARD_ERROR);
        }
        TMerchant tMerchant = merchantService.getMerchantBySource(delBindCardDto.getSource());
        //根据路由筛选解绑卡渠道  注意：由于是单渠道，支持，如果是多渠道需要做调整
        TBankCardMerchant bankCardMerchant = bankCardMerchantService.getMerchant(tBankCard.getId(), tMerchant.getId(), delBindCardDto.getSource());
        if (bankCardMerchant == null) {
            logger.info("该银行卡未绑定");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_UNBINDCARD_CARD_NOT_BIND);
        }
        bankCardMerchantService.updateBankCardAndMerchant(tBankCard.getId(), bankCardMerchant.getId());
        return CommonResponse.builder().result(0).status(0).msg("删除成功").build();
//        PaymentType paymentType = payService.getChannelByChannel(bankCardMerchant.getChannel());
//        if (paymentType == null) {
//            logger.info("渠道不存在");
//            return CommonResponse.build(TransactionPayEnum.YB_PAY_UNBINDCARD_SOURCE_ERROR);
//        }
//        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
//        tBankCard.setCardNo(SecretUtil.decrypt(tBankCard.getCardNo()));
//        CommonResponse commonResponse = payClient.delBindCard(tBankCard, tMerchant);
//        if (commonResponse.getResult() == 0) {
//            List<TBankCardMerchant> list = bankCardMerchantService.getMerchantList(tBankCard.getId(), tMerchant.getId());
//            for (TBankCardMerchant merchant : list) {
//                bankCardMerchantService.updateBankCardAndMerchant(tBankCard.getId(), merchant.getId());
//            }
//        }
//        return commonResponse;
    }

    @Override
    public CommonResponse<BankCardRsDto> bindCardCheck(Long userId, String source) {
        if (StringUtils.isEmpty(source)) {
            logger.info("来源为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BIND_CARD_SOURCE_EMPTY);
        }
        TMerchant merchant = merchantService.getMerchantBySource(source);//确定是哪个交易通道
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BIND_CARD_SETTING);
        }
        //根据用户id 以及merchantId查询是否绑过卡
        TBankCardMerchant bankCardMerchant = bankCardMerchantService.getPreBankCardMerchant(userId, merchant.getId());
        if (bankCardMerchant != null) {
            TBankCard tBankCard = bankCardService.getUserBankCardById(bankCardMerchant.getBankCardId());
            BankCardRsDto rsDto = new BankCardRsDto();
            BeanUtils.copyProperties(tBankCard, rsDto);
            rsDto.setBankCode(bankCardMerchant.getBankCode());
            rsDto.setCardNo(SecretUtil.decrypt(tBankCard.getCardNo()));
            rsDto.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
            rsDto.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));
            rsDto.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
            return CommonResponse.buildWithData(TransactionPayEnum.YB_PAY_BIND_CARD, rsDto);
        } else {
            return CommonResponse.build(TransactionPayEnum.YB_PAY_NOT_BIND_CARD);
        }
    }

    @Override
    public CommonResponse cardNoCheck(Long userId, String source, String cardNo) {
        logger.info("cardNoCheck:");
        if (StringUtils.isEmpty(cardNo)) {
            logger.info("银行卡号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_CARDNO_EMPTY);
        }
        if (userId == null) {
            logger.info("用户id为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_USERID_EMPTY);
        }
        if (StringUtils.isEmpty(source)) {
            logger.info("交易来源为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SOURCE_EMPTY);
        }
        TMerchant merchant = merchantService.getMerchantBySource(source);//确定是哪个交易通道
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_MERCHANT_SOURCE_NOT_SETTING);
        }
        TBankCard bankCard = bankCardService.checkCardNo(userId, cardNo);
        if (bankCard != null) {
            //根据用户id 以及merchantId查询是否绑过卡
            TBankCardMerchant tbankCardMerchant = bankCardMerchantService.getMerchant(bankCard.getId(), merchant.getId(), source);
            if (tbankCardMerchant != null) {
                logger.info("该卡已绑定");
                return CommonResponse.build(TransactionPayEnum.YB_PAY_CARD_USED);
            }
        }
        return CommonResponse.build(ConstantEnum.GLOBAL_SUCCESS);
    }

    @Override
    public CommonResponse cardNoInfo(Long userId, String source, String cardNo) {
        logger.info("【银行卡信息】:");
        if (StringUtils.isEmpty(cardNo)) {
            logger.info("银行卡号为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_CARDNO_EMPTY);
        }
        if (userId == null) {
            logger.info("用户id为空");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_USERID_EMPTY);
        }
        TBankCard tBankCard = bankCardService.getUserBankCard(userId, cardNo);
        if (tBankCard == null) {
            return CommonResponse.builder().result(1).status(1).msg("该卡未绑定").build();
        }
        BankCardRsDto rsDto = new BankCardRsDto();
        BeanUtils.copyProperties(tBankCard, rsDto);
        rsDto.setCardNo(SecretUtil.decrypt(tBankCard.getCardNo()));
        rsDto.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
        rsDto.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));
        rsDto.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
        return CommonResponse.buildWithData(ConstantEnum.GLOBAL_SUCCESS, rsDto);
    }

    @Override
    public CommonResponse<UserInfoRsDto> getUserInfo(Long userId) {
        if (StringUtils.isEmpty(userId)) {
            logger.info("请填写用户ID");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_GET_USER_INFO_USERID_EMPYT);
        }
        UserInfoRsDto rsDto = new UserInfoRsDto();
        TBankCard tBankCard = bankCardService.checkUserCard(userId, true);
        if (tBankCard != null) {
            rsDto.setUserId(userId);
            rsDto.setPhone(SecretUtil.decrypt(tBankCard.getPhone()));
            rsDto.setIdCardNo(SecretUtil.decrypt(tBankCard.getIdCardNo()));
            rsDto.setUserName(SecretUtil.decrypt(tBankCard.getUserName()));

        } else {
            logger.info("无有效实名信息");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_GET_USER_INFO_ERROR);
        }
        return CommonResponse.buildWithData(TransactionPayEnum.GLOBAL_SUCCESS, rsDto);
    }

    @Override
    public CommonResponse queryBindCardList(Long userId, String cardNo, String source) {
        if (userId == null || !(userId.compareTo(0L) > 0)) {
            logger.info("请填写用户ID");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_USERID_EMPTY);
        }
        if (!StringUtils.hasText(cardNo)) {
            logger.info("请填写银行卡号");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_CARDNO_EMPTY);
        }
        if (!StringUtils.hasText(source)) {
            logger.info("请填写交易来源（appId）");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_SOURCE_EMPTY);
        }

        //获取交易来源的交易通道账户号
        TMerchant tMerchant = merchantService.getMerchantBySource(source);
        if (tMerchant == null) {
            logger.info("支付通道信息不存在");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_MERCHANT_SOURCE_NOT_SETTING);
        }
        //查询支付通道
        PaymentType paymentType = payService.getChannelByChannel(tMerchant.getChannel());
        if (paymentType == null) {
            logger.info("未查询到相关支付通道");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_PREBINDCARD_CHANNEL_ERROR);
        }
        IPayService payClient = PayClientServiceFactor.CreatePayClient(paymentType);
        CommonResponse commonResponse = payClient.queryBindCardList(userId, cardNo, tMerchant);
        if (commonResponse.getResult() == 0 && commonResponse.getData() != null) {
            Map<String, String> result = (Map<String, String>) commonResponse.getData();
            //有绑卡记录
            if (StringUtils.hasText(result.get("cardlist"))) {
                JSONArray jsonArray = JSONObject.parseArray(result.get("cardlist"));
                String value = jsonArray.getJSONObject(0).getString("bankcode") + "|" + tMerchant.getChannel();
                return CommonResponse.builder().result(-1).status(-1).msg("已有绑卡记录").data(value).build();
            }
        }
        //没有绑卡记录
        if ("绑卡关系不存在".equals(commonResponse.getMsg())) {
            return CommonResponse.builder().result(0).status(0).msg("没有绑卡记录").build();
        }
        return commonResponse;
    }

    @Override
    public CommonResponse saveBankCard(BindCardDto bindCardDto) {
        logger.info("saveBankCard：");
        TMerchant merchant = merchantService.getMerchantBySource(bindCardDto.getSource(), bindCardDto.getChannel());
        if (merchant == null) {
            logger.info("商户号未配置");
            return CommonResponse.build(TransactionPayEnum.YB_PAY_BINDCARD_MERCHANT_ERROR);
        }
        PreBindCardRsDto preBindCardDTO = new PreBindCardRsDto();
        BeanUtils.copyProperties(bindCardDto, preBindCardDTO);
        bankCardService.saveBankCard(preBindCardDTO);

        ConfirmBindCardRsDto confirmBindCardDTO = new ConfirmBindCardRsDto();
        BeanUtils.copyProperties(bindCardDto, confirmBindCardDTO);
        bankCardService.saveBankCardMerchant(confirmBindCardDTO, merchant.getId());

        return CommonResponse.build(TransactionPayEnum.GLOBAL_SUCCESS);
    }
}
