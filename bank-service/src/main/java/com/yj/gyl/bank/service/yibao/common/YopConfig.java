package com.yj.gyl.bank.service.yibao.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Created by renfei on 2018/6/27.
 */
@Configuration
public class YopConfig {
    private static String privatekey;

    private static String parentMerchantNo;

    private static String merchantNo;

    private static String baseURL;

    private static String tradeOrderURI;

    private static String orderQueryURI;

    private static String cashier;

    private static String apicashier;

    private static String tradeOrderCallback;

    private static String paymentURI;

    private static String paymentqueryURI;

    public static String getPaymentURI() {
        return paymentURI;
    }
    @Value("${yop.paymentURI}")
    public void setPaymentURI(String paymentURI) {
        YopConfig.paymentURI = paymentURI;
    }

    public static String getPaymentqueryURI() {
        return paymentqueryURI;
    }
    @Value("${yop.paymentqueryURI}")
    public void setPaymentqueryURI(String paymentqueryURI) {
        YopConfig.paymentqueryURI = paymentqueryURI;
    }

    public static String getTradeOrderCallback() {
        return tradeOrderCallback;
    }
    @Value("${yop.tradeOrderCallback}")
    public void setTradeOrderCallback(String tradeOrderCallback) {
        YopConfig.tradeOrderCallback = tradeOrderCallback;
    }

    public static String getPrivatekey() {
        return privatekey;
    }
    @Value("${yop.privatekey}")
    public void setPrivatekey(String privatekey) {
        YopConfig.privatekey = privatekey;
    }

    public static String getParentMerchantNo() {
        return parentMerchantNo;
    }
    @Value("${yop.parentMerchantNo}")
    public void setParentMerchantNo(String parentMerchantNo) {
        YopConfig.parentMerchantNo = parentMerchantNo;
    }

    public static String getMerchantNo() {
        return merchantNo;
    }

    @Value("${yop.merchantNo}")
    public void setMerchantNo(String merchantNo) {
        YopConfig.merchantNo = merchantNo;
    }

    public static String getBaseURL() {
        return baseURL;
    }
    @Value("${yop.baseURL}")
    public void setBaseURL(String baseURL) {
        YopConfig.baseURL = baseURL;
    }

    public static String getTradeOrderURI() {
        return tradeOrderURI;
    }
    @Value("${yop.tradeOrderURI}")
    public void setTradeOrderURI(String tradeOrderURI) {
        YopConfig.tradeOrderURI = tradeOrderURI;
    }

    public static String getOrderQueryURI() {
        return orderQueryURI;
    }
    @Value("${yop.orderQueryURI}")
    public void setOrderQueryURI(String orderQueryURI) {
        YopConfig.orderQueryURI = orderQueryURI;
    }

    public static String getCashier() {
        return cashier;
    }
    @Value("${yop.cashier}")
    public void setCashier(String cashier) {
        YopConfig.cashier = cashier;
    }

    public static String getApicashier() {
        return apicashier;
    }
    @Value("${yop.apicashier}")
    public void setApicashier(String apicashier) {
        YopConfig.apicashier = apicashier;
    }
}
