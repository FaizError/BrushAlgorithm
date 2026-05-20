package com.hzx.leetcode;

import java.util.ArrayDeque;
import java.util.Deque;

public class LeetCode239 {

    public int[] maxSlidingWindow(int[] nums, int k) {

        int n = nums.length;
        int[] arr = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // 存下标，保持值递减

        for (int i = 0; i < n; i++) {
            // 移除窗口外的元素
            if (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            // 维护单调递减：弹出所有比当前元素小的
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            // 窗口形成后记录结果
            if (i >= k - 1) {
                arr[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return arr;

    }
}
