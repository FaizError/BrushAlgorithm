package com.hzx.leetcode;

import java.util.HashMap;
import java.util.Map;

public class LeetCode560 {

    public int subarraySum(int[] nums, int k) {

        // 前缀和
        int prefix_sum = 0;

        // 出现总次
        int count = 0;

        // 记录出现的总的前缀和 - k出现的次数
        Map<Integer,Integer> map = new HashMap<>();

        // 初始化map 前缀和从0开始
        map.put(prefix_sum,1);
        for (int i :nums){

            prefix_sum += i;

            // 如果map中有prefix_sum - k 说明有包含k的连续子数组
            if(map.containsKey(prefix_sum - k)){
                count += map.get(prefix_sum - k);
            }

            map.put(prefix_sum,map.getOrDefault(prefix_sum,0) + 1);
        }

        return count;
    }

}
