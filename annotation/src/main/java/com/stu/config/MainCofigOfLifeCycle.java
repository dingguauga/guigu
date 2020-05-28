package com.stu.config;

import com.stu.bean.Car;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * bean生命周期：
 * 创建--初始化--销毁
 *
 *
 * 创建：
 *          单实例：容器启动的时候创建
 *          多实例：每次获取的时候创建对下
 * 初始化：
 *          对象创建完成，调用初始化
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
 *
 */
@Configuration
@Import({Car.class})
public class MainCofigOfLifeCycle {
    @Bean(initMethod = "init",destroyMethod = "destroy")
    public Car car(){
        return new Car();
    }
}
