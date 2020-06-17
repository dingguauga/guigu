package com.stu.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Boss {
   private Car car;

    public Car getCar() {
        return car;
    }
    //创建对象就会完成赋值
    //方法使用的参数，自定义类型的值从ioc容器中获取
    @Autowired
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }
}

