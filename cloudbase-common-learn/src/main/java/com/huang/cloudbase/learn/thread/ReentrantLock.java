package com.huang.cloudbase.learn.thread;

import java.util.concurrent.TimeUnit;

/**
 * 可定时的锁请求
 *
 * @author huangjunbiao_cdv
 */
public class ReentrantLock {
    public static void main(String[] args) {
        java.util.concurrent.locks.ReentrantLock reentrantLock = new java.util.concurrent.locks.ReentrantLock();
        Thread thread1 = new Thread_tryLock(reentrantLock);
        thread1.setName("thread1");
        thread1.start();
        Thread thread2 = new Thread_tryLock(reentrantLock);
        thread2.setName("thread2");
        thread2.start();
    }

    static class Thread_tryLock extends Thread {
        java.util.concurrent.locks.ReentrantLock reentrantLock;

        public Thread_tryLock(java.util.concurrent.locks.ReentrantLock reentrantLock) {
            this.reentrantLock = reentrantLock;
        }

        @Override
        public void run() {
            try {
                System.out.println("ret lock:" + Thread.currentThread().getName());
                boolean tryLock = reentrantLock.tryLock(3, TimeUnit.SECONDS);
                if (tryLock) {
                    System.out.println("try lock success:" + Thread.currentThread().getName());
                    System.out.println("睡眠一下：" + Thread.currentThread().getName());
                    Thread.sleep(5000);
                    System.out.println("醒了：" + Thread.currentThread().getName());
                } else {
                    System.out.println("try lock 超时 :" + Thread.currentThread().getName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("unlock:" + Thread.currentThread().getName());
                reentrantLock.unlock();
            }
        }
    }
}
