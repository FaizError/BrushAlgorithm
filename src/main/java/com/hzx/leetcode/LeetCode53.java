package com.hzx.leetcode;

public class LeetCode53 {

    // 贪心算法
    public int maxSubArrayByGreedyAlgorithm(int[] nums) {

        // 贪心算法
        int preFix = 0;
        int sum = 0;

        for (int num : nums) {
            sum = Math.max(sum + num, num);
            preFix = Math.max(preFix, sum);
        }

        return preFix;
    }


    // 动态规划
    public int maxSubArrayByDynamicProgramming(int[] nums) {

        // 动态规划
        int max = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > 0) {
                nums[i] += nums[i - 1];
            }
            max = Math.max(max, nums[i]);
        }

        return max;
    }

    // 分治算法
    public int maxSubArrayByDivideConquer(int[] nums) {

        // 分治算法
        return 0;
    }


}
