package com.stu.tx.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 只要容器中有事件发布监听器就会回调
 */
@Component
public class MyApplicationListener implements ApplicationListener {
    //当容器中发布事件就会触发
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("onApplicationEvent"+event.getClass()+event.getSource());
    }
}
