package com.hzx.simulationexercise;

public class test1 {

    /**
     * 某运维系统需要监控服务器的请求流量。给定一个长度为 N 的整数数组 requests，表示每秒的请求数量。现需找出任意连续 K 秒时间窗口内的最大流量总和，作为峰值告警阈值。
     * 此外，需统计共有多少个不重叠的长度为 K 的窗口，其流量总和 ≥ 给定阈值 T。
     *
     * 输入：requests = [1,3,2,5,4,6,2,1], k = 3, t = 8
     * 输出：[15, 1]
     *
     * 解释：
     * 所有长度为3的窗口和：
     * [1,3,2]=6, [3,2,5]=10, [2,5,4]=11, [5,4,6]=15, [4,6,2]=12, [6,2,1]=9
     * maxSum = 15
     *
     * 不重叠窗口：下标0起 [1,3,2]=6(<8不计数), 下标3起 [5,4,6]=15(≥8 count=1), 下标6起只剩2个不够k
     * count = 1
     *
     * @param requests
     * @param k
     * @param t
     * @return
     */
    public int[] peakTraffic(int[] requests, int k, int t) {

        if (requests.length < k) {
            return new int[]{0, 0};
        }

        int maxSum = 0;

        // 不重合窗口记录
        int count = 0;

        // 前k个总和
        for (int i = 0; i < k; i++) {
            maxSum += requests[i];
        }

        int left = 0;
        int right = k;

        int sum = maxSum;

        while (right < requests.length) {
            sum = sum - requests[left] + requests[right];
            maxSum = Math.max(maxSum, sum);
            left++;
            right++;
        }

        for (int i = 0; i < requests.length - k + 1; i += k) {
            int temp = 0;
            for (int j = i; j < i + k; j++) {
                temp += requests[j];
            }

            if (temp >= t) {
                count++;
            }
        }

        return new int[]{maxSum, count};
    }

}
