package com.hzx.leetcode;

public class leetCode27 {
    public int removeElement(int[] nums, int val) {

        int i = 0;
        int j = nums.length - 1;
        int sum = 0;
        while(i < j){
            if(nums[i] == val){
                sum++;
            }
            if(nums[j] == val){
                sum++;
            }
            i++;
            j--;
        }

        return sum;
    }
}
