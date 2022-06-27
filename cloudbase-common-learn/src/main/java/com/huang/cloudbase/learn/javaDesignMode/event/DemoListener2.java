package com.huang.cloudbase.learn.javaDesignMode.event;

/**
 * @author huangjunbiao_cdv
 */
public class DemoListener2 implements DemoListener  {
    @Override
    public void handleEvent(DemoEvent dm) {
        System.out.println("Inside listener12...");
        dm.say();//回调
    }
}
