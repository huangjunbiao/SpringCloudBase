package com.huang.cloudbase.learn.javaDesignMode.event;

import java.util.EventListener;

/**
 * @author huangjunbiao_cdv
 */
public interface DemoListener extends EventListener {
    //EventListener是所有事件侦听器接口必须扩展的标记接口、因为它是无内容的标记接口、
    //所以事件处理方法由我们自己声明如下：
    public void handleEvent(DemoEvent dm);
}
