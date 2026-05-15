package com.hzx.leetcode;

import java.util.*;

public class LeetCode15 {

    public List<List<Integer>> threeSum(int[] nums) {

        Arrays.sort(nums);

        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < nums.length - 2; i++) {
            int m =  i + 1;
            int n = nums.length - 1;
            while (m < n) {

                if(nums[i] + nums[m] + nums[n] == 0){
                    List<Integer> list = new ArrayList<>();
                    list.add(nums[i]);
                    list.add(nums[m]);
                    list.add(nums[n]);
                    res.add(list);
                    m++;
                    n--;
                }else if(nums[i] + nums[m] + nums[n] > 0){
                    n--;
                }else{
                    m++;
                }

            }
        }

        Set<List<Integer>> set = new HashSet<>(res);
        return new ArrayList<>(set);
    }

    public List<List<Integer>> threeSum2(int[] nums) {
        // 排序数组（核心前提）
        Arrays.sort(nums);
        int n = nums.length;

        // 返回自定义的AbstractList子类
        return new AbstractList<List<Integer>>() {
            // 存储符合条件的三元组（懒加载时初始化）
            private List<List<Integer>> result;

            // 初始化结果集（懒加载核心：第一次调用size/get时执行）
            private void init() {
                if (result != null) return;
                result = new ArrayList<>();

                // 遍历固定第一个数nums[i]
                for (int i = 0; i < n - 2; i++) {
                    // 跳过重复的第一个数（去重）
                    if (i > 0 && nums[i] == nums[i - 1]) continue;
                    // 第一个数大于0，后续数都≥它，和不可能为0，直接终止
                    if (nums[i] > 0) break;

                    int left = i + 1; // 左指针
                    int right = n - 1; // 右指针

                    while (left < right) {
                        int sum = nums[i] + nums[left] + nums[right];
                        if (sum == 0) {
                            // 找到符合条件的三元组，加入结果
                            result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                            // 跳过左指针重复值（去重）
                            while (left < right && nums[left] == nums[left + 1]) left++;
                            // 跳过右指针重复值（去重）
                            while (left < right && nums[right] == nums[right - 1]) right--;

                            // 移动指针找下一组可能
                            left++;
                            right--;
                        } else if (sum < 0) {
                            // 和太小，左指针右移增大和
                            left++;
                        } else {
                            // 和太大，右指针左移减小和
                            right--;
                        }
                    }
                }
            }

            // 重写size方法：返回结果集大小
            @Override
            public int size() {
                init();
                return result.size();
            }

            // 重写get方法：获取指定索引的三元组
            @Override
            public List<Integer> get(int index) {
                init();
                return result.get(index);
            }
        };
    }
}
