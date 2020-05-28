package com.stu.config;

import com.stu.bean.Color;
import com.stu.bean.ColorFactoryBean;
import com.stu.bean.Person;
import com.stu.bean.Red;
import com.stu.condition.LinuxCondition;
import com.stu.condition.MyImportBeanDefinitionRegistrar;
import com.stu.condition.MyImportSelector;
import com.stu.condition.WindowsCondition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

//瞒住当前条件，这个类中配置的所有bean注册才能生效
@Conditional({WindowsCondition.class})
@Configuration
@Import({Color.class, Red.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class MainConfig2 {
    //默认都是但实例

    /**
     * Specifies the name of the scope to use for the annotated component/bean.
     * <p>Defaults to an empty string ({@code ""}) which implies
     * {@link ConfigurableBeanFactory#SCOPE_SINGLETON SCOPE_SINGLETON}.
     *
     * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE 多实例  ioc容器启动并不会调用方法创建对象，而是每次获取bean时创建
     * @see ConfigurableBeanFactory#SCOPE_SINGLETON 单实例 ioc容器启动会调用方法创建对象放到ioc容器中,以后每次获取就是直接从容器中拿
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST 同一个请求
     * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION 同一个session
     * <p>
     * 懒加载：
     * 单实例bean:容器启动时创建
     * 懒加载：第一次使用时创建
     * @since 4.2
     */
    @Scope("prototype")
    @Bean("person2")
    public Person person() {
        System.out.println("给容器中添加Person");
        return new Person("张三", 26);
    }

    /**
     * @Conditional 按照一定条件进行判断满足条件注入bean
     * @Target({ElementType.TYPE, ElementType.METHOD})
     */

    @Bean("bill")
    public Person person01() {
        return new Person("bill", 65);
    }
    @Conditional({LinuxCondition.class})
    @Bean("linux")
    public Person person02() {
        return new Person("linux", 60);
    }
    /**
     *给容器中注册组件:
     * 1)、包扫描+组件标注（@Controller @Service等）
     * 2）、@Bean【导入第三方包】
     * 3）、@Import
     *      1.@Import({Color.class, Red.class})
     *      2.ImportSelector
     *      3.ImportBeanDefinitionRegistrar
     * 4）使用spring提供的FactoryBean
     */
    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }
}
