package com.stu.test;

import com.stu.config.MainCofigOfLifeCycle;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class IOCTest_Tx {
    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainCofigOfLifeCycle.class);

        System.out.println("容器创建完成");
        Object car = applicationContext.getBean("car");
        applicationContext.close();

    }
}
