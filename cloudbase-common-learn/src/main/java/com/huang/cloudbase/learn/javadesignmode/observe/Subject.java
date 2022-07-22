package com.huang.cloudbase.learn.javadesignmode.observe;

/**
 * 被观察者
 *
 * @author huangjunbiao_cdv
 */
public interface Subject {
    void registerObserver(Observer observer);

    void removeObserver(Observer observer);

    void notifyObserver(String msg);
}
