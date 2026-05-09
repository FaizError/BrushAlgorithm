package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class leetCode2615 {

    public long[] distance(int[] nums) {

        Map<Integer, List<Integer>> numsMap = new HashMap<>();

        for (int i = 0;i < nums.length;i++){
            List<Integer> list = numsMap.getOrDefault(nums[i], new ArrayList<>());
            list.add(i);
            numsMap.put(nums[i],list);
        }

        long[] arr = new long[nums.length];
        for (int num :numsMap.keySet()){
            List<Integer> list = numsMap.get(num);
            long total = 0;
            for (int i :list){
                total += i;
            }
            long prefixTotal = 0;

            for (int i = 0; i < list.size();i++){
                int index = list.get(i);
                arr[index] = total - prefixTotal * 2 + index * (2 * i - list.size());
                prefixTotal += index;
            }
        }

        return arr;
    }
}
