package com.yj.gyl.bank.service.yibao.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenhanning on 2017/9/14.
 */
public class YiBaoInfoUtils {

    public final static Map<String, String> INFO_MAP = new HashMap();


    static {
        INFO_MAP.put("TO_VALIDATE", "待短验确认");
        INFO_MAP.put("BIND_SUCCESS", "绑卡成功");
        INFO_MAP.put("BIND_FAIL", "绑卡失败");
        INFO_MAP.put("BIND_ERROR", "绑卡异常(可重试)");
        INFO_MAP.put("TO_ENHANCED", "待补充鉴权");
        INFO_MAP.put("TIME_OUT", "超时失败");
        INFO_MAP.put("FAIL", "系统异常");
        INFO_MAP.put("PAY_FAIL", "充值失败");
        INFO_MAP.put("PAY_SUCCESS", "充值成功"); //
        INFO_MAP.put("PROCESSING", "处理中");
        INFO_MAP.put("WITHDRAW_FAIL", "提现失败");
        INFO_MAP.put("SUCCESS", "成功");
    }


    /**
     * 取出map中的值
     *
     * @param key
     * @return
     */
    public static String getInfo(String key) {
        return INFO_MAP.get(key);
    }
}
