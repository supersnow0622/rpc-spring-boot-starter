package com.wlx.middleware.rpc.annotation;

import com.wlx.middleware.rpc.config.ServerAutoConfiguration;
import com.wlx.middleware.rpc.config.ServerProperties;
import com.wlx.middleware.rpc.config.spring.bean.ServerBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ServerAutoConfiguration.class)
@EnableConfigurationProperties(ServerProperties.class)
@ComponentScan("com.wlx.middleware.*")
public @interface EnableRpc {
}
