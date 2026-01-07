package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标记需要自动填充字段的操作
 */
@Target(ElementType.METHOD) // 该注解用于方法上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时可用
public @interface AutoFill {
    OperationType value(); // 数据库操作类型， INSERT 或 UPDATE
}
