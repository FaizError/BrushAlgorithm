package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode77 {

    List<Integer> temp = new ArrayList<>();

    List<List<Integer>> res = new ArrayList<>();

    // 回溯算法
    public List<List<Integer>> combine(int n, int k) {
        backTracking(1, n, k);

        return res;
    }


    private void backTracking(int start, int n, int k) {

        if (temp.size() + (n - start + 1) < k) {
            return;
        }

        if (temp.size() == k) {
            res.add(new ArrayList<>(temp));
            return;
        }
        
        temp.add(start);
        backTracking(start + 1, n, k);
        temp.remove(temp.size() - 1);
        backTracking(start + 1, n, k);
    }
}
