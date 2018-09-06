package com.yj.gyl.bank.service.common.impl;

import com.yj.gyl.bank.dao.TBankCardBaseMapper;
import com.yj.gyl.bank.model.TBankCardBase;
import com.yj.gyl.bank.model.TBankCardBaseExample;
import com.yj.gyl.bank.service.common.IBankCardBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by renfei on 2018/3/1.
 */
@Service
public class BankCardBaseServiceImpl implements IBankCardBaseService {
    @Autowired
    private TBankCardBaseMapper bankCardBaseMapper;

    @Override
    public List<TBankCardBase> getBankCardBase(Long id) {
        TBankCardBaseExample example = new TBankCardBaseExample();
        example.or().andMerchantIdEqualTo(id).andDelDegEqualTo(0);
        return bankCardBaseMapper.selectByExample(example);
    }

    @Override
    public TBankCardBase getBankCardBase(Long id, String code) {
        TBankCardBaseExample example = new TBankCardBaseExample();
        example.or().andMerchantIdEqualTo(id).andDelDegEqualTo(0).andCodeEqualTo(code);
        List<TBankCardBase> list = bankCardBaseMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
