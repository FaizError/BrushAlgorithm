package com.hzx.leetcode;

public class leetCode1 {

    public int[] twoSum(int[] nums, int target) {
        int[] arr = new int[2];
        int left = 0;
        int right = nums.length - 1;

        while(left < right){
            if(nums[left] + nums[right] == target){
                arr[0] = left;
                arr[1] = right;
                break;
            }
            left++;
        }

        return arr;
    }
}
