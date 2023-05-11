package com.huang.cloudbase.learn.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author huangjunbiao_cdv
 */
public class StringSpecialUtils {
    public static double similar(String strA, String strB) {
        String newStrA, newStrB;
        if (strA.length() < strB.length()) {
            newStrA = removeSign(strB);
            newStrB = removeSign(strA);
        } else {
            newStrA = removeSign(strA);
            newStrB = removeSign(strB);
        }

        // 用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
        int temp = Math.max(newStrA.length(), newStrB.length());
        if (0 == temp) {
            // 两个都是空串相似度为1，被认为是相同的串
            return 1;
        }

        int temp2 = longestCommonSubstring(newStrB, newStrA);
        return NumberUtil.div(temp2, temp);
    }

    // --------------------------------------------------------------------------------------------------- Private method start

    /**
     * 将字符串的所有数据依次写成一行，去除无意义字符串
     *
     * @param str 字符串
     * @return 处理后的字符串
     */
    private static String removeSign(String str) {
        int length = str.length();
        StringBuilder sb = StrUtil.builder(length);
        // 遍历字符串str,如果是汉字数字或字母，则追加到ab上面
        char c;
        for (int i = 0; i < length; i++) {
            c = str.charAt(i);
            if (isValidChar(c)) {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 判断字符是否为汉字，数字和字母， 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
     *
     * @param charValue 字符
     * @return true表示为非汉字，数字和字母，false反之
     */
    private static boolean isValidChar(char charValue) {
        return (charValue >= 0x4E00 && charValue <= 0X9FFF) || //
            (charValue >= 'a' && charValue <= 'z') || //
            (charValue >= 'A' && charValue <= 'Z') || //
            (charValue >= '0' && charValue <= '9');
    }

    /**
     * 求公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。
     *
     * @param strA 字符串1
     * @param strB 字符串2
     * @return 公共子串
     */
    public static int longestCommonSubstring(String strA, String strB) {
        int X = strA.length() + 1;//0~length
        int Y = strB.length() + 1;
        //确定求值的方向
        if (X > Y) {//从左到右
            int[] dp1 = new int[Y];
            int[] dp2 = new int[Y];
            int mode = 0;//当前对哪个表进行求值，0为dp2,1为dp1,初始状态，dp1是求好的，都是0
            for (int x = 1; x < X; x++) {
                for (int y = 1; y < Y; y++) {
                    if (mode == 0) {
                        dp2[y] = strA.charAt(x-1) == strB.charAt(y-1) ? dp1[y - 1] + 1 : Math.max(dp1[y], dp2[y - 1]);
                    } else {
                        dp1[y] = strA.charAt(x-1) == strB.charAt(y-1) ? dp2[y - 1] + 1 : Math.max(dp2[y], dp1[y - 1]);
                    }
                    if (y == Y - 1) mode = mode == 0 ? 1 : 0;
                }
            }
            return mode == 1 ? dp2[Y - 1] : dp1[Y - 1];
        } else {//从上到下
            int[] dp1 = new int[X];
            int[] dp2 = new int[X];
            int mode = 0;//当前对哪个表进行求值，0为dp2,1为dp1,初始状态，dp1是求好的，都是0
            for (int y = 1; y < Y; y++) {
                for (int x = 1; x < X; x++) {
                    if (mode == 0) {
                        dp2[x] = strA.charAt(x-1) == strB.charAt(y-1) ? dp1[x - 1] + 1 : Math.max(dp1[x], dp2[x - 1]);
                    } else {
                        dp1[x] = strA.charAt(x-1) == strB.charAt(y-1) ? dp2[x - 1] + 1 : Math.max(dp2[x], dp1[x - 1]);
                    }
                    if (x == X - 1) mode = mode == 0 ? 1 : 0;
                }
            }
            return mode == 1 ? dp2[X - 1] : dp1[X - 1];
        }
    }
}
