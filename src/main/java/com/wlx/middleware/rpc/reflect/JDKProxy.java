package com.wlx.middleware.rpc.reflect;

import com.wlx.middleware.rpc.network.future.SyncWrite;
import com.wlx.middleware.rpc.network.msg.Request;
import com.wlx.middleware.rpc.network.msg.Response;
import com.wlx.middleware.rpc.utils.ClassLoaderUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxy implements InvocationHandler {

    private Class<?>[] interfaces;

    private Request request;

    public JDKProxy(Class<?>[] interfaces, Request request) {
        this.interfaces = interfaces;
        this.request = request;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (methodName.equals("toString") && parameterTypes.length == 0) {
            return request.toString();
        }
        if (methodName.equals("hashCode") && parameterTypes.length == 0) {
            return request.hashCode();
        }
        if (methodName.equals("equals") && parameterTypes.length == 1) {
            return request.equals(args[0]);
        }

        request.setMethodName(methodName);
        request.setParamTypes(parameterTypes);
        request.setArgs(args);

        // 发送请求
        Response response = new SyncWrite().writeAndFlush(request.getChannel(), request, 1000);
        return response.getResult();
    }

    public Proxy getProxy() {
        return (Proxy) Proxy.newProxyInstance(ClassLoaderUtils.getCurrentClassLoader(), interfaces, this);
    }
}
