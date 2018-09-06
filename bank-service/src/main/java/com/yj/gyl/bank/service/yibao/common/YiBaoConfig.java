package com.yj.gyl.bank.service.yibao.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by renfei on 2017/12/18.
 */
@Configuration
public class YiBaoConfig {
    /**
     * 是否为正式环境
     */
    private static boolean preFlag;
    /**
     * 提现金额的最小限额
     */
    private static String minLimit;
    /**
     * 商户产品名称
     */
    private static String productName;
    /**
     * 商户号(代付代发)
     */
    private static String merId;
    /**
     * 秘钥(代付代发)
     */
    private static String hmacKey;
    /**
     * 预绑卡地址
     */
    private static String preBindBarkUrl;
    /**
     * 确认绑卡地址
     */
    private static String confirmBindBankcardUrl;
    /**
     * 充值-重发短信验证码地址
     */
    private static String reSendSmsRechargeUrl;
    /**
     * 绑卡-重发短信验证码地址
     */
    private static String reSendSmsBindBankUrl;
    /**
     * 预充值地址
     */
    private static String preRechargeUrl;
    /**
     * 确认充值地址
     */
    private static String confirmRechargeUrl;
    /**
     * 获取绑卡列表地址
     */
    private static String bindCardListUrl;
    /**
     * 充值回调地址
     */
    private static String callbackRechargeUrl;
    /**
     * 充值查询
     */
    private static String rechargeRecordUrl;
    /**
     * 绑卡查询
     */
    private static String bindCardRecordUrl;
    /**
     * 提现查询
     */
    private static String withdrawRecordUrl;
    /**
     * 提现申请
     */
    private static String withdrawUrl;
    /**
     * 提现回调
     */
    private static String callbackWithdrawUrl;
    /**
     * 解除绑卡地址
     */
    private static String delBindBankcardUrl;
    /**
     * 商户余额查询地址
     */
    private static String validAmountUrl;
    /**
     * 提现查询(代付代发)
     */
    private static String accountBalanceQueryUrl;
    /**
     * 单笔打款(代付代发)
     */
    private static String transferSingleUrl;
    /**
     * 账户名称(代付代发)
     */
    private static String accountName;
    /**
     * 账号(代付代发)
     */
    private static String accountNumber;
    /**
     * 银行编码(代付代发)
     */
    private static String bankCode;
    /**
     * 银行名称(代付代发)
     */
    private static String bankName;
    /**
     * 银行支行名称(代付代发)
     */
    private static String bankBranchName;
    /**
     * 收款行省份编码(代付代发)
     */
    private static String province;
    /**
     * 收款行城市编码(代付代发)
     */
    private static String city;
    /**
     * 验证签名地址
     */
    private static String signUrl;
    /**
     * 网银支付商户号
     */
    private static String yeePayMerId;
    /**
     * 网银支付商户密钥
     */
    private static String yeePayKey;
    /**
     * 网银支付地址
     */
    private static String yeePayUrl;
    /**
     * 网银支付查询
     */
    private static String yeePayQueryUrl;
    /**
     * 网银支付回调
     */
    private static String yeePayCallBack;

    private static String port;

    private static String environment;

    public static String getPort() {
        return port;
    }

    @Value("${server.port}")
    public void setPort(String port) {
        YiBaoConfig.port = port;
    }

    public static String getProductName() {
        return productName;
    }

    @Value("${yibao.productName}")
    public void setProductName(String productName) {
        YiBaoConfig.productName = productName;
    }

    public static String getMerId() {
        return merId;
    }

    @Value("${yibao.merId}")
    public void setMerId(String merId) {
        YiBaoConfig.merId = merId;
    }

    public static String getHmacKey() {
        return hmacKey;
    }

    @Value("${yibao.hmacKey}")
    public void setHmacKey(String hmacKey) {
        YiBaoConfig.hmacKey = hmacKey;
    }

    public static String getPreBindBarkUrl() {
        return preBindBarkUrl;
    }

    @Value("${yibao.preBindBarkUrl}")
    public void setPreBindBarkUrl(String preBindBarkUrl) {
        YiBaoConfig.preBindBarkUrl = preBindBarkUrl;
    }

    public static String getConfirmBindBankcardUrl() {
        return confirmBindBankcardUrl;
    }

    @Value("${yibao.confirmBindBankcardUrl}")
    public void setConfirmBindBankcardUrl(String confirmBindBankcardUrl) {
        YiBaoConfig.confirmBindBankcardUrl = confirmBindBankcardUrl;
    }

    public static String getReSendSmsRechargeUrl() {
        return reSendSmsRechargeUrl;
    }

    @Value("${yibao.reSendSmsRechargeUrl}")
    public void setReSendSmsRechargeUrl(String reSendSmsRechargeUrl) {
        YiBaoConfig.reSendSmsRechargeUrl = reSendSmsRechargeUrl;
    }

    public static String getReSendSmsBindBankUrl() {
        return reSendSmsBindBankUrl;
    }

    @Value("${yibao.reSendSmsBindBankUrl}")
    public void setReSendSmsBindBankUrl(String reSendSmsBindBankUrl) {
        YiBaoConfig.reSendSmsBindBankUrl = reSendSmsBindBankUrl;
    }

    public static String getPreRechargeUrl() {
        return preRechargeUrl;
    }

    @Value("${yibao.preRechargeUrl}")
    public void setPreRechargeUrl(String preRechargeUrl) {
        YiBaoConfig.preRechargeUrl = preRechargeUrl;
    }

    public static String getConfirmRechargeUrl() {
        return confirmRechargeUrl;
    }

    @Value("${yibao.confirmRechargeUrl}")
    public void setConfirmRechargeUrl(String confirmRechargeUrl) {
        YiBaoConfig.confirmRechargeUrl = confirmRechargeUrl;
    }

    public static String getBindCardListUrl() {
        return bindCardListUrl;
    }

    @Value("${yibao.bindCardListUrl}")
    public void setBindCardListUrl(String bindCardListUrl) {
        YiBaoConfig.bindCardListUrl = bindCardListUrl;
    }

    public static String getCallbackRechargeUrl() {
        return callbackRechargeUrl;
    }

    @Value("${yibao.callbackRechargeUrl}")
    public void setCallbackRechargeUrl(String callbackRechargeUrl) {
        YiBaoConfig.callbackRechargeUrl = callbackRechargeUrl;
    }

    public static String getRechargeRecordUrl() {
        return rechargeRecordUrl;
    }

    @Value("${yibao.rechargeRecordUrl}")
    public void setRechargeRecordUrl(String rechargeRecordUrl) {
        YiBaoConfig.rechargeRecordUrl = rechargeRecordUrl;
    }

    public static String getBindCardRecordUrl() {
        return bindCardRecordUrl;
    }

    @Value("${yibao.bindCardRecordUrl}")
    public void setBindCardRecordUrl(String bindCardRecordUrl) {
        YiBaoConfig.bindCardRecordUrl = bindCardRecordUrl;
    }

    public static String getWithdrawRecordUrl() {
        return withdrawRecordUrl;
    }

    @Value("${yibao.withdrawRecordUrl}")
    public void setWithdrawRecordUrl(String withdrawRecordUrl) {
        YiBaoConfig.withdrawRecordUrl = withdrawRecordUrl;
    }

    public static String getWithdrawUrl() {
        return withdrawUrl;
    }

    @Value("${yibao.withdrawUrl}")
    public void setWithdrawUrl(String withdrawUrl) {
        YiBaoConfig.withdrawUrl = withdrawUrl;
    }

    public static String getCallbackWithdrawUrl() {
        return callbackWithdrawUrl;
    }

    @Value("${yibao.callbackWithdrawUrl}")
    public void setCallbackWithdrawUrl(String callbackWithdrawUrl) {
        YiBaoConfig.callbackWithdrawUrl = callbackWithdrawUrl;
    }

    public static String getDelBindBankcardUrl() {
        return delBindBankcardUrl;
    }

    @Value("${yibao.delBindBankcardUrl}")
    public void setDelBindBankcardUrl(String delBindBankcardUrl) {
        YiBaoConfig.delBindBankcardUrl = delBindBankcardUrl;
    }

    public static String getValidAmountUrl() {
        return validAmountUrl;
    }

    @Value("${yibao.validAmountUrl}")
    public void setValidAmountUrl(String validAmountUrl) {
        YiBaoConfig.validAmountUrl = validAmountUrl;
    }

    public static String getAccountBalanceQueryUrl() {
        return accountBalanceQueryUrl;
    }

    @Value("${yibao.accountBalanceQueryUrl}")
    public void setAccountBalanceQueryUrl(String accountBalanceQueryUrl) {
        YiBaoConfig.accountBalanceQueryUrl = accountBalanceQueryUrl;
    }

    public static String getTransferSingleUrl() {
        return transferSingleUrl;
    }

    @Value("${yibao.transferSingleUrl}")
    public void setTransferSingleUrl(String transferSingleUrl) {
        YiBaoConfig.transferSingleUrl = transferSingleUrl;
    }

    public static String getAccountName() {
        return accountName;
    }

    @Value("${yibao.accountName}")
    public void setAccountName(String accountName) {
        YiBaoConfig.accountName = accountName;
    }

    public static String getAccountNumber() {
        return accountNumber;
    }

    @Value("${yibao.accountNumber}")
    public void setAccountNumber(String accountNumber) {
        YiBaoConfig.accountNumber = accountNumber;
    }

    public static String getBankCode() {
        return bankCode;
    }

    @Value("${yibao.bankCode}")
    public void setBankCode(String bankCode) {
        YiBaoConfig.bankCode = bankCode;
    }

    public static String getBankName() {
        return bankName;
    }

    @Value("${yibao.bankName}")
    public void setBankName(String bankName) {
        YiBaoConfig.bankName = bankName;
    }

    public static String getBankBranchName() {
        return bankBranchName;
    }

    @Value("${yibao.bankBranchName}")
    public void setBankBranchName(String bankBranchName) {
        YiBaoConfig.bankBranchName = bankBranchName;
    }

    public static String getProvince() {
        return province;
    }

    @Value("${yibao.province}")
    public void setProvince(String province) {
        YiBaoConfig.province = province;
    }

    public static String getCity() {
        return city;
    }

    @Value("${yibao.city}")
    public void setCity(String city) {
        YiBaoConfig.city = city;
    }

    public static String getSignUrl() {
        return signUrl;
    }

    @Value("${yibao.signUrl}")
    public void setSignUrl(String signUrl) {
        YiBaoConfig.signUrl = signUrl;
    }

    public static boolean isPreFlag() {
        return preFlag;
    }

    @Value("${yibao.preFlag}")
    public void setPreFlag(boolean preFlag) {
        YiBaoConfig.preFlag = preFlag;
    }

    public static String getMinLimit() {
        return minLimit;
    }

    @Value("${yibao.minLimit}")
    public void setMinLimit(String minLimit) {
        YiBaoConfig.minLimit = minLimit;
    }

    public static String getYeePayMerId() {
        return yeePayMerId;
    }

    @Value("${yibao.yeePayMerId}")
    public void setYeePayMerId(String yeePayMerId) {
        YiBaoConfig.yeePayMerId = yeePayMerId;
    }

    public static String getYeePayKey() {
        return yeePayKey;
    }

    @Value("${yibao.yeePayKey}")
    public void setYeePayKey(String yeePayKey) {
        YiBaoConfig.yeePayKey = yeePayKey;
    }

    public static String getYeePayUrl() {
        return yeePayUrl;
    }

    @Value("${yibao.yeePayUrl}")
    public void setYeePayUrl(String yeePayUrl) {
        YiBaoConfig.yeePayUrl = yeePayUrl;
    }

    public static String getYeePayCallBack() {
        return yeePayCallBack;
    }

    @Value("${yibao.yeePayCallBack}")
    public void setYeePayCallBack(String yeePayCallBack) {
        YiBaoConfig.yeePayCallBack = yeePayCallBack;
    }

    public static String getYeePayQueryUrl() {
        return yeePayQueryUrl;
    }

    @Value("${yibao.yeePayQueryUrl}")
    public void setYeePayQueryUrl(String yeePayQueryUrl) {
        YiBaoConfig.yeePayQueryUrl = yeePayQueryUrl;
    }

    public static String getEnvironment() {
        return environment;
    }
    @Value("${spring.profiles}")
    public void setEnvironment(String environment) {
        YiBaoConfig.environment = environment;
    }
}
