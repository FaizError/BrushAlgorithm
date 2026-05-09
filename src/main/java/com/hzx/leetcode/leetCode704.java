package com.hzx.leetcode;

public class leetCode704 {
    public static int search(int[] nums, int target) {
        int i = 0;
        int n = nums.length - 1;
        while(i <= n){
            int index = (i + n) / 2;
            if(nums[index] < target){
                i = index + 1;
            }else if(nums[index] > target){
                n = index - 1;
            }else if(nums[index] == target){
                return index;
            }
        }
        return -1;
    }
}
