package com.stu.tx.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 *1.环境配置导入 数据源、数据库驱动 spring-jdbc
 * 注解工作原理：
 *      @EnableTransactionManagement
 *          利用TransactionManagementConfigurationSelector导入组件
 *          导入两个组件
 *          1.AutoProxyRegistrar
 *              AnnotationAwareAspectJAutoProxyCreator
 *          2.ProxyTransactionManagementConfiguration
 */
@EnableTransactionManagement
@ComponentScan("com.stu.tx.*")
@Configuration
public class TxConfig {

    @Bean
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("root");
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        //test为数据库名
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        return dataSource;

    }
    @Bean
    public JdbcTemplate jdbcTemplate() throws PropertyVetoException {
        //spring对@Configuration会特殊处理，
        //给容器中加组件的方法，对此调用都只是从容器中找组件
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        return jdbcTemplate;
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws PropertyVetoException {
        return new DataSourceTransactionManager(dataSource());
    }
}
