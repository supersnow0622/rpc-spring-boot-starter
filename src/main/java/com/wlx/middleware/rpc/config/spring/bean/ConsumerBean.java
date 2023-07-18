package com.wlx.middleware.rpc.config.spring.bean;

import com.alibaba.fastjson.JSON;
import com.wlx.middleware.rpc.config.ConsumerConfig;
import com.wlx.middleware.rpc.domain.RpcProviderConfig;
import com.wlx.middleware.rpc.network.client.ClientSocket;
import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.reflect.JDKProxy;
import com.wlx.middleware.rpc.registry.RedisRegistryCenter;
import com.wlx.middleware.rpc.utils.ClassLoaderUtils;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.FactoryBean;

public class ConsumerBean<T> extends ConsumerConfig implements FactoryBean {

    @Override
    public Object getObject() throws Exception {
        // 从注册中心获取到服务端接口的信息
        String providerInfo = RedisRegistryCenter.getProviderInfo(getInterfaces(), getAlias());
        RpcProviderConfig rpcProviderConfig = JSON.parseObject(providerInfo, RpcProviderConfig.class);
        if (rpcProviderConfig == null) {
            throw new IllegalArgumentException("接口:" + getInterfaces() + "_" + getAlias() + "不存在");
        }

        // 启动客户端，与服务端建立连接
        ClientSocket clientSocket = new ClientSocket(rpcProviderConfig.getHost(), rpcProviderConfig.getPort());
        new Thread(clientSocket).start();

        ChannelFuture channelFuture = null;
        while (true) {
            if (channelFuture != null) {
                break;
            }
            Thread.sleep(50);
            channelFuture = clientSocket.getChannelFuture();
        }

        // 封装请求的request
        Request request = new Request();
        request.setChannel(channelFuture.channel());
        request.setInterfaces(getInterfaces());
        request.setAlias(getAlias());
        request.setRef(rpcProviderConfig.getRef());

        Class clazz = ClassLoaderUtils.forName(getInterfaces());
        return new JDKProxy(new Class<?>[]{clazz}, request).getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return ClassLoaderUtils.forName(getInterfaces());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
