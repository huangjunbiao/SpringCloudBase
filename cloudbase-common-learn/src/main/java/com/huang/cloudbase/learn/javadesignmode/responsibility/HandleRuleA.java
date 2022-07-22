package com.huang.cloudbase.learn.javadesignmode.responsibility;

/**
 * 具体的handleA
 *
 * @author huangjunbiao
 */
public class HandleRuleA extends Handler{
    public HandleRuleA(int level) {
        super(level);
    }

    @Override
    public void echo(Request request) {
        System.out.println("处理者1：正在处理A规则。。。");
    }
}
