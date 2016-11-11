package com.ote.test.aop;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Profile("with-aop")
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopConfiguration {

    @PostConstruct
    public void init() {
        LoggerFactory.getLogger(this.getClass()).warn("####- AOP is enabled");
    }
}
