package com.yj.gyl.bank.service.common.impl;

import com.yj.base.common.CommonResponse;
import com.yj.gyl.bank.dao.*;
import com.yj.gyl.bank.handler.common.PaymentType;
import com.yj.gyl.bank.model.*;
import com.yj.gyl.bank.rsdto.PayRecordMessRsDto;
import com.yj.gyl.bank.service.common.IPayService;
import com.yj.gyl.bank.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hepei
 * @date 2017/12/28 11:17:09
 */
@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    private TTradeRecordMapper tradeRecordMapper;
    @Autowired
    private TBankCardMapper bankCardMapper;
    @Autowired
    private TMerchantSourceMapper merchantSourceMapper;
    @Autowired
    private TBankCardMerchantMapper bankCardMerchantMapper;
    @Autowired
    private YeeB2cBankMapper b2cBankMapper;


    @Override
    public CommonResponse<PayRecordMessRsDto> showOrderMessByOrderNo(String orderNo) {
        CommonResponse<PayRecordMessRsDto> commonResponse = new CommonResponse<>();
        PayRecordMessRsDto messRsDto = new PayRecordMessRsDto();
        //查询订单记录
        TTradeRecordExample recordExample = new TTradeRecordExample();
        recordExample.or().andOrderNoEqualTo(orderNo);
        List<TTradeRecord> tradeRecord = tradeRecordMapper.selectByExample(recordExample);
        if (tradeRecord == null || tradeRecord.size() < 1) {
            commonResponse.setResult(1);
            commonResponse.setStatus(0);
            commonResponse.setMsg("未查询到相关订单记录");
            return commonResponse;
        }
        TTradeRecord record = tradeRecord.get(0);
        TBankCardMerchant bankCardMerchant = bankCardMerchantMapper.selectByPrimaryKey(record.getCardMerchantId());
        if (bankCardMerchant == null) {
            commonResponse.setResult(1);
            commonResponse.setStatus(0);
            commonResponse.setMsg("未查询到相关绑卡记录信息");
            return commonResponse;
        }
        TBankCard bankCard = bankCardMapper.selectByPrimaryKey(bankCardMerchant.getBankCardId());
        if (bankCard == null) {
            commonResponse.setResult(1);
            commonResponse.setStatus(0);
            commonResponse.setMsg("未查询到相关绑卡记录信息");
            return commonResponse;
        }

        messRsDto.setUserId(record.getUserId());
        messRsDto.setIdCardNo(SecretUtil.decrypt(bankCard.getIdCardNo()));
        messRsDto.setUserName(bankCard.getUserName());
        messRsDto.setPhone(SecretUtil.decrypt(bankCard.getPhone()));
        messRsDto.setBankName(bankCard.getBankName());
        messRsDto.setBankCode(bankCardMerchant.getBankCode());
        messRsDto.setBankBranch(bankCard.getBankBranch());
        messRsDto.setProvince(bankCard.getProvince());
        messRsDto.setCity(bankCard.getCity());
        messRsDto.setCardNo(SecretUtil.decrypt(bankCard.getCardNo()));
        messRsDto.setOrderNo(record.getOrderNo());
        messRsDto.setAmount(record.getAmount());
        messRsDto.setStatus(record.getStatus());
        messRsDto.setChannel(bankCardMerchant.getSource());
        messRsDto.setType(record.getType());

        commonResponse.setResult(0);
        commonResponse.setStatus(0);
        commonResponse.setData(messRsDto);
        commonResponse.setMsg("成功");
        return commonResponse;
    }

    @Override
    public PaymentType getChannelByChannel(String channel) {
        for (PaymentType type : PaymentType.values()) {
            if (type.getCode().equals(channel)) {
                return type;
            }
        }
        return null;
    }


    @Override
    public PaymentType getChannel(Long userId, String cardNo, String source) {
        TMerchantSourceExample sourceExample = new TMerchantSourceExample();
        sourceExample.or().andSourceEqualTo(source);
        List<TMerchantSource> merchantSourceList = merchantSourceMapper.selectByExample(sourceExample);
        if (merchantSourceList == null || merchantSourceList.size() < 1) {
            return null;
        }
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(userId).andCardNoEqualTo(SecretUtil.encrypt(cardNo));
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        TBankCard tBankCard = null;
        if (bankCardList != null && bankCardList.size() > 0) {
            tBankCard = bankCardList.get(0);
        }
        List<PaymentType> paymentTypes = new ArrayList<>();
        for (PaymentType type : PaymentType.values()) {
            paymentTypes.add(type);
        }
        if (tBankCard != null) {
            TBankCardMerchantExample example = new TBankCardMerchantExample();
            example.or().andBankCardIdEqualTo(tBankCard.getId()).andMerchantIdEqualTo(merchantSourceList.get(0).getMerchantId()).andStatusEqualTo(0);
            List<TBankCardMerchant> list = bankCardMerchantMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                for (PaymentType type : PaymentType.values()) {
                    for (TBankCardMerchant merchant : list) {
                        if (type.getCode().equals(merchant.getChannel())) {
                            paymentTypes.remove(type);
                            break;
                        }
                    }
                }
            }
        }
        if (paymentTypes.size() == 0) {
            return null;
        }
        return paymentTypes.get(0);
    }

    /**
     * 获取银行通道编码表
     *
     * @return
     */
    @Override
    public CommonResponse yeePayBankFrpList() {
        YeeB2cBankExample example = new YeeB2cBankExample();
        example.or().andStatusEqualTo(0);
        List<YeeB2cBank> list = b2cBankMapper.selectByExample(example);
        List<Map<String, String>> result = new ArrayList<>(16);
        if (list != null && list.size() > 0) {
            for (YeeB2cBank b2cBank : list) {
                Map<String, String> map = new HashMap<>(2);
                map.put("code", b2cBank.getFrpCode().trim());
                map.put("name", b2cBank.getBankName().trim());
                result.add(map);
            }
        }
        return CommonResponse.builder().msg("成功").data(result).build();
    }

}
