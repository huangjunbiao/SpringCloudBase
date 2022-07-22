package com.huang.cloudbase.learn.javadesignmode.state;

/**
 * 状态模式执行
 * <p>
 * 当一个对象内在状态改变时允许其改变行为，这个对象看起来像改变了其类。
 *
 * @author huangjunbiao
 */
public class Client {
    public static void main(String[] args) {
        Context context = new Context();
        context.setCurrentState(new ConcreteStateA());
        context.handle1();
        context.handle2();
    }
}
