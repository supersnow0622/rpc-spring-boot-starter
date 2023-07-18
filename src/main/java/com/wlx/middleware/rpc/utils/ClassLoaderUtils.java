package com.wlx.middleware.rpc.utils;

public class ClassLoaderUtils {

    public static ClassLoader getCurrentClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassLoaderUtils.class.getClassLoader();
        }
        return classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
    }

    public static Class forName(String className) throws ClassNotFoundException {
        return Class.forName(className, true, getCurrentClassLoader());
    }

}
