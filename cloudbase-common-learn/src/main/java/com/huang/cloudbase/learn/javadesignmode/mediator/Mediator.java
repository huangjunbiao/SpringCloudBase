package com.huang.cloudbase.learn.javadesignmode.mediator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangjunbiao_cdv
 */
public abstract class Mediator {
    List<User> list = new ArrayList<>();

    abstract void sendToAll(String msg);//群发

    abstract void senToPerson(String msg, String name);//给某个人发送消息

    abstract void join(User user);//用户加入聊天室

    abstract void leave(User user);//用户离开聊天室

}
