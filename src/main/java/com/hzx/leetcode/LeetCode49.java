package com.hzx.leetcode;

import java.util.*;

public class LeetCode49 {

    public List<List<String>> groupAnagrams(String[] strs) {

        if(strs == null || strs.length == 0) return new ArrayList<>();

        Map<String,List<String>> map = new HashMap<>();

        for(String s : strs){
            char[] chars = s.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            List<String> list = map.getOrDefault(key,new ArrayList<>());
            if(map.containsKey(key)){
                list.add(s);
            }
            map.put(key,list);
        }

        return new ArrayList<>(map.values());
    }
}
