package com.huang.cloudbase.learn.code;

/**
 * 回文数
 * 给你一个整数 x ，如果 x 是一个回文整数，返回 true ；否则，返回 false 。
 * <p>
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * <p>
 * 输入：x = 121
 * 输出：true
 * <p>
 * 输入：x = -121
 * 输出：false
 * 解释：从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 *
 * @author huangjunbiao_cdv
 */
public class Code009 {

    public static void main(String[] args) {
        System.out.println(isPalindrome(123432));
    }

    public static boolean isPalindrome(int x) {
        if (x < 0) {
            return false;
        }
        // 从余数开始倒推
        int y = 0;
        int t = x;
        while (t != 0) {
            y = y * 10 + t % 10;
            t = t / 10;
        }
        System.out.println(y);
        System.out.println(t);
        return x==y;
    }
}
