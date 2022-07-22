package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * @author huangjunbiao_cdv
 */
public class Invoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void action() {
        command.execute();
    }

    public void undo() {
        command.unDo();
    }
}
