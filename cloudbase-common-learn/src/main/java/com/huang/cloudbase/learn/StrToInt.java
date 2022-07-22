package com.huang.cloudbase.learn;

/**
 * 将字符串转换为整数
 *
 * @author huangjunbiao
 */
public class StrToInt {

    public static int strToInt(String str) {
        if (str.length() == 0) {
            return 0;
        }
        char[] chars = str.toCharArray();
        // 判断是否存在符号位
        int flag = 0;
        if (chars[0] == '+') {
            flag = 1;
        } else if (chars[0] == '-') {
            flag = 2;
        }
        int start = flag > 0 ? 1 : 0;
        // 保存结果
        int res = 0;
        for (int i = start; i < chars.length; i++) {
            // 判断是否是数字
            if (Character.isDigit(chars[i])) {
                int temp = chars[i] - '0';
                // *10 保证位数
                res = res * 10 + temp;
            } else {
                return 0;
            }
        }
        return flag != 2 ? res : -res;
    }

    public static void main(String[] args) {
        String s = "-12312312";
        System.out.println("使⽤库函数转换：" + Integer.valueOf(s));
        int res = strToInt(s);
        System.out.println("使⽤⾃⼰写的⽅法转换：" + res);
    }
}
