package com.huang.cloudbase.learn.javadesignmode.mediator;

/**
 * 中介者模式
 * 通过中介者来调用具体实现
 *
 * @author huangjunbiao_cdv
 */
public class MediatorClient {
    public static void main(String[] args) {
        ChatPlatform chatPlatform = new ChatPlatform();
        NormalUser a = new NormalUser(chatPlatform, "A");
        NormalUser b = new NormalUser(chatPlatform, "B");
        NormalUser c = new NormalUser(chatPlatform, "C");
        a.join();
        b.join();
        c.join();
        System.out.println("-----------------A群发送消息------------------");
        a.sendToAll("A：大家听得到吗?");
        System.out.println("-----------------A给B私发消息------------------");
        a.sendToPerson("A：B,我只想和你说", "B");
        System.out.println("-----------------B给A私发消息------------------");
        b.sendToPerson("B:可以，请说", "A");
        System.out.println("-----------------A离开聊天室------------------");
        a.leave();
        System.out.println("-----------------B群发送消息------------------");
        b.sendToAll("B:A能听到吗");
    }
}
