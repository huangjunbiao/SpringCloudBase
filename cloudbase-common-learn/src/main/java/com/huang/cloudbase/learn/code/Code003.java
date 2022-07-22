package com.huang.cloudbase.learn.code;

import java.util.HashSet;
import java.util.Set;

/**
 * 给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 *
 * 输入: s = "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 *
 * 输入: s = "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "kew"，所以其长度为 3。
 *      请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 * @author huangjunbiao_cdv
 */
public class Code003 {

    public static void main(String[] args) {
        // 定义一个哈希表记录当前窗口内出现的字符，i、j 分别表示不重复子串的开始位置和结束位置，ans 表示无重复字符子串的最大长度。
        //
        //遍历 s 每个字符 c，若 [i, j - 1] 窗口内存在 c，则 i 循环向右移动，更新哈希表，直至 [i, j - 1] 窗口不存在 c，循环结束。将 c 加入哈希表中，此时 [i, j] 窗口内不含重复元素，更新 ans 的最大值：ans = max(ans, j - i + 1)。
        //
        //最后返回 ans 即可。
        //
        //时间复杂度 O(n)，其中 n 表示字符串 s 的长度。
        System.out.println(lengthOfLongestSubstring("pwwkew"));
    }

    public static int lengthOfLongestSubstring(String s) {
        int i=0,j=0,ans=0;
        Set<Character> chars = new HashSet<>();
        for (char c : s.toCharArray()) {
            while (chars.contains(c)) {
                chars.remove(s.charAt(i++));
            }
            chars.add(c);
            ans = Math.max(ans, j-i+1);
            ++j;
            System.out.println("chars：" + chars + j + "," + i);
        }
        return ans;
    }
}
