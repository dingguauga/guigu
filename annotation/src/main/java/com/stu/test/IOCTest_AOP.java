package com.stu.test;

import com.stu.aop.MathCalculator;
import com.stu.config.MainConfigAOP;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * AOP测试类
 */
public class IOCTest_AOP {
    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigAOP.class);

        System.out.println("容器创建完成");
        MathCalculator bean = applicationContext.getBean(MathCalculator.class);
        bean.div(1,1);
        applicationContext.close();

    }
}
