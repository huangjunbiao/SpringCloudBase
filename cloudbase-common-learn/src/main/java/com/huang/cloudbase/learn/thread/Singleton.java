package com.huang.cloudbase.learn.thread;

/**
 * 单例，双重锁
 * volatile保证可见性，不保证原子性，禁止指令重排序
 * 无法解决多线程同时写问题
 *
 * @author huangjunbiao_cdv
 */
public class Singleton {
    private static volatile Singleton singleton;

    public static Singleton getInstance() {
        if (singleton == null) {
            synchronized (Singleton.class) {
                if (singleton == null) {
                    singleton = new Singleton();
                }
            }
        }
        return singleton;
    }
}
