package com.wlx.middleware.rpc.config;

public class ProviderConfig {

    // 接口
    private String interfaces;

    // 映射名
    private String ref;

    // 别名
    private String alias;

    protected void doExport() {
        System.out.format("生产者信息=> [接口:%s][映射:%s][别名:%s]", interfaces, ref, alias);
    }

    public String getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String interfaces) {
        this.interfaces = interfaces;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
