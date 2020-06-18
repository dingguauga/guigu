package com.stu.config;

import com.stu.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * bean生命周期：
 * 创建--初始化--销毁
 *
 *
 * 构造（对象创建）：
 *          单实例：容器启动的时候创建
 *          多实例：每次获取的时候创建对下
 *
 * postProcessBeforeInitialization

 * 初始化：
 *          对象创建完成，调用初始化
 *
 *  postProcessAfterInitialization
 * 销毁：
 *          单实例：容器关闭的时候
 *          多实例：容器不会调用销毁方法
 *
 * 容器管理bean的生命周期
 * 我们可以自定义初始化和销毁方法
 *
 * 1）、指定初始化和销毁方法
 *      通过@Bean指定initMethod和destroyMethod
 * 2)、让bean视线InitializingBean和DisposableBean
 *
 * 3)、@PostConstruct初始化  @PreDestroy
 *
 * 4）@BeanPostProcessor:bean后置处理器
 *      在bean初始化化前后进行一些处理工作
 *      1.postProcessBeforeInitialization
 *      2.postProcessAfterInitialization
 *
 *  如何读取源码？
 *  先看什么后看什么？
 */

@Configuration
@ComponentScan(basePackages = "com.stu")

public class MainConfigOfLifeCycle {
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }
}
