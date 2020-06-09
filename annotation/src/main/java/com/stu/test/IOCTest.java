package com.stu.test;

import com.stu.bean.Blue;
import com.stu.bean.Person;
import com.stu.config.MainConfig;
import com.stu.config.MainConfig2;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class IOCTest {

    @Test
    public void test01(){
        //IOC容器 测试一下
       ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
    }


    @Test
    public void test02(){
        //IOC容器
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        Object person = applicationContext.getBean("person2");
        Object person2 = applicationContext.getBean("person2");
        //默认单实例
        System.out.println(person==person2);
    }

    @Test
    public void test03(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String s : beanNamesForType) {
            System.out.println(s);
        }

    }
    @Test
    public void testImport(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        Blue bean = applicationContext.getBean(Blue.class);
        System.out.println(bean);

    }

    @Test
    public void testFactory(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }
        Object colorFactoryBean = applicationContext.getBean("colorFactoryBean");
        //	/**
        //	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
        //	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
        //	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
        //	 * will return the factory, not the instance returned by the factory.
        //	 */
        //	String FACTORY_BEAN_PREFIX = "&";
        Object colorFactoryBean2 = applicationContext.getBean("&colorFactoryBean");
        System.out.println(colorFactoryBean);
        System.out.println(colorFactoryBean2);

    }
}
