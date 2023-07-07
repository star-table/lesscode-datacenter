package com.polaris.lesscode.dc;

import io.sentry.Sentry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableFeignClients(basePackages = {"com.polaris.lesscode.form.internal.feign"})
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DataCenterApplication {

    public static void main(String[] args) {
        String env = System.getenv("SERVER_ENVIROMENT");
        Sentry.getStoredClient().setEnvironment(env);
        SpringApplication.run(DataCenterApplication.class, args);
    }
}
