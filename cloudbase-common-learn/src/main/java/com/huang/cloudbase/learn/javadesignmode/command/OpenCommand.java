package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * @author huangjunbiao_cdv
 */
public class OpenCommand implements Command {
    private OpenReceiver openReceiver;

    public OpenCommand(OpenReceiver openReceiver) {
        this.openReceiver = openReceiver;
    }

    @Override
    public void execute() {
        openReceiver.action();
    }

    @Override
    public void unDo() {
        openReceiver.undo();
    }
}
