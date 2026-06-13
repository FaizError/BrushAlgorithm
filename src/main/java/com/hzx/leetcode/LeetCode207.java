package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LeetCode207 {

    public boolean canFinish(int numCourses, int[][] prerequisites) {

        if(numCourses == 0) return true;

        // 存储每个课程学习完 可以学习的课程
        List<Integer>[]  graph = new List[numCourses];
        for(int i = 0; i < numCourses; i++){
            graph[i] = new ArrayList<>();
        }
        // 存储每个课程依赖的课程的数量
        int[] edges = new int[numCourses];

        for (int i = 0 ; i < prerequisites.length; i++){
            // [0,1] 想学1 先学0
            graph[prerequisites[i][0]].add(prerequisites[i][1]);
            edges[prerequisites[i][1]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++){
            if(edges[i] == 0){
                queue.add(i);
            }
        }

        int studyCount = 0;
        while(!queue.isEmpty()){
            Integer poll = queue.poll();
            studyCount++;
            for(int i : graph[poll]){
                if(--edges[i] == 0){
                    queue.add(i);
                }
            }
        }

        return studyCount == numCourses;
    }
}
