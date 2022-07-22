package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * 命令模式
 *
 * Invoker调用者角色：接收到命令，并执行命令
 * Command命令角色：需要执行的所有命令都在这里声明
 * Receiver接受者角色：该角色就是干活的角色，命令传递到这里是应该被执行的
 *
 * @author huangjunbiao_cdv
 */
public class CommandClient {

    public static void main(String[] args) {
        Invoker invoker = new Invoker();
        invoker.setCommand(new OpenCommand(new OpenReceiver()));
        invoker.action();
        invoker.setCommand(new CloseCommand(new CloseReceiver()));
        invoker.action();
        invoker.undo();
    }
}
