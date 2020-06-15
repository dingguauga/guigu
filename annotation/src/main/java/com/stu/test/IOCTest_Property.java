package com.stu.test;

import com.stu.bean.Person;
import com.stu.config.PropertyConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class IOCTest_Property {
    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PropertyConfig.class);

        System.out.println("容器创建完成");
        Person person = (Person)applicationContext.getBean("person");
        System.out.println(person);

//        applicationContext.close();

    }
}
