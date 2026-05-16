package com.hzx.leetcode;

import java.util.*;

public class LeetCode438 {

    public List<Integer> findAnagrams(String s, String p) {
        char[] pCharArray = p.toCharArray();
        Map<Character, Integer> pMap = new HashMap<>();
        for (char c : pCharArray) {
            pMap.put(c, pMap.getOrDefault(c, 0) + 1);
        }

        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < s.length(); i++) {

            Map<Character, Integer> sMap = new HashMap<>();
            for (int j = i; j < s.length(); j++) {
                if (pMap.containsKey(s.charAt(j)) && (sMap.getOrDefault(s.charAt(j), 0) < pMap.get(s.charAt(j)))) {
                    sMap.put(s.charAt(j), sMap.getOrDefault(s.charAt(j), 0) + 1);
                } else {
                    break;
                }
            }

            boolean result = true;
            for (char c : pMap.keySet()) {
                if(sMap.getOrDefault(c, 0) != pMap.get(c)){
                    result = false;
                }
            }


            if (result){
                res.add(i);
            }

        }

        return res;
    }
}
