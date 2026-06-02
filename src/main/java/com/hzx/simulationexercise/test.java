package com.hzx.simulationexercise;

public class test {

    /**
     * 统计达标区间数量（100分）
     *
     * 给定正整数数组 nums 和目标值 S。
     * 求有多少个连续子数组，其和 ≥ S。
     *
     * 输入：nums = [1,2,3,4], S = 5
     * 输出：6
     * 解释：≥5的子数组：[2,3],[1,2,3],[3,4],[2,3,4],[1,2,3,4],[4]...
     * 实际[4]=4<5, 所以是 [2,3],[1,2,3],[3,4],[2,3,4],[1,2,3,4] = 5个
     */

    public int countSubarrays(int[] nums, int S) {

        int count = 0;
        for (int i = 0; i < nums.length; i++) {
            int index = i + 1;
            int sum = nums[i];
            if (sum >= S) {
                count++;
            }
            while (index < nums.length) {
                sum += nums[index];
                if (sum >= S) {
                    count++;
                }
                index++;
            }
        }

        return count;
    }

}
