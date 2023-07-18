package com.wlx.middleware.rpc.config;

public class ConsumerConfig {

    // 接口
    private String interfaces;

    // 别名
    private String alias;

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
