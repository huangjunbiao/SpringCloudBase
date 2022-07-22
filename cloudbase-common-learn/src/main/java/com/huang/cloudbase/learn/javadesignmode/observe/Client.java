package com.huang.cloudbase.learn.javadesignmode.observe;

/**
 * @author huangjunbiao_cdv
 */
public class Client implements Observer {
    String callNo;

    public Client(String callNo) {
        this.callNo = callNo;
    }

    @Override
    public void update(String msg) {
        if (msg.equals(this.callNo)) {
            System.out.println("我是" + callNo + "号客户,现在到我了！");
        } else {
            System.out.println("我是" + callNo + "号客户,现在还没到我了！");
        }
    }
}
