package com.yj.gyl.bank.service.yibao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhanning on 2017/11/14.
 */
public class YiBaoEBankUtil {


    public final static Map<String, String> CARD_INFO_MAP = new HashMap();

    static {
        CARD_INFO_MAP.put("ICBC", "ICBC-NET-B2C");
        CARD_INFO_MAP.put("BOC", "BOC-NET-B2C");
        CARD_INFO_MAP.put("CCB", "CCB-NET-B2C");
        CARD_INFO_MAP.put("ECITIC", "ECITIC-NET-B2C");
        CARD_INFO_MAP.put("CEB", "CEB-NET-B2C");
        CARD_INFO_MAP.put("HXB", "HXB-NET-B2C");
        CARD_INFO_MAP.put("CIB", "CIB-NET-B2C");
        CARD_INFO_MAP.put("SPDB", "SPDB-NET-B2C");
        CARD_INFO_MAP.put("SDB", "SDB-NET-B2C");
        CARD_INFO_MAP.put("CMBC", "CMBC-NET-B2C");
        CARD_INFO_MAP.put("GDB", "GDB-NET-B2C");
        CARD_INFO_MAP.put("ABC", "ABC-NET-B2C");
        CARD_INFO_MAP.put("CMBCHINA", "CMBCHINA-NET-B2C");
        CARD_INFO_MAP.put("BCCB", "BCCB-NET-B2C");
        CARD_INFO_MAP.put("BOCO", "BOCO-NET-B2C");
        CARD_INFO_MAP.put("PINGANBANK", "PINGANBANK-NET-B2C");
        CARD_INFO_MAP.put("SHB", "SHB-NET-B2C");
        CARD_INFO_MAP.put("POST", "POST-NET-B2C");
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
