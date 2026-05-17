package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeetCode438 {

    public List<Integer> findAnagrams(String s, String p) {
        char[] pCharArray = p.toCharArray();
        Map<Character, Integer> pMap = new HashMap<>();
        for (char c : pCharArray) {
            pMap.put(c, pMap.getOrDefault(c, 0) + 1);
        }

        List<Integer> res = new ArrayList<>();

        // 出现字母是否符合预期 符合预期+1
        int count = 0;

        Map<Character, Integer> sMap = new HashMap<>();

        int left = 0;
        int right = 0;

        while (right < s.length()) {
            if (pMap.containsKey(s.charAt(right))) {
                Integer oldValue = sMap.getOrDefault(s.charAt(right), 0);
                sMap.put(s.charAt(right), oldValue + 1);

                if (oldValue.equals(pMap.get(s.charAt(right)))) {
                    count--;
                } else if (oldValue + 1 == pMap.get(s.charAt(right))) {
                    count++;
                }
            }

            right++;

            if (right - left == p.length()) {
                if (count == pMap.size()) {
                    res.add(left);
                }
                if (pMap.containsKey(s.charAt(left))) {
                    Integer oldValue = sMap.getOrDefault(s.charAt(left), 0);

                    sMap.put(s.charAt(left), oldValue - 1);

                    if (oldValue.equals(pMap.get(s.charAt(left)))) {
                        count--;
                    } else if (oldValue - 1 == (pMap.get(s.charAt(left)))) {
                        count++;
                    }
                }
                left++;

            }

        }

        return res;
    }
}
