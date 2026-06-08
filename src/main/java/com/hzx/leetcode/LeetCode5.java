package com.hzx.leetcode;

public class LeetCode5 {

    public String longestPalindrome(String s) {
        int len = s.length();
        char[] array = s.toCharArray();
        boolean[][] dp = new boolean[len][len];
        int maxlen = 0;
        int left = 0;
        int right = 0;
        for (int i = len - 1; i >= 0; i--) {
            for (int j = i; j < len; j++) {
                if (array[i] == array[j]) {
                    if (j - i <= 1) {
                        dp[i][j] = true;
                    } else if (dp[i + 1][j - 1]) {
                        dp[i][j] = true;
                    }
                }
                if (dp[i][j] && j - i + 1 > maxlen) {
                    maxlen = j - i + 1;
                    left = i;
                    right = j;
                }
            }
        }
        return s.substring(left, right + 1);
    }
}
