package com.huang.cloudbase.learn.javadesignmode.template;

/**
 * @author huangjunbiao
 */
public abstract class DrinkTemplate {
    public void makeDrink() {
        takeBottle();
        addSomething();
        addWater();
        plasticBottle();
    }

    abstract void takeBottle();

    abstract void addSomething();

    abstract void addWater();

    protected void plasticBottle() {
        System.out.println("塑封杯子，制作完成。");
    }
}
