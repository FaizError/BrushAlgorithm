package com.hzx.leetcode;

import java.util.Arrays;


public class LeetCode128 {

    public int longestConsecutive(int[] nums) {

        if(nums==null || nums.length==0){
            return 0;
        }

        nums = Arrays.stream(nums).sorted().distinct().toArray();
        int max = 1;
        int count = 1;
        for (int i = 1; i < nums.length; i++ ) {
            if(nums[i] - nums[i-1] == 1) {
                count++;
            }else {
                max = Math.max(max, count);
                count = 1;
            }
        }

        return Math.max(max, count);
    }

}
