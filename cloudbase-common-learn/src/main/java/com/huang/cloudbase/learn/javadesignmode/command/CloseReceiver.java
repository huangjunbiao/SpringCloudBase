package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * @author huangjunbiao_cdv
 */
public class CloseReceiver {
    public void action() {
        System.out.println("收到命令后关闭");
    }

    public void undo() {
        System.out.println("撤销关闭任务");
    }
}
