package com.huang.cloudbase.learn.javadesignmode.observe;

import java.util.ArrayList;
import java.util.List;

/**
 * 观察者模式：定义对象间一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并更新。即订阅-发布
 *
 * @author huangjunbiao_cdv
 */
public class BankCallCenter implements Subject {
    List<Observer> list = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        list.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        list.remove(observer);
    }

    @Override
    public void notifyObserver(String msg) {
        for (Observer observer : list) {
            observer.update(msg);
        }
    }
}
