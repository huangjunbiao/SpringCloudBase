package com.huang.cloudbase.learn.javadesignmode.responsibility;

/**
 * @author huangjunbiao
 */
public class HandleRuleB extends Handler {
    public HandleRuleB(int level) {
        super(level);
    }

    @Override
    public void echo(Request request) {
        System.out.println("处理者2：正在处理B规则。。。");
    }
}
