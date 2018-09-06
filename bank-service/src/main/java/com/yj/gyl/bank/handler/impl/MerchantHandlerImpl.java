package com.yj.gyl.bank.handler.impl;

import com.yj.gyl.bank.dao.TMerchantSourceMapper;
import com.yj.gyl.bank.handler.IMerchantHandler;
import com.yj.gyl.bank.model.TMerchantSource;
import com.yj.gyl.bank.model.TMerchantSourceExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by renfei on 2017/12/29.
 */
@Component
public class MerchantHandlerImpl implements IMerchantHandler {
    @Autowired
    private TMerchantSourceMapper merchantSourceMapper;

    @Override
    public TMerchantSource getMerchant(String source) {
        TMerchantSourceExample example = new TMerchantSourceExample();
        example.or().andSourceEqualTo(source);
        List<TMerchantSource> list = merchantSourceMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
