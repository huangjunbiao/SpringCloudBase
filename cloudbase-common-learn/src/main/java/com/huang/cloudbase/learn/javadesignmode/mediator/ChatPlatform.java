package com.huang.cloudbase.learn.javadesignmode.mediator;

import java.util.Objects;

/**
 * @author huangjunbiao_cdv
 */
public class ChatPlatform extends Mediator {
    @Override
    void sendToAll(String msg) {
        for (User user : list) {
            user.accept(msg);
        }
    }

    @Override
    void senToPerson(String msg, String name) {
        for (User user : list) {
            if (Objects.equals(user.getName(), name)) {
                user.accept(msg);
            }
        }
    }

    @Override
    void join(User user) {
        list.add(user);
    }

    @Override
    void leave(User user) {
        list.remove(user);
    }
}
