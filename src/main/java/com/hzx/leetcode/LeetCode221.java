package com.hzx.leetcode;

public class LeetCode221 {

    // 空间复杂度 mn
    public int maximalSquare(char[][] matrix) {

        int[][] dp = new int[matrix.length + 1][matrix[0].length + 1];

        int maxSide = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] != '0') {
                    dp[i + 1][j + 1] = Math.min(Math.min(dp[i][j], dp[i][j + 1]), dp[i + 1][j]) + 1;
                }
                maxSide = Math.max(maxSide, dp[i + 1][j + 1]);
            }
        }

        return maxSide * maxSide;
    }

    // 空间复杂度 n

}
