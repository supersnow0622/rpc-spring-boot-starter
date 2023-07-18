package com.wlx.middleware.rpc.config.spring.bean;

import com.wlx.middleware.rpc.config.ServerConfig;
import com.wlx.middleware.rpc.domain.LocalServerInfo;
import com.wlx.middleware.rpc.network.server.ServerSocket;
import com.wlx.middleware.rpc.registry.RedisRegistryCenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServerBean extends ServerConfig implements ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ServerBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.info("启动注册中心开始");
        RedisRegistryCenter.init(getHost(), getPort());
        logger.info("启动注册中心完成,{}:{}", getHost(), getPort());

        ServerSocket serverSocket = new ServerSocket(applicationContext);
        new Thread(serverSocket).start();

        if (!serverSocket.isActiveSocket()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        logger.info("启动服务端完成，{},{}", LocalServerInfo.LOCAL_HOST, LocalServerInfo.LOCAL_PORT);
    }
}
