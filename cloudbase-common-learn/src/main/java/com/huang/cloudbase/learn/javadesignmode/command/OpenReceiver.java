package com.huang.cloudbase.learn.javadesignmode.command;

/**
 * @author huangjunbiao_cdv
 */
public class OpenReceiver {

    public void action() {
        System.out.println("收到命令后开机");
    }

    public void undo() {
        System.out.println("撤销开机任务");
    }
}
