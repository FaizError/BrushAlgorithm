package com.hzx.leetcode;

public class LeetCode410 {

    // 动态规划：重叠子问题，最优子结构
    public int splitArrayByDP(int[] nums, int k) {
        // dp[i][j] 将其前i个元素分成j组 全局最小值
        int[][] dp = new int[nums.length][k];
        

        return 0;
    }

    public int splitArrayByBisection(int[] nums, int k) {

        // 找边界 left最大的数  right总和  这样不管怎么拆分 拆分的子数组和最大值都在这个范围
        int left = 0;
        int right = 0;
        for (int i = 0; i < nums.length; i++) {
            left = Math.max(left, nums[i]);
            right += nums[i];
        }

        while (left < right) {
            int mid = (left + right) / 2;

            // 贪心地模拟分割的过程，从前到后遍历数组，用 sum 表示当前分割子数组的和，cnt 表示已经分割出的子数组的数量（包括当前子数组），那么每当 sum 加上当前值超过了 x，我们就把当前取的值作为新的一段分割子数组的开头，并将 cnt 加 1。遍历结束后验证是否 cnt 不超过 m
            if (check(nums, mid, k)) {
                right = mid;

            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean check(int[] nums, int x, int m) {
        int sum = 0;
        int cnt = 1;
        for (int i = 0; i < nums.length; i++) {

            if (sum + nums[i] > x) {
                cnt++;
                sum = nums[i];
            } else {
                sum += nums[i];
            }
        }

        return cnt <= m;
    }

}
