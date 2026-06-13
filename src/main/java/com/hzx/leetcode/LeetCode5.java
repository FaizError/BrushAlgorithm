package com.hzx.leetcode;

public class LeetCode5 {

    // dp
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

    // 中心法
    public String longestPalindrome1(String s) {

        int maxLen = 0;
        int start = 0;
        for (int i = 0; i < s.length(); i++) {

            // 奇数子串 如 bab
            int single = getMaxLen(s, i, i);

            // 偶数子串 如 abba
            int doubled =  getMaxLen(s, i, i + 1);

            int len = Math.max(single, doubled);

            if(len > maxLen){
                maxLen = len;
                start = i - (maxLen - 1) / 2;
            }

        }

        return s.substring(start, start + maxLen);
    }

    private int getMaxLen(String s,int left, int right) {

        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }

        return right - left - 1;
    }
}
