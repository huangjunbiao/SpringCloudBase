package com.huang.cloudbase.learn.javadesignmode.template;

/**
 * @author huangjunbiao
 */
public class MilkTeaDrink extends DrinkTemplate {
    @Override
    void takeBottle() {
        System.out.println("拿放奶茶的杯子");
    }

    @Override
    void addSomething() {
        System.out.println("加入珍珠");
    }

    @Override
    void addWater() {
        System.out.println("加一半牛奶");
        System.out.println("加一半红茶");
    }
}
