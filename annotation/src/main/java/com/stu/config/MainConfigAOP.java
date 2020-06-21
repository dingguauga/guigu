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
 * AnnotationAwareAspectJAutoProxyCreator是一个InstantiationAwareBeanPostProcessor会在r任何bean创建之前先尝试返回bean的实例
 *
 * 4.finishBeanFactoryInitialization完成BeanFactory初始化工作；创建剩下的单实例
 *      1.遍历获取容器中所有的Bean。依次创建对象getBean（beanName）
 *      2.创建bean
 *          1）、先从缓存获取bean如果存在则不创建
 *          2）createBean():创建bean，AnnotationAwareAspectJAutoProxyCreator会在r任何bean创建之前先尝试返回bean的实例
 *          【BeanPostProcessor是在Bean对象创建完成初始化前后调用的】
 *          【InstantiationAwareBeanPostProcessor是在创建bean实例之前先尝试用后置处理器返回对象的】
 *              1)、resolveBeforeInstantiation()
 *                  1）、后置处理器尝试先返回对象
 *                      bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 *                      拿到后置处理器，如果是InstantiationAwareBeanPostProcessor就执行postProcessBeforeInstantiation(beanClass, beanName);
 *
 *                      if (bean != null) {
 * 						bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 *                                        }
 *              Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
 *              希望后置处理器能返回一个代理对象，如果能返回代理对象就直接返回
 *              2）、doCreateBean(beanName, mbdToUse, args);不能返回就创建（真正的创建一个bean实例）
 *                  1）createBeanInstance(beanName, mbd, args)
 *                  2）populateBean(beanName, mbd, instanceWrapper); Initialize the bean instance.
 *                  3）initializeBean(beanName, exposedObject, mbd);
 *                      1）invokeAwareMethods(beanName, bean);
 *                      2）applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
 *                      3）invokeInitMethods(beanName, wrappedBean, mbd);
 *                      4）applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
 *              3)、
 *              结论：由于AnnotationAwareAspectJAutoProxyCreator是一个InstantiationAwareBeanPostProcessor
 *              而InstantiationAwareBeanPostProcessor会在任何bean创建之前先尝试返回bean的实例
 *=============================================以上是AnnotationAwareAspectJAutoProxyCreator的执行时机=====================
 * AnnotationAwareAspectJAutoProxyCreator【InstantiationAwareBeanPostProcessor】的作用
 *
 * 1）在每一个bean创建之前，调用postProcessBeforeInstantiation()
 *      查看MathCalculator、LogAspects这两个bean在postProcessBeforeInstantiation里面做了什么
 *      1）、判断当前bean是否在advisedBeans中（保存了需要增强的bean）
 *      if (this.advisedBeans.containsKey(cacheKey)) {
 * 				return null;
 *                        }
 *      2)isInfrastructureClass(beanClass)包含了两个判断，第一判断当前bean是否是基础类型 Advice、Pointcut、Advisor、AopInfrastructureBean；第二是否是切面@Aspect
 *      if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
 * 				this.advisedBeans.put(cacheKey, Boolean.FALSE);
 * 				return null;
 *                        }
 *      3）shouldSkip(beanClass, beanName)判断是否需要跳过
 *          // TODO: Consider optimization by caching the list of the aspect names
 *          考虑通过缓存方面名称列表进行优化
 *         1） List<Advisor> candidateAdvisors = findCandidateAdvisors();获取增强器（切面里面的方法） InstantiationModelAwarePointcutAdvisor: expression [pointCut()]; advice method [public void com.stu.aop.LogAspects.logException(java.lang.Exception)]; perClauseKind=SINGLETON
 *          增强器的类型是InstantiationModelAwarePointcutAdvisor
 *          判断每一个增强器是否是AspectJPointcutAdvisor类型，返回true
 *          2）super.shouldSkip(beanClass, beanName); 否则调用父类返回false(父类直接返回false)
 *2)、创建对象
 * postProcessAfterInitialization
 *      1）、wrapIfNecessary(bean, beanName, cacheKey);包装如果需要的情况
 *      什么情况需要包装呢？
 *
 *          1）、获取当前bean的所有增强器（通知方法）
 *          // Create proxy if we have advice.
 * 		    Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
 * 		        1.findCandidateAdvisors()找到候选的所有增强器(找到那些通知方法是需要切入当前bean)
 * 		        2.findAdvisorsThatCanApply获取能在当前bean使用的增强器
 * 		        if (canApply(candidate, clazz, hasIntroductions)) {
 * 				eligibleAdvisors.add(candidate);
 *                        }
 * 		        3.sortAdvisors(eligibleAdvisors)给增强器排序
 * 		        specificInterceptors具体的拦截器
 * 		 2）、保存当前bean在advisedBeans
 * 		        this.advisedBeans.put(cacheKey, Boolean.TRUE);
 * 		 3）、createProxy如果当前bean需要增强、创建当前bean的代理对象
 * 		        1）、获取所有的增强方法
 * 		        Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
 * 		        2）保存到代理工厂
 * 		        proxyFactory.addAdvisors(advisors);
 * 		        3）用代理工厂创建对象（spring自动决定看是否实现接口,默认ObjenesisCglibAopProxy）
 * 		        proxyFactory.getProxy(getProxyClassLoader());=>createAopProxy().getProxy(classLoader);=》getAopProxyFactory().createAopProxy(this);
 * 		         return new JdkDynamicAopProxy(config);
 * 		         return new ObjenesisCglibAopProxy(config);
 *        4）给容器中返回当前组件使用cglib增强了的代理对象
 *        5）以后容器中获取的就是组件的代理对象、执行目标方法的时候，代理对象就会执行通知方法的流程
 * =========================================================bean创建后postProcessAfterInitialization的执行流程======================
 * 代理对象执行方法的流程:
 *给 bean.div(1,1);打上断点
 * 容器中保存了组件的代理对象(cglib对象)，这个对象保存了详细信息（比如增强器、目标对象）
 *  CglibAopProxy.intercept()
 *   1)根据proxyFactory获取将要执行目标方法的拦截器链
 *   List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass);
 *      1）保存所有拦截器，长度为config.getAdvisors().length（增强方法的个数4+1自带的）
 *      List<Object> interceptorList = new ArrayList<Object>(config.getAdvisors().length);
 *      一个默认的org.springframework.aop.interceptor.ExposeInvocationInterceptor.ADVISOR
 *      四个自己的：
 *          1 = {InstantiationModelAwarePointcutAdvisorImpl@2098} "InstantiationModelAwarePointcutAdvisor: expression [pointCut()]; advice method [public void com.stu.aop.LogAspects.logException(java.lang.Exception)]; perClauseKind=SINGLETON"
 *          2 = {InstantiationModelAwarePointcutAdvisorImpl@2099} "InstantiationModelAwarePointcutAdvisor: expression [pointCut()]; advice method [public void com.stu.aop.LogAspects.logRrturn(java.lang.Object)]; perClauseKind=SINGLETON"
 *          3 = {InstantiationModelAwarePointcutAdvisorImpl@2100} "InstantiationModelAwarePointcutAdvisor: expression [pointCut()]; advice method [public void com.stu.aop.LogAspects.logEnd()]; perClauseKind=SINGLETON"
 *          4 = {InstantiationModelAwarePointcutAdvisorImpl@2101} "InstantiationModelAwarePointcutAdvisor: expression [pointCut()]; advice method [public void com.stu.aop.LogAspects.logStart(org.aspectj.lang.JoinPoint)]; perClauseKind=SINGLETON"
 *      2)遍历所有的增强器将其转为Interceptor（拦截器）
 *          Interceptor[] interceptors = registry.getInterceptors(advisor);
 *          1）判断是否为Interceptor
 *              是：直接加入
 *              否:判断是否支持适配器，支持通过适配器转换为Interceptor在加入，否则不做处理
 *              适配器处理逻辑：
 *              AfterReturningAdvice advice = (AfterReturningAdvice) advisor.getAdvice();
 * 		        return new AfterReturningAdviceInterceptor(advice);
 *          2)最后转为数组返回
 *  2）如果没有拦截器链，直接执行目标方法
 *   We can skip creating a MethodInvocation: just invoke the target directly.
 *  拦截器链（每个一个通知方法被包装为方法拦截器，利用MethodInterceptor机制）
 *   3）如果有拦截器链
 *   retVal = new CglibMethodInvocation(proxy, target, method, args, targetClass, chain, methodProxy).proceed();
 *   把需要执行的目标对象，目标方法、拦截器年等信息传入创建CglibMethodInvocation并调用proceed()方法，
 *   4）拦截器链的触发过程
 *   private int currentInterceptorIndex = -1;记录当前拦截器的索引
 *  this.interceptorsAndDynamicMethodMatchers 连接器链
 *      1）判断拦截器链与索引值是否相等，是就直接执行目标方法
 *      （如果没有拦截器，或者拦截器执行到最后一个会触发）
 *      if (this.currentInterceptorIndex == this.interceptorsAndDynamicMethodMatchers.size() - 1) {
 * 			return invokeJoinpoint();
 *                }
 *        拦截器：
 *        第一次调用ExposeInvocationInterceptor： 这个有什么作用呢？
 *        mi.proceed();
 *
 *        AspectJAfterThrowingAdvice异常处理
 *         try {
 * 			return mi.proceed();
 *                }
 * 		catch (Throwable ex) {
 * 			if (shouldInvokeOnThrowing(ex)) {
 * 		       执行异常通知
 * 				invokeAdviceMethod(getJoinPointMatch(), null, ex);
 *            }
 * 			throw ex;
 *        }
 *
 *        AfterReturningAdviceInterceptor返回通知
 *          Object retVal = mi.proceed();
 * 		    this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
 * 		    return retVal;
 *
 *        AspectJAfterAdvice后置通知
 *          try {
 * 			return mi.proceed();
 *                }
 * 		    finally {
 * 		        执行后置通知？？？
 * 			    invokeAdviceMethod(getJoinPointMatch(), null, null);
 *              }
 *
 *        MethodBeforeAdviceInterceptor
 *          调用前置通知
 *          this.advice.before(mi.getMethod(), mi.getArguments(), mi.getThis() );
 * 		    return mi.proceed();
 *
 * 		   最后执行目标方法： invokeJoinpoint();利用反射执行目标方法
 *1）链式获取每一个拦截器，拦截器执行invoke方法，每一个拦截器等待下一个拦截执行完成返回后再来执行
 * 拦截器链的机制来保证通知方法和目标方法的执行顺序。
 *
 *
 * 总结：
 *      1）@EnableAspectJAutoProxy开启AOP功能
 *      2）@EnableAspectJAutoProxy会给容器注册一个组件AnnotationAwareAspectJAutoProxyCreator
 *      3）AnnotationAwareAspectJAutoProxyCreator是一个后置处理器
 *      4）容器的创建流程
 *          1）// Register bean processors that intercept bean creation.
 * 				registerBeanPostProcessors(beanFactory);注册后置处理器，创建AnnotationAwareAspectJAutoProxyCreator
 * 			2）// Instantiate all remaining (non-lazy-init) singletons.
 * 				finishBeanFactoryInitialization(beanFactory);初始化剩下的单实例bean
 * 			   1）创建业务逻辑组件MathCalculator和切面组件LogAspects
 * 			   2）AnnotationAwareAspectJAutoProxyCreator拦截组件的创建过程
 * 			   3）组件创建完成判断组件是否需要增强wrapIfNecessary
 * 			        是：切面的通知方法，包装成增强器(Advisor),给业务逻辑组件创建一个动态代理对象（cglib）
 * 		5)执行目标方法：
 * 	        1）代理对象执行目标方法
 * 	        2）CglibAopProxy.intercept()
 * 	            1）得到目标方法的拦截器链（增强器包Advisor装成拦截器MethodInterceptor机制）
 * 	            2）利用拦截器的链式机制，依次进入每一个拦截器进行执行
 * 	            3）效果：
 * 	                前置通知=>目标方法=>后置通知=>返回通知
 * 	                前置通知=>目标方法=>后置通知=>异常通知
 * 	     后面多自己练习走几遍
 *
 *
 *
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
