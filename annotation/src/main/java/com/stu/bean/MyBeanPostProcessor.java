package com.stu.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 后置处理器：初始化前后进行工作
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    /**
     *
     * @param bean 刚创建的bean实例
     * @param beanName bean实例的名字
     * @return 原始的bean或者包装过后的bean
     * @throws BeansException
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization ..."+beanName);
        return bean;
    }

    /**
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization ..."+beanName);
        return bean;
    }
}
