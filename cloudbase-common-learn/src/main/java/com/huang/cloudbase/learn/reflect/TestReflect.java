package com.huang.cloudbase.learn.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author huangjunbiao
 */
public class TestReflect {

    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        Class<Student> studentClass = Student.class;
        Constructor<?>[] declaredConstructors = studentClass.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            System.out.println(constructor);
        }

        // 根据名称获得一个变量
        Field name = studentClass.getDeclaredField("name");
        // 根据名称获得public成员变量
        Field age = studentClass.getField("age");


        // 获得public方法
        Method[] methods = studentClass.getMethods();

        // 获得所有方法
        Method[] declaredMethods = studentClass.getDeclaredMethods();

        // 获取特定方法
        Method handleName = studentClass.getDeclaredMethod("handleName", String.class);
    }
}
