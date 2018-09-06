package com.yj.gyl.bank.handler;

/**
 * Created by renfei on 2017/12/19.
 */
public interface IYbCallBackHandler {

    /**
     * 易宝充值确认回调
     *
     * @throws Exception
     */
    void callbackRecharge(Long merchantId) throws Exception;

    /**
     * 易宝提现确认回调
     *
     * @throws Exception
     */

    void callbackWithDraw(Long merchantId) throws Exception;

    /**
     * 易宝网银充值回调接口
     *
     * @throws Exception
     */
    void callbackEbankRecharge() throws Exception;
}
