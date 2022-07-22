package com.huang.cloudbase.learn.javadesignmode.state;


/**
 * 状态抽象类
 *
 * @author huangjunbiao
 */
public abstract class State {
    Context context;

    public abstract void handle1();

    public abstract void handle2();

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
