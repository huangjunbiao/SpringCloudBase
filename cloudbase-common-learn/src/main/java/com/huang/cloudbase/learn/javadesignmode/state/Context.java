package com.huang.cloudbase.learn.javadesignmode.state;

/**
 * 上下文管理对象
 *
 * @author huangjunbiao
 */
public class Context {
    public final static ConcreteStateA concreteStateA = new ConcreteStateA();

    public final static ConcreteStateB concreteStateB = new ConcreteStateB();

    private State currentState;

    public void handle1() {
        this.currentState.handle1();
    }

    public void handle2() {
        this.currentState.handle2();
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
        this.currentState.setContext(this);
    }
}
