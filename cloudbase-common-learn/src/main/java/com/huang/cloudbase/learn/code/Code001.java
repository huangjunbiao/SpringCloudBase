package com.huang.cloudbase.learn.code;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target  的那 两个 整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 *
 *
 * 示例 1：
 * 输入：nums = [2,7,11,15], target = 9
 * 输出：[0,1]
 * 解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
 *
 * @author huangjunbiao_cdv
 */
public class Code001 {
    public static void main(String[] args) {
        int[] ints = new int[]{2,1,4,3,7};
        int target = 7;
        System.out.println(Arrays.toString(sum(ints, target)));
    }

    public static int[] sum(int[] nums, int target) {
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int l = target - nums[i];
            if (result.containsKey(l)) {
                return new int[] {result.get(l), i};
            }
            result.put(nums[i], i);
        }
        return null;
    }
}
