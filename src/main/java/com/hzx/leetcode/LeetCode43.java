package com.hzx.leetcode;

import java.util.HashMap;
import java.util.Map;

public class LeetCode43 {

    public int firstMissingPositive(int[] nums) {

        Map<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], 1);
        }

        int num = 1;
        for (int i = 0; i < map.size(); i++) {
            if (map.containsKey(num)) {
                num++;
            }
        }

        return num;
    }


    public int firstMissingPositive2(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0) {
                nums[i] = nums.length + 1;
            }
        }

        for (int i = 0; i < nums.length; i++) {

            int num = Math.abs(nums[i]);
            if (num <= nums.length) {
                nums[num - 1] = -Math.abs(nums[num - 1]);
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                return i + 1;
            }
        }

        return nums.length + 1;
    }
}
