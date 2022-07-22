package com.huang.cloudbase.learn.javadesignmode.state;

/**
 * 状态B
 *
 * @author huangjunbiao
 */
public class ConcreteStateB extends State {
    @Override
    public void handle1() {
        super.context.setCurrentState(Context.concreteStateA);
        super.context.handle1();
    }

    @Override
    public void handle2() {
        System.out.println("状态B doing");
    }
}
