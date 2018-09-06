package com.yj.gyl.bank.service.common.impl;

import com.yj.gyl.bank.dao.TBankCardMapper;
import com.yj.gyl.bank.dao.TBankCardMerchantMapper;
import com.yj.gyl.bank.model.TBankCardMerchant;
import com.yj.gyl.bank.model.TBankCardMerchantExample;
import com.yj.gyl.bank.service.common.IBankCardMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hepei
 * @date 2017/12/29 15:35:47
 */
@Service
public class BankCardMerchantServiceImpl implements IBankCardMerchantService {

    @Autowired
    private TBankCardMapper bankCardMapper;
    @Autowired
    private TBankCardMerchantMapper bankCardMerchantMapper;

    @Override
    public TBankCardMerchant getBankCardMerchantByBankCardId(Long bankCardId,String source) {
        TBankCardMerchantExample example = new TBankCardMerchantExample();
        example.or().andBankCardIdEqualTo(bankCardId).andSourceEqualTo(source).andStatusEqualTo(0);
        List<TBankCardMerchant> list = bankCardMerchantMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<TBankCardMerchant> getBankCardMerchant(Long userId, Long merchantId,String source) {
        TBankCardMerchantExample example = new TBankCardMerchantExample();
        example.or().andMerchantIdEqualTo(merchantId).andUserIdEqualTo(userId).andSourceEqualTo(source).andStatusEqualTo(0);
        return bankCardMerchantMapper.selectByExample(example);
    }

    @Override
    public TBankCardMerchant getPreBankCardMerchant(Long userId, Long merchantId) {
        TBankCardMerchantExample example = new TBankCardMerchantExample();
        example.or().andMerchantIdEqualTo(merchantId).andUserIdEqualTo(userId);
        List<TBankCardMerchant> list = bankCardMerchantMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public TBankCardMerchant getBankCardMerchantById(Long id) {
        return bankCardMerchantMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateBankCardAndMerchant(Long bankId, Long merchantId) {
//        TBankCard bankCard = new TBankCard();
//        bankCard.setId(bankId);
//        bankCard.setStatus(1);
//        bankCardMapper.updateByPrimaryKeySelective(bankCard);

        TBankCardMerchant merchant = new TBankCardMerchant();
        merchant.setId(merchantId);
        merchant.setStatus(1);
        bankCardMerchantMapper.updateByPrimaryKeySelective(merchant);
    }

    @Override
    public TBankCardMerchant getMerchant(Long bankCardId, Long merchantId,String source) {
        TBankCardMerchantExample bankCardMerchantExample = new TBankCardMerchantExample();
        bankCardMerchantExample.or().andBankCardIdEqualTo(bankCardId).andMerchantIdEqualTo(merchantId).andSourceEqualTo(source).andStatusEqualTo(0);
        List<TBankCardMerchant> bankCardMerchantList = bankCardMerchantMapper.selectByExample(bankCardMerchantExample);
        if (bankCardMerchantList == null || bankCardMerchantList.size() < 1) {
            return null;
        }
        return bankCardMerchantList.get(0);
    }

    @Override
    public List<TBankCardMerchant> getMerchantList(Long bankCardId, Long merchantId) {
        TBankCardMerchantExample bankCardMerchantExample = new TBankCardMerchantExample();
        bankCardMerchantExample.or().andBankCardIdEqualTo(bankCardId).andMerchantIdEqualTo(merchantId).andStatusEqualTo(0);
        return bankCardMerchantMapper.selectByExample(bankCardMerchantExample);
    }

}
