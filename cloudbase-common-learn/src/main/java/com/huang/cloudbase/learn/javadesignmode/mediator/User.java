package com.huang.cloudbase.learn.javadesignmode.mediator;

/**
 * @author huangjunbiao_cdv
 */
public abstract class User {
    protected Mediator mediator;
    /**
     * name
     */
    private String name;

    public User(Mediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }

    abstract void sendToAll(String msg);

    abstract void sendToPerson(String msg, String name);

    abstract void accept(String msg);

    abstract void join();

    abstract void leave();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
