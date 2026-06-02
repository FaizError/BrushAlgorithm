package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeetCode51 {


    List<List<String>> res = new ArrayList<>();

    public List<List<String>> solveNQueens(int n) {

        char[][] arr = new char[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(arr[i], '.');
        }

        backTracking(arr, 0);

        return res;
    }

    private void backTracking(char[][] arr, int row) {


        if (row == arr.length) {
            res.add(new ArrayList<>(buildList(arr)));
            return;
        }


        for (int i = 0; i < arr.length; i++) {
            if (!isVaild(arr, row, i)) {
                continue;
            }
            arr[row][i] = 'Q';
            backTracking(arr, row + 1);
            arr[row][i] = '.';
        }

    }

    private boolean isVaild(char[][] arr, int row, int col) {

        for (int i = 0; i < row; i++) {
            if (arr[i][col] == 'Q') {
                return false;
            }
        }

        for (int i = row - 1, j = col + 1; i >= 0 && j < arr.length; i--, j++) {
            if (arr[i][j] == 'Q') {
                return false;
            }
        }

        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (arr[i][j] == 'Q') {
                return false;
            }
        }

        return true;
    }

    private List<String> buildList(char[][] arr) {

        List<String> tempList = new ArrayList<>();

        for (char[] chars : arr) {
            tempList.add(new String(chars));
        }

        return tempList;
    }
}
