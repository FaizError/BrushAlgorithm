package com.hzx.leetcode;

import java.util.HashMap;
import java.util.Map;

public class LeetCode76 {

    public String minWindow(String s, String t) {

        Map<Character, Integer> tMap = new HashMap<>();
        for (char c : t.toCharArray()) {
            tMap.put(c, tMap.getOrDefault(c, 0) + 1);
        }

        int left = 0;
        int right = -1;

        int len = Integer.MAX_VALUE;

        int ansLeft = -1;
        int ansRight = -1;

        Map<Character, Integer> sMap = new HashMap<>();

        while (right < s.length()) {
            right++;
            if (right < s.length() && tMap.containsKey(s.charAt(right))) {
                sMap.put(s.charAt(right), sMap.getOrDefault(s.charAt(right), 0) + 1);
            }
            while (check(tMap, sMap) && left <= right) {
                if (right - left + 1 < len) {
                    len = right - left + 1;
                    ansLeft = left;
                    ansRight = left + len;
                }

                if (sMap.containsKey(s.charAt(left))) {
                    sMap.put(s.charAt(left), sMap.getOrDefault(s.charAt(left), 0) - 1);
                }

                left++;
            }

        }

        return ansLeft == -1 ? "" : s.substring(ansLeft, ansRight);
    }


    private boolean check(Map<Character, Integer> tMap, Map<Character, Integer> sMap) {
        for (char c : tMap.keySet()) {

            if (tMap.get(c) > sMap.getOrDefault(c, 0)) {
                return false;
            }

        }

        return true;
    }
}
