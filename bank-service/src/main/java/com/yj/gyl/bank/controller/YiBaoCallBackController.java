package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.gyl.bank.api.IYiBaoCallBackServiceApi;
import com.yj.gyl.bank.handler.IYbCallBackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hepei
 * @date 2017/12/13 17:28:39
 */
@RestController
public class YiBaoCallBackController extends AbstractController implements IYiBaoCallBackServiceApi {
    @Autowired
    private IYbCallBackHandler ybCallBackHandler;

    @Override
    public void callbackRecharge(@PathVariable Long merchantId) throws Exception {
        ybCallBackHandler.callbackRecharge(merchantId);
    }

    @Override
    public void callbackWithDraw(@PathVariable Long merchantId) throws Exception {
        ybCallBackHandler.callbackWithDraw(merchantId);
    }

    @Override
    public void callbackEbankRecharge() throws Exception {
        ybCallBackHandler.callbackEbankRecharge();
    }
}
