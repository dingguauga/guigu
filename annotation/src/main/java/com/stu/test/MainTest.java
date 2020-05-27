package com.stu.test;

import com.stu.bean.Person;
import com.stu.config.MainConfig;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {
    @Test
   public void TestOriginalBean(){
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
        Person person = (Person) applicationContext.getBean("person");
        System.out.println(person);
    }

    @Test
    public void TestAnnotation(){
       ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);


        String[] beanNamesForType = applicationContext.getBeanDefinitionNames();
        for (String s : beanNamesForType) {
            System.out.println(s);
        }


    }

}
