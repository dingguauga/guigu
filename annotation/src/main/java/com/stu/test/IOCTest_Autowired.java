package com.stu.test;

import com.stu.bean.Boss;
import com.stu.bean.Car;
import com.stu.config.AutoWiredCofig;
import com.stu.dao.BookDao;
import com.stu.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class IOCTest_Autowired {
    @Test
    public void test(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutoWiredCofig.class);

        System.out.println("容器创建完成");
        BookService bookService = applicationContext.getBean(BookService.class);
        System.out.println(bookService);
        BookDao bookDao = applicationContext.getBean(BookDao.class);
        System.out.println(bookDao);


        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);
        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);


        applicationContext.close();

    }


    @Test
    public void test1(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AutoWiredCofig.class);


        //测试在set方法上加@AutoWried
        Boss boss = applicationContext.getBean(Boss.class);
        System.out.println(boss);
        Car car = applicationContext.getBean(Car.class);
        System.out.println(car);


        applicationContext.close();

    }
}
