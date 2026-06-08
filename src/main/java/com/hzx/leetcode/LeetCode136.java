package com.hzx.leetcode;

public class LeetCode136 {

    public int singleNumber(int[] nums) {

        int start = nums[0];

        for (int i = 1; i < nums.length; i++) {
            start ^= nums[i];
        }

        return start;
    }
}
