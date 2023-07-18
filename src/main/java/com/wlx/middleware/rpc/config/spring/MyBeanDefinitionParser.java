package com.wlx.middleware.rpc.config.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MyBeanDefinitionParser implements BeanDefinitionParser {

    private Class beanClass;

    public MyBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    // 解析spring-config.xml中的bean，并注册beanDefinition
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String beanName = element.getAttribute("id");
        parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);

        for (Method method : beanClass.getMethods()) {
            if (!isProperty(method, beanClass)) {
                continue;
            }
            // 添加bean的属性
            String methodName = method.getName();
            String propertyName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
            String propertyValue = element.getAttribute(propertyName);
            beanDefinition.getPropertyValues().addPropertyValue(propertyName, propertyValue);
        }
        return beanDefinition;
    }

    private boolean isProperty(Method method, Class<?> beanClass) {
        // 判断是否是set方法
        String methodName = method.getName();
        boolean isSetMethod = methodName.length() > 3 && methodName.startsWith("set") &&
                Modifier.isPublic(method.getModifiers()) && method.getParameters().length == 1;
        if (!isSetMethod) {
            return false;
        }

        // 找到get方法，判断set的入参和get的出参是否一致
        Class<?> type = method.getParameterTypes()[0];
        Method getMethod = null;
        try {
            getMethod = beanClass.getMethod("get" + methodName.substring(3));
            if (getMethod == null) {
                getMethod = beanClass.getMethod("is" + methodName.substring(3));
            }
        } catch (NoSuchMethodException e) {

        }

        if (getMethod == null) {
            return false;
        }

        if (!Modifier.isPublic(getMethod.getModifiers()) || !getMethod.getReturnType().equals(type)) {
            return false;
        }

        return true;
    }
}
