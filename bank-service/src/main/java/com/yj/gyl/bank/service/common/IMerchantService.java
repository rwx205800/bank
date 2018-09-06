package com.yj.gyl.bank.service.common;


import com.yj.gyl.bank.model.TMerchant;

/**
 * @author hepei
 * @date 2017/12/29 15:35:18
 */
public interface IMerchantService {

    /**
     * 根据交易来源查询交易通道账户信息
     *
     * @param source 交易来源
     * @return
     */
    TMerchant getMerchantBySource(String source);

    TMerchant getMerchantBySource(String source, String channel);

    /**
     * 根据id查询交易通道账户信息
     *
     * @param id
     * @return
     */
    TMerchant getMerchantById(Long id);

}
