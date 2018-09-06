package com.yj.gyl.bank.service.common;

import com.yj.gyl.bank.model.TBankCardBase;

import java.util.List;

/**
 * Created by renfei on 2018/3/1.
 */
public interface IBankCardBaseService {

    List<TBankCardBase> getBankCardBase(Long id);

    TBankCardBase getBankCardBase(Long id, String code);

}
