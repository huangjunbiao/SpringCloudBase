package com.huang.cloudbase.learn.javadesignmode.event.demo;

/**
 * @author huangjunbiao_cdv
 */
public class Demo {
    DemoSource ds;

    public Demo() {
        try {
            ds = new DemoSource();
            //将监听器在事件源对象中登记：
            DemoListener1 listener1 = new DemoListener1();
            ds.addDemoListener(listener1);
            ds.addDemoListener(event -> System.out.println("Method come from 匿名类..."));
            ds.notifyDemoEvent();//触发事件、通知监听器
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        new Demo();
    }
}
