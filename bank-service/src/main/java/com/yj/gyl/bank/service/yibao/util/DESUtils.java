package com.yj.gyl.bank.service.yibao.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;

/**
 * Created by renfei on 2017/12/7.
 */
public class DESUtils {
    public DESUtils() {
    }

    public static String encryptCG(String message) throws Exception {
        return encryptByKey("bankCard", message).replaceAll("[^0-9a-zA-Z]+", "");
    }

    public static String decryptCG(String message) throws Exception {
        return decryptByKey("bankCard", message).replaceAll("[^0-9a-zA-Z]+", "");
    }

    public static String decryptByKey(String key, String message) throws Exception {
        byte[] bytesrc = decodeBase64(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(2, secretKey, iv);
        byte[] retByte = cipher.doFinal(bytesrc);
        return new String(retByte);
    }

    public static String encryptByKey(String key, String message) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
        cipher.init(1, secretKey, iv);
        return encodeBase64(cipher.doFinal(message.getBytes("UTF-8")));
    }

    private static String encodeBase64(byte[] s) {
        if(s == null) {
            return null;
        } else {
            BASE64Encoder b = new BASE64Encoder();
            return b.encode(s);
        }
    }

    private static byte[] decodeBase64(String s) throws IOException {
        if(s == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(s);
            return b;
        }
    }

    public static void main(String[] args) throws Exception {
        String key = "bankCard";
        System.out.println(key.length());
        String value = "YB2017113016285606918401146431234567";
        String value1 = "";
        System.out.println(encryptByKey(key, value));
        System.out.println(encryptByKey(key, value1));
    }
}
