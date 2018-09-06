package com.yj.gyl.bank.handler;

/**
 * Created by renfei on 2018/6/27.
 */
public interface IYopCallBackHandler {

    void tradeOrderCallback() throws Exception;

    void tradeOrderCallbackForPage() throws Exception;

    void transferSendCallback() throws Exception;
}
