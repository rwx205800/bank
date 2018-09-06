package com.yj.gyl.bank.service.common.impl;

import com.yj.gyl.bank.dao.TMerchantMapper;
import com.yj.gyl.bank.dao.TMerchantSourceMapper;
import com.yj.gyl.bank.model.TMerchant;
import com.yj.gyl.bank.model.TMerchantSource;
import com.yj.gyl.bank.model.TMerchantSourceExample;
import com.yj.gyl.bank.service.common.IMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by renfei on 2017/12/29.
 */
@Service
@Transactional
public class MerchantServiceImpl implements IMerchantService {
    @Autowired
    private TMerchantSourceMapper merchantSourceMapper;

    @Autowired
    private TMerchantMapper merchantMapper;

    @Override
    public TMerchant getMerchantBySource(String source) {
        TMerchantSourceExample sourceExample = new TMerchantSourceExample();
        sourceExample.or().andSourceEqualTo(source);
        List<TMerchantSource> merchantSourceList = merchantSourceMapper.selectByExample(sourceExample);
        if (merchantSourceList == null || merchantSourceList.size() < 1) {
            return null;
        }
        TMerchant merchant = merchantMapper.selectByPrimaryKey(merchantSourceList.get(0).getMerchantId());
        if (merchant == null) {
            return null;
        }
        return merchant;
    }

    @Override
    public TMerchant getMerchantBySource(String source, String channel) {
        TMerchantSourceExample sourceExample = new TMerchantSourceExample();
        sourceExample.or().andSourceEqualTo(source);
        List<TMerchantSource> merchantSourceList = merchantSourceMapper.selectByExample(sourceExample);
        if (merchantSourceList == null || merchantSourceList.size() < 1) {
            return null;
        }
        for(TMerchantSource merchantSource : merchantSourceList){
            TMerchant merchant = merchantMapper.selectByPrimaryKey(merchantSource.getMerchantId());
            if (merchant != null && channel.equals(merchant.getChannel())) {
                return merchant;
            }
        }
        return null;
    }

    @Override
    public TMerchant getMerchantById(Long id) {
        return merchantMapper.selectByPrimaryKey(id);
    }
}
