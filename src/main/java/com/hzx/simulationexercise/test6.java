package com.hzx.simulationexercise;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class test6 {

    /**
     * 题目描述
     * 一个微服务系统中有 N 个服务，编号 0 到 N-1。服务之间存在依赖关系：dependencies[i] = [a, b] 表示服务 a 依赖服务 b（即 b 必须先于 a 启动）。
     * 请判断是否存在一个合法的启动顺序。如果存在，返回字典序最小的启动序列；如果存在循环依赖则返回空数组。
     * 请实现函数：
     * 1
     * public int[] serviceOrder(int n, int[][] dependencies)
     * 输入
     * n：服务数量（1 ≤ N ≤ 10^5）
     * dependencies：依赖关系，每个元素 [a, b]（a 依赖 b）
     * 0 ≤ a, b < n，不存在重复的依赖关系
     * 输出
     * 返回字典序最小的拓扑序列，若存在环则返回空数组 []。
     * 示例
     * 1
     * 2
     * 3
     * 4
     * 5
     * 6
     * 7
     * 输入：n = 4, dependencies = [[1,0], [2,0], [3,1], [3,2]]
     * 输出：[0, 1, 2, 3]
     *
     * 解释：
     * 0 无依赖，最先启动
     * 1 依赖 0，2 依赖 0 → 按字典序 1 在 2 前
     * 3 依赖 1 和 2 → 最后启动
     * 1
     * 2
     * 3
     * 输入：n = 2, dependencies = [[1,0], [0,1]]
     * 输出：[]
     * 解释：0依赖1，1依赖0 → 死锁/环
     *
     * @param n
     * @param dependencies
     * @return
     */
    public int[] serviceOrder(int n, int[][] dependencies) {
        // 邻接表：graph[b] = list of a (b启动后，依赖它的a可以被启动)
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        int[] indegree = new int[n];
        for (int[] dependency : dependencies) {
            int a = dependency[0];
            int b = dependency[1];
            graph[b].add(a);
            indegree[a]++;
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                pq.offer(i);
            }
        }

        int idx = 0;
        int[] result = new int[n];

        while (!pq.isEmpty()) {
            Integer poll = pq.poll();
            result[idx++] = poll;

            for (int neighbor : graph[poll]) {
                indegree[neighbor]--;
                if (indegree[neighbor] == 0) {
                    pq.offer(neighbor);
                }
            }
        }

        if (idx < n) {
            // 有环
            return new int[0];
        }

        return result;
    }
}


