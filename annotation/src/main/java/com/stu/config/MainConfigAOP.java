package com.stu.config;

import com.stu.aop.LogAspects;
import com.stu.aop.MathCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 思路：
 * 有没有给容器注册一些组件
 * 组件什么时候工作
 * 注册的组件功能是什么
 * 原理：
 *      @EnableAspectJAutoProxy:
 *      1.EnableAspectJAutoProxy是什么？
 *          @Import(AspectJAutoProxyRegistrar.class)给容器中导入AspectJAutoProxyRegistrar
 *          利用AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar 自定义给容器中注册BeanDefinitionRegistry（bean信息的定义）
 *          internalAutoProxyCreator=AnnotationAwareAspectJAutoProxyCreator
 *          给容器中注册一个AnnotationAwareAspectJAutoProxyCreator（自动代理创建器）
 *       2、AnnotationAwareAspectJAutoProxyCreator的功能是什么？
 *              AnnotationAwareAspectJAutoProxyCreator
 *                  AspectJAwareAdvisorAutoProxyCreator
 *                      AbstractAdvisorAutoProxyCreator
 *                          AbstractAutoProxyCreator：
 *                               AbstractAutoProxyCreator extends ProxyProcessorSupport
 * 		                            implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
 * 		                            发现了一个后置处理器、和一个设置setBeanFactory的类
 * 		                            关注：后置处理器（在bean初始化前后做的事情）、自动注入BeanFactory
 * 		  3、SmartInstantiationAwareBeanPostProcessor、BeanFactoryAware的功能是什么？
 * 		    AbstractAutoProxyCreator.setBeanFactory()设置BeanFactoryAware的逻辑
 * 		    AbstractAutoProxyCreator.postProcessBeforeInstantiation()后置处理器的逻辑
 *
 * 		    AbstractAdvisorAutoProxyCreator.setBeanFactory()
 * 		    AbstractAdvisorAutoProxyCreator.setBeanFactory()=>AbstractAdvisorAutoProxyCreator.initBeanFactory()
 *
 * 		    AspectJAwareAdvisorAutoProxyCreator
 * 		    AnnotationAwareAspectJAutoProxyCreator.initBeanFactory()
 * 		    跟上述的所有方法打上断点已分析具体的功能是什么。
 * 流程：
 * 1、传入配置类创建ioc容器
 * 2、注册配置类、调用refresh()刷新容器
 * 3、Register bean processors that intercept bean creation.注册bean的后置处理器来拦截bean的创建
 * 				registerBeanPostProcessors(beanFactory);
 * 			那么registerBeanPostProcessors的处理逻辑是什么呢？
 * 		1.获取ioc容器已经定义了的需要创建对象的所有BeanPostProcessor
 * 	        String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
 * 	    2。给容器中加入别的BeanPostProcessor（addBeanPostProcessor）
 * 	    3.给postProcessor分组（PriorityOrdered，Ordered，其他）并注册
 * 	        a.First, register the BeanPostProcessors that implement PriorityOrdered.
 * 	             registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);
 * 	        b.Next, register the BeanPostProcessors that implement Ordered.
 * 	            registerBeanPostProcessors(beanFactory, orderedPostProcessors);
 * 	            由于AnnotationAwareAspectJAutoProxyCreator实现了Ordered所以进入
 * 	            那么registerBeanPostProcessors有什么功能呢？
 * 	            getBean=》doGetBean
 * 	            ❤❤❤第一次获取不到，就创建beanProceessor并保存到容器中
 * 	        c.Now, register all regular BeanPostProcessors.
 * 	            registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);
 * 	      4.注册BeanPostProcessor时间上就是创建BeanPostProcessor对象并存在容器中
 * 	      创建internalAutoProxyCreator的BeanPostProcessor[AnnotationAwareAspectJAutoProxyCreator]
 *          1.创建bean实例createBeanInstance
 *          2.给bean赋值populateBean
 *          3.初始化bean  initializeBean
 *              BeanPostProcessor就是在bean初始化前后工作的所以需要重点看
 *                  1.invokeAwareMethods :处理Aware接口的方法回调（BeanFactoryAware）
 *                      initBeanFactory
 *                  2.applyBeanPostProcessorsBeforeInitialization：应用后置处理器前置方法
 *                  3.invokeInitMethods执行自定义的初始化方法
 *                  4.applyBeanPostProcessorsAfterInitialization：应用后置处理器后置方法
 *                  5.BeanPostProcessor(AnnotationAwareAspectJAutoProxyCreator)创建成功
 *          5.把BeanPostProcessor注册到BeanFactory中
 *          beanFactory.addBeanPostProcessor
 *===================================以上是创建和注册AnnotationAwareAspectJAutoProxyCreator的过程========================
 *
 *
 */
@EnableAspectJAutoProxy
@Configuration
public class MainConfigAOP {
    //业务逻辑
    @Bean
    public MathCalculator calculator() {
        return new MathCalculator();
    }
    //切面类
    @Bean
    public LogAspects logAspects() {
        return new LogAspects();
    }
}
