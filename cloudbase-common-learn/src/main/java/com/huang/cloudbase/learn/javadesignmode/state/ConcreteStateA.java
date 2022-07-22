package com.huang.cloudbase.learn.javadesignmode.state;

/**
 * 定义状态A
 *
 * @author huangjunbiao
 */
public class ConcreteStateA extends State {
    @Override
    public void handle1() {
        // 本状态下必须处理的事
        System.out.println("状态A doing");
    }

    @Override
    public void handle2() {
        // 切换到B状态
        super.context.setCurrentState(Context.concreteStateB);
        // 执行B任务
        super.context.handle2();
    }
}
