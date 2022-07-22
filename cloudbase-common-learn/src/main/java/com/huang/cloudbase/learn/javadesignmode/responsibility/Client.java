package com.huang.cloudbase.learn.javadesignmode.responsibility;

/**
 * 责任链模式客户端实现
 *
 * 使多个对象都有机会处理请求，从而避免了请求的发送者和接收者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有对象处理它为止。
 *
 * 责任链模式最重要的优点是解耦，将客户端和处理者分开，客户端不需要了解是哪个处理者对事件进行处理，处理者也不需要知道处理的整个流程。
 *
 * @author huangjunbiao
 */
public class Client {

    public static void main(String[] args) {
        HandleRuleA handleRuleA = new HandleRuleA(1);
        HandleRuleB handleRuleB = new HandleRuleB(2);
        // 串联handleA和handleB
        handleRuleA.setNextHandler(handleRuleB);
        handleRuleA.handleMessage(new Request(1));
        handleRuleA.handleMessage(new Request(2));
        handleRuleA.handleMessage(new Request(3));
    }
}
