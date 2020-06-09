package com.stu.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * z切面类
 */
@Aspect
public class LogAspects {
    //提取公共的切入点表达式
    @Pointcut("execution(public int com.stu.aop.MathCalculator.*(..))")
    public void pointCut(){};

    @Before(value = "pointCut()")
    public void logStart(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature()+"除法运行。。。。参数列表是：{"+ Arrays.asList(joinPoint.getArgs()) +"}");
    }
    @After(value = "pointCut()")
    public void logEnd(){
        System.out.println("除法结束。。。。");
    }
    @AfterReturning(value = "pointCut()",returning = "result")
    public void logRrturn(Object result){
        System.out.println("除法正常返回。。。。运行结果：{"+result+"}");
    }
    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logException(Exception exception){
        System.out.println("除法异常。。。异常信息：{"+exception+"}");
    }
}
