package com.hzx.leetcode;

public class LeetCode73 {

    // 空间复杂度mn
    public void setZeroes(int[][] matrix) {

        int[][] arr = new int[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                arr[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (arr[i][j] == 0) {
                    for (int m = 0; m < matrix.length; m++) {
                        matrix[m][j] = 0;
                    }
                    for (int n = 0; n < matrix[0].length; n++) {
                        matrix[i][n] = 0;
                    }
                }
            }
        }
    }


    // 空间复杂度m + n
    public void setZeroes1(int[][] matrix) {

        // 行
        boolean[] row = new boolean[matrix.length];

        // 列
        boolean[] col = new boolean[matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 0) {
                    row[i] = true;
                    col[j] = true;
                }
            }
        }


        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (row[i] || col[j]) {
                    matrix[i][j] = 0;
                }
            }
        }

    }

    // 空间复杂度常量
    public void setZeroes2(int[][] matrix) {


    }

}
