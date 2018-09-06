package com.yj.gyl.bank.annotation;

import org.springframework.cloud.netflix.feign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.yj.gyl.bank.api"})
public class GylBankFeignProxy {
    public GylBankFeignProxy() {

    }
}
