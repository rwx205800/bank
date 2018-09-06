package com.yj.gyl.bank.service.common;


import com.yj.gyl.bank.model.TBankCard;
import com.yj.gyl.bank.rsdto.ConfirmBindCardRsDto;
import com.yj.gyl.bank.rsdto.PreBindCardRsDto;

import java.util.List;

/**
 * Created by renfei on 2017/12/18.
 */
public interface IBankCardService {

    TBankCard checkUserCard(Long userId, boolean flag);

    TBankCard checkCardNo(long userId, String cardNo);

    boolean checkBindOtherCard(long userId, String cardNo);

    void saveBankCard(PreBindCardRsDto preBindCardDTO);

    TBankCard saveBankCardMerchant(ConfirmBindCardRsDto confirmBindCardDTO, Long merchantId);

    TBankCard getUserBankCard(long userId, String cardNo);

    List<TBankCard> getAllBindCards(Long userId);

    TBankCard getUserBankCardById(long bankCardId);

}
