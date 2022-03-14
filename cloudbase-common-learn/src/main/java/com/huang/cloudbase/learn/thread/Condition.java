package com.huang.cloudbase.learn.thread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huangjunbiao_cdv
 */
public class Condition {
    public static void main(String[] args) {
        Thread_Condition thread_condition = new Thread_Condition();
        thread_condition.setName("测试condition的线程");
        thread_condition.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread_condition.signal();
    }

    static class Thread_Condition extends Thread {
        @Override
        public void run() {
            await();
        }

        private ReentrantLock lock = new ReentrantLock();
        private java.util.concurrent.locks.Condition condition = lock.newCondition();

        public void await() {
            System.out.println("lock");
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ":我在等待通知到来...");
            try {
                System.out.println("lock");
                lock.lock();
                System.out.println(Thread.currentThread().getName() + ":我在等待通知到来...");
                //await和signal对应
                condition.await();
                //设置等待超时时间
//                condition.await(2, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + ":等到通知了，我继续执行>>>");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("unlock");
                lock.unlock();
            }
        }

        public void signal() {
            try {
                System.out.println("lock");
                lock.lock();
                System.out.println("我要通知在等待的线程，condition.signal()");
                condition.signal();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("unlock");
                lock.unlock();
            }
        }
    }
}
