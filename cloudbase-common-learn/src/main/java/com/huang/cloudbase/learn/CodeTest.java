package com.huang.cloudbase.learn;

import java.util.HashSet;
import java.util.Set;

/**
 * 最大不重复子串
 *
 * @author huangjunbiao
 */
public class CodeTest {

    public static void main(String[] args) {
        String s = "qwerwerertyuiertgvbf";
        System.out.println(formatSubString(s));
    }

    public static int formatSubString(String str) {
        int len = str.length();
        Set<Character> window = new HashSet<>();
        int left = 0;
        int right = 0;
        int res = 0;
        while (right < len) {
            char c = str.charAt(right);
            right++;
            while (window.contains(c)) {
                window.remove(str.charAt(left));
                left++;
            }
            window.add(c);
            res = Math.max(res, window.size());
        }
        return res;
    }
}
