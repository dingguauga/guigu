package com.stu.tx.config;

import com.stu.bean.Blue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * 事件监听步骤：
 *      1.丛谢ApplicationListener
 *      2.加入容器
 * 如何发布一个事件？
 * applicationContext.publishEvent
 * 原理：
 *      创建一个派发器
 *      将所有的监听器加入到派发器中
 *
 */
@ComponentScan("com.stu.tx.*")
@Configuration
public class ExtConfig {
    @Bean
    public Blue blue(){
        return new Blue();
    }
}
