package com.hzx.odtest;

public class od522_200 {

    public static int maxAccuracy(int N, int T, int[] accuracy, int[] latency) {

        int M = accuracy.length;
        int[][] dp = new int[N + 1][T + 1];
        for (int k = 0; k <= N; k++) {
            for (int t = 0; t <= T; t++) {
                dp[k][t] = -1;
            }
        }
        for (int t = 0; t <= T; t++) {
            dp[0][t] = 0;
        }
        for (int k = 1; k <= N; k++) {
            for (int t = 0; t <= T; t++) {
                int bestVal = -1;
                for (int m = 0; m < M; m++) {
                    int l = latency[m];
                    int a = accuracy[m];
                    if (t >= l) {
                        int prevT = t - l;
                        if (dp[k - 1][prevT] != -1) {
                            int candidate = dp[k - 1][prevT] + a;
                            if (candidate > bestVal) {
                                bestVal = candidate;
                            }
                        }
                    }
                }
                dp[k][t] = bestVal;
            }
        }
        int maxAcc = -1;
        for (int t = 0; t <= T; t++) {
            if (dp[N][t] > maxAcc) {
                maxAcc = dp[N][t];
            }
        }
        return (maxAcc == -1) ? 0 : maxAcc;
    }
}
