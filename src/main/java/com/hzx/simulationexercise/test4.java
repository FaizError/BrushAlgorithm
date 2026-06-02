package com.hzx.simulationexercise;

import java.util.Arrays;

public class test4 {

    /**
     * 题目描述
     * 给定一个字符串 s 和一个模式串 p（均由小写字母组成），统计 s 中有多少个子串，其字母种类构成与 p 完全相同。
     * 说明：字母种类构成相同 = 包含的字母集合与 p 完全一致，且每种字母的数量也与 p 相同。即该子串是 p 的一个字母异位词。
     * 请实现函数：
     * 1
     * public int countAnagrams(String s, String p)
     * 输入
     * s：长度 N（1 ≤ N ≤ 10^5）
     * p：模式串，长度 K（1 ≤ K ≤ min(N, 26)）
     * 输出
     * 返回满足条件的子串数量。
     * 示例
     * 1
     * 2
     * 3
     * 输入：s = "cbaebabacd", p = "abc"
     * 输出：2
     * 解释：子串 "cba"（下标0-2）和 "bac"（下标6-8）都是 "abc" 的异位词
     * 1
     * 2
     * 3
     * 输入：s = "abab", p = "ab"
     * 输出：3
     * 解释："ab"(0-1), "ba"(1-2), "ab"(2-3) — 注意 "ba" 包含a和b各1个，与"ab"一致
     *
     * @param s
     * @param p
     * @return
     */

    public int countAnagrams(String s, String p) {
        if (s.length() < p.length()) {
            return 0;
        }

        int[] sArr = new int[26];
        int[] pArr = new int[26];

        for (int i = 0; i < p.length(); i++) {
            sArr[s.charAt(i) - 'a']++;
            pArr[p.charAt(i) - 'a']++;
        }

        int result = Arrays.equals(sArr, pArr) ? 1 : 0;

        for (int i = p.length(); i < s.length(); i++) {
            sArr[s.charAt(i) - 'a']++;
            sArr[s.charAt(i - p.length()) - 'a']--;
            if (Arrays.equals(sArr, pArr)) {
                result++;
            }
        }

        return result;
    }
}
