package com.yj.gyl.bank.service.common.impl;

import com.yj.gyl.bank.dao.TBankCardMapper;
import com.yj.gyl.bank.dao.TBankCardMerchantMapper;
import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.model.TBankCardExample;
import com.yj.gyl.bank.model.TBankCardMerchant;
import com.yj.gyl.bank.model.TBankCardMerchantExample;
import com.yj.gyl.bank.rsdto.ConfirmBindCardRsDto;
import com.yj.gyl.bank.rsdto.PreBindCardRsDto;
import com.yj.gyl.bank.service.common.IBankCardService;
import com.yj.gyl.bank.service.yibao.util.ReflectHelper;
import com.yj.gyl.bank.util.SecretUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by renfei on 2017/12/18.
 */
@Service
@Transactional
public class BankCardServiceImpl implements IBankCardService {
    @Autowired
    private TBankCardMapper bankCardMapper;
    @Autowired
    private TBankCardMerchantMapper bankCardMerchantMapper;

    @Override
    public TBankCard checkUserCard(Long userId, boolean flag) {
        TBankCardExample bankCardExample = new TBankCardExample();
        if (flag) {
            bankCardExample.or().andUserIdEqualTo(userId).andStatusEqualTo(0);//正常的绑卡
        } else {
            bankCardExample.or().andUserIdEqualTo(userId).andStatusNotEqualTo(-1);
        }
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() > 0) {
            return bankCardList.get(0);
        }
        return null;
    }

    @Override
    public TBankCard checkCardNo(long userid, String cardNo) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(userid).andCardNoEqualTo(SecretUtil.encrypt(cardNo));
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() > 0) {
            return bankCardList.get(0);
        }
        return null;
    }

    /**
     * 查询是否绑定了其他卡号
     *
     * @param userId
     * @param cardNo
     * @return
     */
    @Override
    public boolean checkBindOtherCard(long userId, String cardNo) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(userId).andCardNoNotEqualTo(cardNo).andStatusEqualTo(0);
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public void saveBankCard(PreBindCardRsDto preBindCardDTO) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(preBindCardDTO.getUserId()).andCardNoEqualTo(SecretUtil.encrypt(preBindCardDTO.getCardNo().trim()));
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() == 0) {
            TBankCard record = new TBankCard();
            ReflectHelper.getTargetObject(preBindCardDTO, record);
            record.setCardNo(SecretUtil.encrypt(preBindCardDTO.getCardNo().trim()));
            record.setIdCardNo(SecretUtil.encrypt(preBindCardDTO.getIdCardNo()));
            record.setUserName(SecretUtil.encrypt(preBindCardDTO.getUserName()));
            record.setPhone(SecretUtil.encrypt(preBindCardDTO.getPhone()));
            if(preBindCardDTO.getCreateTime() > 0){
                record.setCreateTime(new Date(preBindCardDTO.getCreateTime()));
            }else {
                record.setCreateTime(new Date());
            }
            record.setStatus(-1);
            bankCardMapper.insertSelective(record);
        }
    }

    @Override
    public TBankCard saveBankCardMerchant(ConfirmBindCardRsDto confirmBindCardDTO, Long merchantId) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(confirmBindCardDTO.getUserId()).andCardNoEqualTo(SecretUtil.encrypt(confirmBindCardDTO.getCardNo().trim()));
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() > 0) {
            TBankCardMerchantExample example = new TBankCardMerchantExample();
            example.or().andBankCardIdEqualTo(bankCardList.get(0).getId()).andMerchantIdEqualTo(merchantId).andSourceEqualTo(confirmBindCardDTO.getSource()).andUserIdEqualTo(confirmBindCardDTO.getUserId()).andStatusEqualTo(0);
            long count = bankCardMerchantMapper.countByExample(example);
            if (count > 0) {
                return bankCardList.get(0);
            }
            TBankCardMerchant record = new TBankCardMerchant();
            record.setBankCode(confirmBindCardDTO.getBankCode());
            if(confirmBindCardDTO.getCreateTime() > 0){
                record.setCreateTime(new Date(confirmBindCardDTO.getCreateTime()));
            }else {
                record.setCreateTime(new Date());
            }
            record.setBankCardId(bankCardList.get(0).getId());
            record.setChannel(confirmBindCardDTO.getChannel());
            record.setUserId(confirmBindCardDTO.getUserId());
            record.setSource(confirmBindCardDTO.getSource());
            record.setMerchantId(merchantId);
            bankCardMerchantMapper.insertSelective(record);

            if (bankCardList.get(0).getStatus() != 0) {
                TBankCard bankCard = new TBankCard();
                bankCard.setId(bankCardList.get(0).getId());
                bankCard.setStatus(0);
                bankCardMapper.updateByPrimaryKeySelective(bankCard);
            }
            return bankCardList.get(0);
        }
        return null;
    }

    @Override
    public TBankCard getUserBankCard(long userid, String cardNo) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(userid).andCardNoEqualTo(SecretUtil.encrypt(cardNo)).andStatusEqualTo(0);
        List<TBankCard> bankCardList = bankCardMapper.selectByExample(bankCardExample);
        if (bankCardList != null && bankCardList.size() > 0) {
            return bankCardList.get(0);
        }
        return null;
    }

    @Override
    public List<TBankCard> getAllBindCards(Long userId) {
        TBankCardExample bankCardExample = new TBankCardExample();
        bankCardExample.or().andUserIdEqualTo(userId).andStatusNotEqualTo(-1);
        return bankCardMapper.selectByExample(bankCardExample);
    }

    @Override
    public TBankCard getUserBankCardById(long bankCardId) {
        return bankCardMapper.selectByPrimaryKey(bankCardId);
    }

}
