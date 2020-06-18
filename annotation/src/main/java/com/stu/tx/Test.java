package com.stu.tx;

import com.stu.tx.config.ExtConfig;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {
    @org.junit.Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExtConfig.class);
        //自定义发布一个事件
        applicationContext.publishEvent(new ApplicationEvent(new String("我发布了一个事件")) {

        });
        System.out.println("容器创建完成");

        applicationContext.close();

    }
}
