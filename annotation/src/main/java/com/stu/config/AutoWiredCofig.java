package com.stu.config;

import com.stu.bean.Boss;
import com.stu.bean.Car;
import com.stu.dao.BookDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

/**
 * @Retention 定义注解的生命周期
 * @Documented
 * @Inherrited
 * @Target
 *
 *
 *
 *
 *
 *
 *
 *
 * 自动装配
 * spring利用依赖注入DI完成对IOC容器中各个组件的依赖关系赋值
 * 1)@Autowried自动注入
 *  a、默认优先按照类型去容器中找到对应的组件 applicationContext.getBean(BookService.class);
 *  b、根据id查找 applicationContext.getBean("bookDao");
 *  c、@Qualifier("bookDao")指定
 *  d、指定首选装配的bean
 *  2)
 * @Resource
 * @inject
 * 3）@Autowired 可以在方法 ，参数,构造方法
 */
@Configuration
//@ComponentScan({"com.stu.service","com.stu.dao","com.stu.controller"})
//@ComponentScan({"com.stu"})
@Import({Boss.class, Car.class})
public class AutoWiredCofig {
    //首选装配bookDao2
    @Primary
    @Bean("bookDao2")
    public BookDao bookDao(){
        BookDao bookDao = new BookDao();
        bookDao.setLabel("2");
        return  bookDao;
    }
}
