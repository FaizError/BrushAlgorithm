package com.hzx.leetcode;

import java.util.ArrayDeque;
import java.util.Queue;

public class LeetCode994 {


    public int orangesRotting(int[][] grid) {

        // 新鲜的橘子
        int fresh = 0;

        Queue<int[]> queue = new ArrayDeque();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    fresh++;
                }
                if (grid[i][j] == 2) {
                    queue.offer(new int[]{i, j});
                }
            }
        }

        int time = 0;

        while (!queue.isEmpty() && fresh > 0) {
            int size = queue.size();
            boolean change = false;
            for (int i = 0; i < size; i++) {
                int[] poll = queue.poll();
                int row = poll[0];
                int col = poll[1];
                if (row - 1 >= 0 && grid[row - 1][col] == 1) {
                    grid[row - 1][col] = 2;
                    fresh--;
                    change = true;
                    queue.offer(new int[]{row - 1, col});
                }
                if (row + 1 < grid.length && grid[row + 1][col] == 1) {
                    grid[row + 1][col] = 2;
                    fresh--;
                    change = true;
                    queue.offer(new int[]{row + 1, col});
                }
                if (col - 1 >= 0 && grid[row][col - 1] == 1) {
                    grid[row][col - 1] = 2;
                    fresh--;
                    change = true;
                    queue.offer(new int[]{row, col - 1});
                }
                if (col + 1 < grid[0].length && grid[row][col + 1] == 1) {
                    grid[row][col + 1] = 2;
                    fresh--;
                    change = true;
                    queue.offer(new int[]{row, col + 1});
                }
            }

            if (change) {
                time++;
            }
        }

        return fresh > 0 ? -1 : time;
    }

}
