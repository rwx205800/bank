package com.yj.gyl.bank.rsdto;

import com.yj.base.common.exception.CodeEntity;
import com.yj.base.common.exception.IBuzinessCodeEnum;

/**
 * Created by renfei on 2017/11/21.
 */
public enum TransactionPayEnum implements IBuzinessCodeEnum {
    @CodeEntity(status = 0, result = 0, msg = "成功")
    GLOBAL_SUCCESS,
    @CodeEntity(status = 0, result = 1, msg = "失败")
    GLOBAL_FAIL,
    /**
     * 预绑卡返回枚举
     */
    @CodeEntity(status = 1, result = 1, msg = "银行卡号为空")
    YB_PAY_CARDNO_EMPTY,
    @CodeEntity(status = 2, result = 1, msg = "用户id为空")
    YB_PAY_USERID_EMPTY,
    @CodeEntity(status = 3, result = 1, msg = "手机号为空")
    YB_PAY_PHONE_EMPTY,
    @CodeEntity(status = 4, result = 1, msg = "身份证为空")
    YB_PAY_IDCARDNO_EMPTY,
    @CodeEntity(status = 5, result = 1, msg = "用户姓名为空")
    YB_PAY_USERNAME_EMPTY,
    @CodeEntity(status = 6, result = 1, msg = "该银行卡被其他用户绑定过，请联系客服")
    YB_PAY_OTHERUSER_USED,
    @CodeEntity(status = 7, result = 1, msg = "该卡已绑定")
    YB_PAY_CARD_USED,
    @CodeEntity(status = 8, result = 1, msg = "已有绑卡，请先解绑")
    YB_PAY_CARD_BIND,
    @CodeEntity(status = 9, result = 1, msg = "交易来源为空")
    YB_PAY_SOURCE_EMPTY,
    @CodeEntity(status = 10, result = 1, msg = "商户号未配置")
    YB_PAY_MERCHANT_SOURCE_NOT_SETTING,
    @CodeEntity(status = 11, result = 1, msg = "交易通道错误")
    YB_PAY_PREBINDCARD_CHANNEL_ERROR,
    @CodeEntity(status = 12, result = 1, msg = "校验用户信息有误")
    YB_PAY_USER_INFO_ERROR,
    @CodeEntity(status = 13, result = 1, msg = "校验用户实名信息有误")
    YB_PAY_USER_INFO_IDENTITY,
    /**
     * 确定绑卡返回枚举
     */
    @CodeEntity(status = 1, result = 1, msg = "验证码为空")
    YB_PAY_BINDCARD_VALIDATECODE,
    @CodeEntity(status = 2, result = 1, msg = "请填写卡号")
    YB_PAY_BINDCARD_CARD_EMPTY,
    @CodeEntity(status = 3, result = 1, msg = "请填写渠道")
    YB_PAY_BINDCARD_CHANNEL_EMPTY,
    @CodeEntity(status = 4, result = 1, msg = "请填写用户ID")
    YB_PAY_BINDCARD_USERID_EMPTY,
    @CodeEntity(status = 5, result = 1, msg = "请填写订单号")
    YB_PAY_BINDCARD_ORDERNO_EMPTY,
    @CodeEntity(status = 6, result = 1, msg = "交易通道错误")
    YB_PAY_BINDCARD_CHANNEL_ERROR,
    @CodeEntity(status = 7, result = 1, msg = "交易来源为空")
    YB_PAY_BINDCARD_SOURCE_EMPTY,
    @CodeEntity(status = 8, result = 1, msg = "商户号未配置")
    YB_PAY_BINDCARD_MERCHANT_ERROR,

    /**
     * 解绑卡
     */
    @CodeEntity(status = 1, result = 1, msg = "请填写卡号")
    YB_PAY_UNBINDCARD_CARD_EMPTY,
    @CodeEntity(status = 2, result = 1, msg = "请填写用户ID")
    YB_PAY_UNBINDCARD_USERID_EMPTY,
    @CodeEntity(status = 3, result = 1, msg = "银行卡信息有误")
    YB_PAY_UNBINDCARD_CARD_ERROR,
    @CodeEntity(status = 4, result = 1, msg = "银行卡未绑定")
    YB_PAY_UNBINDCARD_CARD_NOT_BIND,
    @CodeEntity(status = 5, result = 1, msg = "渠道错误")
    YB_PAY_UNBINDCARD_SOURCE_ERROR,
    /**
     * 提现返回枚举
     */
    @CodeEntity(status = 1, result = 1, msg = "银行卡号为空")
    YB_PAY_WITHDRAW_CARDNO_EMPTY,
    @CodeEntity(status = 2, result = 1, msg = "用户id为空")
    YB_PAY_WITHDRAW_USERID_EMPTY,
    @CodeEntity(status = 3, result = 1, msg = "提现金额有误")
    YB_PAY_WITHDRAW_AMOUNT,
    @CodeEntity(status = 4, result = 1, msg = "请填写回调地址")
    YB_PAY_WITHDRAW_RESPONSEURL,
    @CodeEntity(status = 5, result = 1, msg = "银行卡信息不存在")
    YB_PAY_WITHDRAW_CARD_NOTEXIST,
    @CodeEntity(status = 6, result = 1, msg = "银行卡未绑定")
    YB_PAY_WITHDRAW_CARD_NOT_BIND,
    @CodeEntity(status = 7, result = 1, msg = "渠道错误")
    YB_PAY_WITHDRAW_SOURCE_ERROR,
    @CodeEntity(status = 8, result = 1, msg = "请填写订单号")
    YB_PAY_WITHDRAW_ORDERNO_EMPTY,
    @CodeEntity(status = 9, result = 1, msg = "您的资金正在迁往存管账户")
    YB_PAY_WITHDRAW_DELETE,

    /**
     * 充值、提现查询枚举
     */
    @CodeEntity(status = 1, result = 1, msg = "订单号为空")
    YB_PAY_SEARCH_ORDERNO_EMPTY,
    @CodeEntity(status = 2, result = 1, msg = "订单号有误")
    YB_PAY_SEARCH_ORDERNO_ERROR,
    @CodeEntity(status = 3, result = 1, msg = "支付通道错误")
    YB_PAY_SEARCH_SOURCE_ERROR,

    /**
     * 查询绑卡列表
     */
    @CodeEntity(status = 1, result = 0, msg = "用户查询绑卡列表为空")
    YB_PAY_GET_BIND_CARD,
    @CodeEntity(status = 2, result = 0, msg = "来源为空")
    YB_PAY_GET_BIND_CARD_SOURCE_EMPYT,
    @CodeEntity(status = 3, result = 0, msg = "商户号未配置")
    YB_PAY_GET_BIND_CARD_SETTING,


    @CodeEntity(status = 2, result = 0, msg = "提现记录为空")
    YB_PAY_WITHDRAW,
    @CodeEntity(status = 3, result = 0, msg = "充值记录为空")
    YB_PAY_RECHRGE,


    /**
     * 充值返回值枚举
     */
    @CodeEntity(status = 1, result = 1, msg = "支付银行卡号为空")
    YB_PAY_RECHARGE_CARDNO_EMPTY,
    @CodeEntity(status = 2, result = 1, msg = "用户id为空")
    YB_PAY_RECHARGE_UID_EMPTY,
    @CodeEntity(status = 3, result = 1, msg = "支付交易号为空")
    YB_PAY_RECHARGE_ORDERNO_EMPTY,
    @CodeEntity(status = 4, result = 1, msg = "请正确填写金额")
    YB_PAY_RECHARGE_MONEY_EMPTY,
    @CodeEntity(status = 5, result = 1, msg = "请填写回调地址")
    YB_PAY_RECHARGE_RESPONSE_URL_EMPTY,
    @CodeEntity(status = 6, result = 1, msg = "银行卡信息不存在")
    YB_PAY_RECHARGE_CARDNO_NOT_EXIST,
    @CodeEntity(status = 7, result = 1, msg = "未查询到绑卡记录")
    YB_PAY_RECHARGE_CARDNO_NOT_BIND,
    @CodeEntity(status = 8, result = 1, msg = "支付通道错误")
    YB_PAY_RECHARGE_CHANNEL_ERROR,

    @CodeEntity(status = 1, result = 0, msg = "短信验证码为空")
    YB_PAY_VERIFY_RECHARGE_VALIDATE_CODE_EMPTY,
    @CodeEntity(status = 2, result = 0, msg = "预充值记录为空")
    YB_PAY_VERIFY_RECHARGE_RECORD_EMPTY,

    /**
     * 是否首次绑卡
     */
    @CodeEntity(status = 0, result = 0, msg = "首次绑定")
    YB_PAY_NOT_BIND_CARD,
    @CodeEntity(status = 1, result = 0, msg = "再次绑定")
    YB_PAY_BIND_CARD,
    @CodeEntity(status = 2, result = 0, msg = "商户号未配置")
    YB_PAY_BIND_CARD_SETTING,
    @CodeEntity(status = 3, result = 0, msg = "来源为空")
    YB_PAY_BIND_CARD_SOURCE_EMPTY,

    /**
     * 获取用户实名信息
     */
    @CodeEntity(status = 1, result = 0, msg = "用户id为空")
    YB_PAY_GET_USER_INFO_USERID_EMPYT,
    @CodeEntity(status = 2, result = 0, msg = "无有效实名信息")
    YB_PAY_GET_USER_INFO_ERROR,
    ;
}
