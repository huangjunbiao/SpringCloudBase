package com.huang.cloudbase.learn.javadesignmode.template;

/**
 * 模板方法测试
 *
 * 抽象模板：定义一个模板方法，给出逻辑的骨架，而逻辑的组成步骤在相应的抽象操作中，由具体子类实现。
 * 具体模板：实现父类所定义的抽象方法，每一个抽象模板角色都可以有任意多个具体模板角色与之对应，从而使模板的最终结果也不一样。
 *
 *
 *
 * @author huangjunbiao
 */
public class TemplateClient {
    public static void main(String[] args) {
        MilkDrink milkDrink = new MilkDrink();
        milkDrink.makeDrink();

        System.out.println("-------------------");
        MilkTeaDrink milkTeaDrink = new MilkTeaDrink();
        milkTeaDrink.makeDrink();
    }
}
