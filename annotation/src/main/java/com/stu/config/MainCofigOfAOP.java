package com.stu.config;

import com.stu.aop.LogAspects;
import com.stu.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 *AOP:动态代理
 *      指在程序运行期间动态将某段代码切入到指定方法的指定位置进行运行的编程方式
 *
 * 1.业务处理类MathCalculatore
 * 2.日志切面类
 *      通知方法：
 *          前置通知：
 *          后置通知：
 *          返回通知：
 *          异常通知：
 *          环绕通知：
 * 使用AOP的步骤：
 *      1.用@Aspect标注切面类，并注入到spring容器中
 *      2.在切面类的方法上标注@Before(value = "pointCut()")以表示方法何时运行
 *      3.加入EnableAspectJAutoProxy注解
 * AOP原理：【1.看给容器注册了什么组件
 *           2.这个组件什么时候工作
 *           3.这个组件的功能是什么
 *           】
 *      @EnableAspectJAutoProxy
 *      1.向容器中注入了一个组件AnnotationAwareAspectJAutoProxyCreator
 *      2.AnnotationAwareAspectJAutoProxyCreator这个组件有什么作用。通过继承关系发现
 *       AbstractAutoProxyCreator extends ProxyProcessorSupport
 * 		implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 * 	有一个后置处理器，一个BeanFactoryAware工厂注入
 * 	setBeanFactory
 * 	postProcessBeforeInstantiation
 */
@Configuration
@EnableAspectJAutoProxy
public class MainCofigOfAOP {
    @Bean
    public LogAspects getLogAspects(){
        return new LogAspects();
    }
    @Bean
    public MathCalculator getMathCalculator(){
        return new MathCalculator();
    }
}
