package com.hzx.leetcode;

public class LeetCode45 {

    public int jump(int[] nums) {

        int index = 0;

        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i <= index) {

                if (index < i + nums[i]) {
                    index = i + nums[i];
                    count++;
                }

                if (index >= nums.length - 1) {
                    break;
                }
            }
        }

        return count;
    }
}
