package com.yj.gyl.bank.util;

import com.yj.base.common.utils.SecureUtil;

/**
 * Created by renfei on 2018/2/7.
 */
public class SecretUtil {
    public static String password = "yptransactionpay";

    public static String encrypt(String content) {
        String encryptStr = "";
        try {
            encryptStr = SecureUtil.encrypt16(content, password);
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
        return encryptStr;
    }

    public static String decrypt(String content) {
        String decryptStr = "";
        try {
            decryptStr = SecureUtil.decrypt16(content, password);
        } catch (Exception e) {
            e.printStackTrace();
            return content;
        }
        return decryptStr;
    }

}
