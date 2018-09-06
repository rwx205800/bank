package com.yj.gyl.bank.service.yibao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhanning on 2017/9/14.
 */
public class YiBaoStatusUtils {

    public final static Map<String, String> STATUS_MAP = new HashMap();


    static {
        STATUS_MAP.put("PAY_SUCCESS", "0");
        STATUS_MAP.put("PAY_FAIL", "1");
        STATUS_MAP.put("TIME_OUT", "2");
        STATUS_MAP.put("WITHDRAW_SUCCESS", "1");
        STATUS_MAP.put("WITHDRAW_FAIL", "2");
    }


    /**
     *
     *
     * @param key
     * @return
     */
    public static String getStatus(String key) {
        return STATUS_MAP.get(key);
    }
}
