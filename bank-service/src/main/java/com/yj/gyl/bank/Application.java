package com.yj.gyl.bank;

import com.hzm.core.restTemplate.YJRestTemplateStarter;
import com.yj.feign.UserCenterStarter;
import com.yj.gyl.composer.channel.ComposerChannelStarter;
import com.yj.snowflake.api.SnowflakeStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
@UserCenterStarter
@YJRestTemplateStarter
@SnowflakeStarter
@ComposerChannelStarter
@EnableHystrix
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
