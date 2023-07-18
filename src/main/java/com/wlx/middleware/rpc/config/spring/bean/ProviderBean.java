package com.wlx.middleware.rpc.config.spring.bean;

import com.alibaba.fastjson.JSON;
import com.wlx.middleware.rpc.config.ProviderConfig;
import com.wlx.middleware.rpc.domain.LocalServerInfo;
import com.wlx.middleware.rpc.domain.RpcProviderConfig;
import com.wlx.middleware.rpc.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ProviderBean extends ProviderConfig implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ProviderBean.class);
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        RpcProviderConfig rpcProviderConfig = new RpcProviderConfig();
        rpcProviderConfig.setInterfaces(getInterfaces());
        rpcProviderConfig.setAlias(getAlias());
        rpcProviderConfig.setRef(getRef());
        rpcProviderConfig.setHost(LocalServerInfo.LOCAL_HOST);
        rpcProviderConfig.setPort(LocalServerInfo.LOCAL_PORT);

        RedisRegistryCenter.registryProvider(getInterfaces(), getAlias(), JSON.toJSONString(rpcProviderConfig));
        logger.info("注册生产者，接口:{}-{}, 地址：{}:{}", getInterfaces(), getAlias(), LocalServerInfo.LOCAL_HOST,
                LocalServerInfo.LOCAL_PORT);
    }

}
