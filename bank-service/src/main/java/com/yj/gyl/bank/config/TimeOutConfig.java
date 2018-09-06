package com.yj.gyl.bank.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by renfei on 2017/12/28.
 */
@Configuration
public class TimeOutConfig {

    @Bean
    Request.Options feignOptions() {
        return new Request.Options(/**connectTimeoutMillis**/60 * 1000, /** readTimeoutMillis **/300 * 1000);
    }

}
