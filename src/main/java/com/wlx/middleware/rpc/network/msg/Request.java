package com.wlx.middleware.rpc.network.msg;

import io.netty.channel.Channel;

import java.util.UUID;

public class Request {

    private transient Channel channel;

    private String requestId;
    // 方法名
    private String methodName;
    // 参数类型
    private Class[] paramTypes;
    // 参数
    private Object[] args;
    // 接口
    private String interfaces;
    // 实现对象名
    private String ref;
    // 别名
    private String alias;

    public Request() {
        requestId = UUID.randomUUID().toString();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
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
