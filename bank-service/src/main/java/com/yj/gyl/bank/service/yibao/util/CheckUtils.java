package com.yj.gyl.bank.service.yibao.util;

import com.yeepay.DigestUtil;
import com.yj.gyl.bank.service.yibao.common.YiBaoConfig;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


public class CheckUtils {

	public static final String COMMON_FIELD = "flowID,initiator,";


	/**
	 * 验证对象是否为NULL,空字符串，空数组，空的Collection或Map(只有空格的字符串也认为是空串)
	 * @param obj 被验证的对象
	 * @param message 异常信息
	 */
	@SuppressWarnings("rawtypes")
	public static void notEmpty(Object obj, String message) {
		if (obj == null){
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof String && obj.toString().trim().length()==0){
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj.getClass().isArray() && Array.getLength(obj)==0){
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof Collection && ((Collection)obj).isEmpty()){
			throw new IllegalArgumentException(message + " must be specified");
		}
		if (obj instanceof Map && ((Map)obj).isEmpty()){
			throw new IllegalArgumentException(message + " must be specified");
		}
	}

	/**
	 * 验证回调参数是否有效
	 *
	 * @param stringValue
	 * @param hmacFromYeepay
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean verifyCallbackHmac(String[] stringValue, String hmacFromYeepay) throws UnsupportedEncodingException {
		String localHmac = DigestUtil.getHmac(stringValue, YiBaoConfig.getYeePayKey());
		return (localHmac.equals(hmacFromYeepay) ? true : false);
	}

	/**
	 * 验证回调安全参数
	 *
	 * @param stringValue
	 * @param hmac_safeFromYeepay
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean verifyCallbackHmac_safe(String[] stringValue, String hmac_safeFromYeepay) throws UnsupportedEncodingException {
		StringBuffer sourceData = new StringBuffer();
		for (int i = 0; i < stringValue.length; i++) {
			if (!"".equals(stringValue[i])) {
				sourceData.append(stringValue[i] + "#");
			}
		}
		sourceData = sourceData.deleteCharAt(sourceData.length() - 1);
		String localHmac_safe = DigestUtil.hmacSign(sourceData.toString(), YiBaoConfig.getYeePayKey());
		return (localHmac_safe.equals(hmac_safeFromYeepay) ? true : false);
	}

}
