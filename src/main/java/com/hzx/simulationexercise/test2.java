package com.hzx.simulationexercise;

import java.util.Arrays;

public class test2 {

    /**
     * 题目描述
     * 系统中有 N 个待执行任务，每个任务有一个开始时间 start[i] 和结束时间 end[i]。你只有一台服务器，同一时刻只能执行一个任务。
     * 请你计算：
     * 1. 最多能完成多少个任务（不可重叠）
     * 2. 如果能完成的最多任务数为 M，返回完成 M 个任务时，服务器的最短总占用时长
     *
     * @param tasks
     * @return
     */
    public int[] bestSchedule(int[][] tasks) {

        int n = tasks.length;
        if (n == 0) {
            return new int[]{0, 0};
        }

        // 1. 按结束时间排序（保证最多任务数的前提）
        Arrays.sort(tasks, (a, b) -> a[1] - b[1]);

        // 2. p[i] = 最靠右的与 i 不重叠的任务下标（二分查找）
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = -1;
            int left = 0, right = i - 1;
            while (left <= right) {
                int mid = (left + right) / 2;
                if (tasks[mid][1] < tasks[i][0]) {
                    p[i] = mid;       // mid 不重叠，尝试找更靠右的
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        // 3. dp[i][0] = maxCount, dp[i][1] = minSum
        int[][] dp = new int[n][2];

        for (int i = 0; i < n; i++) {
            int duration = tasks[i][1] - tasks[i][0];

            // 不选任务 i
            int cntSkip = (i > 0) ? dp[i - 1][0] : 0;
            int sumSkip = (i > 0) ? dp[i - 1][1] : 0;

            // 选任务 i（与 p[i] 的最优解拼接）
            int cntTake = 1;
            int sumTake = duration;
            if (p[i] >= 0) {
                cntTake += dp[p[i]][0];
                sumTake += dp[p[i]][1];
            }

            // 择优：先比数量（多的好），再比时长（少的好）
            if (cntTake > cntSkip || (cntTake == cntSkip && sumTake < sumSkip)) {
                dp[i][0] = cntTake;
                dp[i][1] = sumTake;
            } else {
                dp[i][0] = cntSkip;
                dp[i][1] = sumSkip;
            }
        }

        return dp[n - 1];
    }
}
