package com.yj.gyl.bank.service.common;


import com.yj.gyl.bank.model.TBankCardMerchant;

import java.util.List;

/**
 * @author hepei
 * @date 2017/12/29 15:34:57
 */
public interface IBankCardMerchantService {
    /**
     * 获取交易通道
     *
     * @param bankCardId 银行卡id
     * @param merchantId 商户账户id
     * @return
     */
    TBankCardMerchant getMerchant(Long bankCardId, Long merchantId, String source);

    /**
     * 根据 id 获取TBankCardMerchant
     *
     * @param id
     * @return
     */
    TBankCardMerchant getBankCardMerchantById(Long id);

    TBankCardMerchant getBankCardMerchantByBankCardId(Long bankCardId, String source);

    /**
     * 根据用户id以及商户账户id查询该商户号下改用户是否绑卡
     *
     * @param userId
     * @param merchantId
     * @return
     */
    List<TBankCardMerchant> getBankCardMerchant(Long userId, Long merchantId, String source);

    TBankCardMerchant getPreBankCardMerchant(Long userId, Long merchantId);

    /**
     * 删除绑卡
     *
     * @param bankId
     * @param merchantId
     */
    void updateBankCardAndMerchant(Long bankId, Long merchantId);

    List<TBankCardMerchant> getMerchantList(Long bankCardId, Long merchantId);
}
