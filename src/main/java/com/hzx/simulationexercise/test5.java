package com.hzx.simulationexercise;

import java.util.ArrayDeque;

public class test5 {

    /**
     * 题目描述
     * 数据中心有一个 M × N 的机房网格，每个格子可能是：
     * ● 0：空闲位置，可通行
     * ● 1：障碍物，不可通行
     * ● 2：起点（网络入口，只有1个）
     * ● 3：终点（目标区域，只有1个）
     * ● 4：信号增强器
     * 现在需要从起点铺设一条网线到终点。网线只能沿上下左右四个方向走空闲位置。每经过一个空闲位置，铺设成本为 1。起点和终点不计算成本。
     * 此外，机房中存在 K 个信号增强器（用 4 标记）。经过增强器时，后续连续 2 步的移动成本变为 0（增强效果不能叠加，取最新的一次）。
     * 请计算从起点到终点的最低铺设成本。如果无法到达，返回 -1。
     * 请实现函数：
     * public int minWireCost(int[][] grid)
     * 输入
     * ● grid：M × N 网格（1 ≤ M, N ≤ 500），元素 ∈ {0,1,2,3,4}
     * ● 保证恰好有一个起点(2)和一个终点(3)
     * ● K（增强器数量）≤ 10
     * 输出
     * 返回最低成本（整数），不可达返回 -1。
     *
     * 示例
     * 输入：
     * grid = [
     * [2, 0, 0, 1],
     * [0, 1, 4, 0],
     * [0, 0, 0, 3]
     * ]
     * 输出：2
     *
     * 解释：
     * 路径 (0,0)→(0,1)[成本1]→(0,2)[成本1]→(1,2)[增强器,buff=2]
     * →(1,3)[buff>0,成本0]→(2,3)[buff>0,成本0]→终点
     * 总成本 = 1 + 1 + 0 + 0 = 2
     * 解题思路
     */

    // 方向数组：上下左右
    private static final int[][] DIRS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public int minWireCost(int[][] grid) {

        // 先找到起点
        int sr = -1, sc = -1, er = -1, ec = -1;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    sr = i;
                    sc = j;
                }
                if (grid[i][j] == 3) {
                    er = i;
                    ec = j;
                }
            }
        }

        boolean[][][] visited = new boolean[grid.length][grid[0].length][3];
        ArrayDeque<int[]> queue = new ArrayDeque<>();

        queue.offer(new int[]{sr, sc, 0, 0});
        visited[sr][sc][0] = true;


        while (!queue.isEmpty()) {

            int[] poll = queue.poll();
            int row = poll[0];
            int col = poll[1];
            int buff = poll[2];
            int cost = poll[3];

            if (row == er && col == ec) {
                return cost;
            }

            for (int[] d : DIRS) {
                int nr = row + d[0];
                int nc = col + d[1];

                if (nr < 0 || nr >= grid.length || nc < 0 || nc >= grid[0].length) {
                    continue;
                }

                if (grid[nr][nc] == 1) {
                    continue;
                }

                int stepCost = (buff > 0 || grid[nr][nc] != 0) ? 0 : 1;
                int newCost = cost + stepCost;

                int newBuff = Math.max(0, buff - 1);
                if (grid[nr][nc] == 4) {
                    newBuff = 2;
                }

                if (!visited[nr][nc][newBuff]) {
                    visited[nr][nc][newBuff] = true;

                    if (stepCost == 0) {
                        queue.offerFirst(new int[]{nr, nc, newBuff, newCost});
                    } else {
                        queue.offerLast(new int[]{nr, nc, newBuff, newCost});
                    }
                }

            }

        }

        return -1;
    }

}
