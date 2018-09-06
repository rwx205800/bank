package com.yj.gyl.bank.mq;


import com.yj.base.alimq.annotations.MessageTag;

import java.lang.annotation.*;

/**
 * 
 * @author hzm
 * 2017-10-31
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@MessageTag
public @interface TransactionPayMQAnnotation {
    TransactionPayResult value();
}
