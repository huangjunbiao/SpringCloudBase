package com.huang.cloudbase.learn.javadesignmode.observe;

/**
 * @author huangjunbiao_cdv
 */
public class ObserveClient {
    public static void main(String[] args) {
        BankCallCenter bankCallCenter = new BankCallCenter();
        Client client = new Client("1");
        bankCallCenter.registerObserver(client);
        Client client2 = new Client("2");
        bankCallCenter.registerObserver(client2);
        Client client3 = new Client("3");
        bankCallCenter.registerObserver(client3);
        System.out.println("----------中心叫一号去办理业务------------");
        bankCallCenter.notifyObserver("1");
        System.out.println("----------从观察者列表中移除一号------------");
        bankCallCenter.removeObserver(client);
        System.out.println("-----------中心叫二号去办理业务-------------");
        bankCallCenter.notifyObserver("2");
        System.out.println("------------从观察者列表中移除二号-----------");
    }
}
