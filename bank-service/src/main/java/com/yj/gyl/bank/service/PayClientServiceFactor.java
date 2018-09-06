package com.yj.gyl.bank.service;

import com.yj.gyl.bank.handler.common.PaymentType;
import org.slf4j.LoggerFactory;

/**
 * Created by renfei on 2017/12/18.
 */
public class PayClientServiceFactor {
    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    public static IPayService CreatePayClient(PaymentType channel) {
        if (channel == null) {
            throw new IllegalArgumentException("channel == null");
        }
        try {
            String namespace = PayClientServiceFactor.class.getName();
            IPayService service = (IPayService) Class.forName(String.format("%s%s.PayService", namespace.substring(0, namespace.lastIndexOf(".") + 1), channel)).newInstance();
            return service;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
