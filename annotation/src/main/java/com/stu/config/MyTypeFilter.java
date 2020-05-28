package com.stu.config;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * 自定义过滤规则
 */
public class MyTypeFilter implements TypeFilter {
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        metadataReader.getAnnotationMetadata();
        String className = metadataReader.getClassMetadata().getClassName();
        if(className.contains("er")){
            return true;
        }

        return false;
    }
}
