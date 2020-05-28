package com.stu.bean;

import org.springframework.beans.factory.FactoryBean;

public class ColorFactoryBean implements FactoryBean<Color> {
    //返回的对象会加入到容器中
    public Color getObject() throws Exception {
        System.out.println("对象被注入ColorFactoryBean...........");
        return new Color();
    }

    public Class<?> getObjectType() {
        return null;
    }

    public boolean isSingleton() {
        return false;
    }
}
