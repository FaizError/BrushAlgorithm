package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode54 {

    public List<Integer> spiralOrder(int[][] matrix) {

        List<Integer> res = new ArrayList<>();

        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return res;
        }

        int left = 0;
        int right = matrix[0].length - 1;
        int top = 0;
        int bottom = matrix.length - 1;

        while (left <= right && top <= bottom) {
            for (int i = left; i <= right; i++) {
                res.add(matrix[top][i]);
            }

            for (int j = top + 1; j <= bottom; j++) {
                res.add(matrix[j][right]);
            }

            if (left < right && top < bottom) {
                for (int m = right - 1; m > left; m--) {
                    res.add(matrix[bottom][m]);
                }

                for (int n = bottom; n > top; n--) {
                    res.add(matrix[n][left]);
                }
            }
            left++;
            right--;
            top++;
            bottom--;
        }

        return res;
    }
}
