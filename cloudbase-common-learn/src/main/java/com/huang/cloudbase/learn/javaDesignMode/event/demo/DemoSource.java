package com.huang.cloudbase.learn.javaDesignMode.event.demo;

import java.util.Enumeration;
import java.util.Vector;

/**
 * @author huangjunbiao_cdv
 */
public class DemoSource {
    private Vector repository = new Vector();//监听自己的监听器队列

    public DemoSource() {
    }

    public void addDemoListener(DemoListener dl) {
        repository.addElement(dl);
    }

    public void notifyDemoEvent() {//通知所有的监听器
        Enumeration elements = repository.elements();
        while (elements.hasMoreElements()) {
            DemoListener dl = (DemoListener) elements.nextElement();
            dl.handleEvent(new DemoEvent(this));
        }
    }
}
