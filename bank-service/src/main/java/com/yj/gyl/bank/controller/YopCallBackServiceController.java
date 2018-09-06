package com.yj.gyl.bank.controller;

import com.yj.base.common.AbstractController;
import com.yj.gyl.bank.api.IYopCallBackServiceApi;
import com.yj.gyl.bank.handler.IYopCallBackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by renfei on 2018/6/27.
 */
@RestController
public class YopCallBackServiceController extends AbstractController implements IYopCallBackServiceApi {
    @Autowired
    private IYopCallBackHandler yopCallBackHandler;

//    @Override
//    public void tradeOrderCallbackForPage() throws Exception {
//        yopCallBackHandler.tradeOrderCallbackForPage();
//    }

    @Override
    public void tradeOrderCallback() throws Exception {
        yopCallBackHandler.tradeOrderCallback();
    }

    @Override
    public void transferSendCallback() throws Exception {
        yopCallBackHandler.transferSendCallback();
    }
}
