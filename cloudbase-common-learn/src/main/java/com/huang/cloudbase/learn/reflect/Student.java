package com.huang.cloudbase.learn.reflect;

/**
 *  used for reflect
 *
 * @author huangjunbiao
 */
public class Student {
    private String name;

    public int age;

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void handleName(String name) {
        this.name = name;
        System.out.println(this.name);
    }
}
