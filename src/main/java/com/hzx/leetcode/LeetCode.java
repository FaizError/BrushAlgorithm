package com.hzx.leetcode;

public class LeetCode {

    public int[] productExceptSelf(int[] nums) {

        int[] leftArr = new int[nums.length];

        leftArr[0] = 1;

        for (int i = 1; i < nums.length; i++) {
            leftArr[i] = leftArr[i - 1] * nums[i - 1];
        }

        int right = 1;

        for (int i = nums.length - 1; i >= 0; i--) {
            leftArr[i] = leftArr[i] * right;

            right = right * nums[i];
        }

        return leftArr;
    }
}
