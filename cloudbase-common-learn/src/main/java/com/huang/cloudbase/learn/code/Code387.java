package com.huang.cloudbase.learn.code;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个字符串 s ，找到 它的第一个不重复的字符，并返回它的索引 。如果不存在，则返回 -1 。
 *
 *
 *  输入: s = "leetcode"
 * 输出: 0
 *
 * 输入: s = "loveleetcode"
 * 输出: 2
 *
 * @author huangjunbiao_cdv
 */
public class Code387 {

    public static void main(String[] args) {
        // tips：遍历字符串，用一个 map 或者字典存放字符串中每个字符出现的次数。然后再次遍历字符串，
        // 取出对应字符出现的次数，若次数为 1，直接返回当前字符串的下标。遍历结束，返回 -1。
        System.out.println(uniqueIndex("shadioasoashndabsi"));
    }

    public static int uniqueIndex(String str) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < str.length(); i++) {
            if (map.containsKey(String.valueOf(str.charAt(i)))) {
                map.replace(String.valueOf(str.charAt(i)), map.get(String.valueOf(str.charAt(i))) + 1);
            } else {
                map.put(String.valueOf(str.charAt(i)), 1);
            }
        }
        for (int i = 0; i < str.length(); i++) {
            if (map.get(String.valueOf(str.charAt(i))) == 1) {
                return i;
            }
        }

        return -1;
    }
}
