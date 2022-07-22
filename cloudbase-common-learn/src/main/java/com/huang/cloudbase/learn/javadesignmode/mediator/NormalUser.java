package com.huang.cloudbase.learn.javadesignmode.mediator;

/**
 * @author huangjunbiao_cdv
 */
public class NormalUser extends User {
    public NormalUser(Mediator mediator, String name) {
        super(mediator, name);
    }

    @Override
    void sendToAll(String msg) {
        mediator.sendToAll(msg);
    }

    @Override
    void sendToPerson(String msg, String name) {
        mediator.senToPerson(msg, name);
    }

    @Override
    void accept(String msg) {
        System.out.println(this.getName() + "收到消息" + msg);
    }

    @Override
    void join() {
        mediator.join(this);
    }

    @Override
    void leave() {
        mediator.leave(this);
    }
}
