package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * @author huangjunbiao_cdv
 */
public class CloseCommand implements Command {
    private CloseReceiver closeReceiver;

    public CloseCommand(CloseReceiver closeReceiver) {
        this.closeReceiver = closeReceiver;
    }

    @Override
    public void execute() {
        closeReceiver.action();
    }

    @Override
    public void unDo() {
        closeReceiver.undo();
    }
}
