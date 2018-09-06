package com.yj.gyl.bank.model;

import com.yeepay.g3.sdk.yop.YopServiceException;
import com.yeepay.g3.sdk.yop.client.YopConstants;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.config.AppSDKConfig;
import com.yeepay.g3.sdk.yop.config.AppSDKConfigSupport;
import com.yeepay.g3.sdk.yop.config.ConfigUtils;
import com.yeepay.g3.sdk.yop.config.SDKConfig;
import com.yeepay.g3.sdk.yop.utils.JsonUtils;
import com.yeepay.shade.com.google.common.collect.ArrayListMultimap;
import com.yeepay.shade.com.google.common.collect.Multimap;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by renfei on 2018/6/30.
 */
public class YopRequestChild extends YopRequest{
    private Logger logger = Logger.getLogger(this.getClass());
    private String locale = "zh_CN";
    private String signAlg = "SHA1";
    private Multimap<String, String> paramMap = ArrayListMultimap.create();
    private Multimap<String, Object> multiportFiles = ArrayListMultimap.create();
    private Map<String, String> headers = new HashMap();
    private List<String> ignoreSignParams = new ArrayList(Arrays.asList(new String[]{"sign"}));

    public YopRequestChild(){
        this.init();
    }

    private void init() {
        this.headers.put("User-Agent", YopConstants.USER_AGENT);
        this.paramMap.put("appKey", "OPR:10000466938");
        this.paramMap.put("locale", this.locale);
        this.paramMap.put("ts", String.valueOf(System.currentTimeMillis()));
    }



}
