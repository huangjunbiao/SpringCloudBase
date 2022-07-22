package com.huang.cloudbase.learn.code;

/**
 * 最长公共前缀
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * 如果不存在公共前缀，返回空字符串 ""。
 *
 * 输入：strs = ["flower","flow","flight"]
 * 输出："fl"
 *
 * @author huangjunbiao_cdv
 */
public class Code014 {
    public static void main(String[] args) {
        String[] s = new String[]{"c", "casd"};
        System.out.println(longestCommonPrefix(s));
    }

    public static String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        if (strs.length == 1) {
            return strs[0];
        }
        String pre = "";
        int i = 0;
        while (true) {
            if (strs[0].length() == i) {
                return pre;
            }
            char temp = strs[0].charAt(i);
            for (int k = 1; k < strs.length; k++) {
                if (strs[k].length() == i || temp != strs[k].charAt(i)) {
                    return pre;
                }
            }
            pre = pre + temp;
            i++;
        }
    }

    public static String longestCommonPrefix2(String[] strs) {
        int n;
        if ((n = strs.length) == 0) return "";
        for (int i = 0; i < strs[0].length(); ++i) {
            for (int j = 1; j < n; ++j) {
                if (strs[j].length() <= i || strs[j].charAt(i) != strs[0].charAt(i)) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }
}
