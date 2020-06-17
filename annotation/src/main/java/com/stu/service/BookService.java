package com.stu.service;

import com.stu.dao.BookDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    //给属性赋值,能赋值的就赋值不能就算了
    @Autowired(required = false)
    //明确指定需要装配的bookDao
    @Qualifier("bookDao")
    //和@Autowired是一样
//    @Inject
    BookDao bookDao;

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }
}
