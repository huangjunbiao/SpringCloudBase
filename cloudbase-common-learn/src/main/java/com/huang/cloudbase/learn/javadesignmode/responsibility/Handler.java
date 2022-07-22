package com.huang.cloudbase.learn.javadesignmode.responsibility;

/**
 * 抽象handler
 *
 * @author huangjunbiao
 */
public abstract class Handler {
    // 指向下一个handler
    private Handler nextHandler;
    // 处理者能够处理的级别
    private int level;

    public Handler(int level) {
        this.level = level;
    }

    public void setNextHandler(Handler handler) {
        this.nextHandler = handler;
    }

    // 处理请求传递，子类不可重写
    public final void handleMessage(Request request) {
        if (level == request.getRequestLevel()) {
            this.echo(request);
        } else {
            if (this.nextHandler != null) {
                this.nextHandler.handleMessage(request);
            } else {
                System.out.println("进行到最后");
            }
        }
    }

    public abstract void echo(Request request);
}
