package com.yj.gyl.bank.service.yibao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhanning on 2017/9/14.
 */
public class BankInfoUtils {

    public final static Map<String, String> CARD_INFO_MAP = new HashMap();


    static {
        CARD_INFO_MAP.put("ICBC", "工商银行");
        CARD_INFO_MAP.put("BOC", "中国银行");
        CARD_INFO_MAP.put("CCB", "建设银行");
        CARD_INFO_MAP.put("PSBC", "邮政储蓄");
        CARD_INFO_MAP.put("ECITIC", "中信银行");
        CARD_INFO_MAP.put("CEB", "光大银行");
        CARD_INFO_MAP.put("HX", "华夏银行");
        CARD_INFO_MAP.put("CIB", "兴业银行");
        CARD_INFO_MAP.put("SPDB", "浦发银行");
        CARD_INFO_MAP.put("SZPA", "平安银行");
        CARD_INFO_MAP.put("CMBC", "民生银行");
        CARD_INFO_MAP.put("GDB", "广发银行");
        CARD_INFO_MAP.put("ABC", "农业银行");
        CARD_INFO_MAP.put("CMBCHINA", "招商银行");
        CARD_INFO_MAP.put("BCCB", "北京银行");
        CARD_INFO_MAP.put("BOCO", "交通银行");
    }



    /**
     * 取出map中的值
     *
     * @param key
     * @return
     */
    public static String getBankInfo(String key) {
        return CARD_INFO_MAP.get(key);
    }
}
