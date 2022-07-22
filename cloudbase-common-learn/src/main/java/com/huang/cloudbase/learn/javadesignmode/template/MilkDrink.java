package com.huang.cloudbase.learn.javadesignmode.template;

/**
 * @author huangjunbiao
 */
public class MilkDrink extends DrinkTemplate {
    @Override
    void takeBottle() {
        System.out.println("拿牛奶杯");
    }

    @Override
    void addSomething() {
        System.out.println("加入果肉");
    }

    @Override
    void addWater() {
        System.out.println("加入牛奶");
    }
}
