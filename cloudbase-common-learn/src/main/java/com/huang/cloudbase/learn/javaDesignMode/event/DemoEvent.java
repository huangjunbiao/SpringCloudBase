package com.huang.cloudbase.learn.javaDesignMode.event;

import java.util.EventObject;

/**
 * @author huangjunbiao_cdv
 */
public class DemoEvent extends EventObject {
    public DemoEvent(Object source) {
        super(source);//source—事件源对象—如在界面上发生的点击按钮事件中的按钮
        //所有 Event 在构造时都引用了对象 "source"，在逻辑上认为该对象是最初发生有关 Event 的对象
    }
    public void say() {
        System.out.println("This is say method...");
    }
}
