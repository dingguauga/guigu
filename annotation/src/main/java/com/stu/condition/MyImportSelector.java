package com.stu.condition;

        import org.springframework.context.annotation.ImportSelector;
        import org.springframework.core.type.AnnotationMetadata;

//自定义逻辑返回需要导入的组件
public class MyImportSelector implements ImportSelector {
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //返回全类名
        return new String[]{"com.stu.bean.Blue" ,"com.stu.bean.Yellow"};
    }
}
