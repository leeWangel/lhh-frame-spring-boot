package com.lhh.base.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统日志记录的设置
 * 默认新增删除修改都需记录
 * @author hwaggLee
 * 2017年7月20日 下午12:39:19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceLogSet {
    boolean add() default true;
    boolean delete() default true;
    boolean update() default true;
}
