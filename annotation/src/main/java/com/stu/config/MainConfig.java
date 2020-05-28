package com.stu.config;

import com.stu.bean.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(value = "com.stu",
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes ={Controller.class} )},
        includeFilters = {@ComponentScan.Filter(type = FilterType.CUSTOM,classes={MyTypeFilter.class})}
        ,useDefaultFilters=false)


/**
 *excludeFilters:指定需要排除的注解
 * includeFilters：指定需要包含的注解
 * FilterType:
 *  1.ANNOTATION 按注解
 *  2.ASSIGNABLE_TYPE 按类型
 *  3.ASPECTJ
 *  4.CUSTOM使用自定义规则
 */
public class MainConfig {
    //给容器中注册一个bean类型为返回值的类型,id默认是用方法名作为id
    @Bean("personAlias")
    public Person person(){
        return new Person("lisi",20);
    }
}
