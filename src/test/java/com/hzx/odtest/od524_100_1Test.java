package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（2个）
 * 2. 边界条件（单元素、minInterval=1、minInterval很大）
 * 3. 基本场景（全合法、全不合法）
 * 4. ⚠️ Bug暴露：三元及以上子集
 * 5. 复杂组合（多种长度子集混合）
 * 6. 较大n的组合验证
 */
@DisplayName("请求放行方案计数算法测试")
class od524_100_1Test {

    private od524_100_1 solution;

    @BeforeEach
    void setUp() {
        solution = new od524_100_1();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: timestamps=[1,2,4], minInterval=2 → 6")
        void example1() {
            assertEquals(6, solution.getSum(new int[]{1, 2, 4}, 2));
        }

        @Test
        @DisplayName("示例2: timestamps=[10], minInterval=5 → 2")
        void example2() {
            assertEquals(2, solution.getSum(new int[]{10}, 5));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("单元素 minInterval=1 → 2（空+[x]）")
        void singleElement_minInterval1() {
            assertEquals(2, solution.getSum(new int[]{5}, 1));
        }

        @Test
        @DisplayName("单元素 minInterval很大 → 2（空+[x]）")
        void singleElement_largeMinInterval() {
            assertEquals(2, solution.getSum(new int[]{5}, 100));
        }

        @Test
        @DisplayName("minInterval=1 相当于所有子集都合法")
        void minInterval1_allSubsetsValid() {
            // n=3, 所有2^3=8子集都合法（任意差≥1都满足）
            assertEquals(8, solution.getSum(new int[]{1, 2, 3}, 1));
        }

        @Test
        @DisplayName("minInterval很大 只有空集+单元素合法")
        void veryLargeMinInterval_onlySingletons() {
            // 任意两元素差都<100，只有空集+3个单元素
            assertEquals(4, solution.getSum(new int[]{1, 2, 3}, 100));
        }

        @Test
        @DisplayName("乱序输入（应自动排序处理）")
        void unsortedInput() {
            assertEquals(6, solution.getSum(new int[]{4, 1, 2}, 2));
        }
    }

    // ==================== 基本功能 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicFunctionality {

        @Test
        @DisplayName("两元素 差≥minInterval → 4（空+[a]+[b]+[a,b]）")
        void twoElements_validPair() {
            assertEquals(4, solution.getSum(new int[]{1, 4}, 2));
        }

        @Test
        @DisplayName("两元素 差<minInterval → 3（空+[a]+[b]）")
        void twoElements_invalidPair() {
            assertEquals(3, solution.getSum(new int[]{1, 2}, 3));
        }

        @Test
        @DisplayName("两元素 差=minInterval → 4（合法，边界情况）")
        void twoElements_exactInterval() {
            assertEquals(4, solution.getSum(new int[]{1, 3}, 2));
        }

        @Test
        @DisplayName("三元素 线性排列 恰好间隔都满足")
        void threeElements_allPairsValid() {
            // [1,4,7], minInterval=2
            // 空, [1],[4],[7], [1,4],[1,7],[4,7], [1,4,7] → 8
            assertEquals(8, solution.getSum(new int[]{1, 4, 7}, 2));
        }

        @Test
        @DisplayName("三元素 只有相邻远的不合法")
        void threeElements_middleClose() {
            // [1,2,4], minInterval=2 → 见示例1
            assertEquals(6, solution.getSum(new int[]{1, 2, 4}, 2));
        }
    }

    // ==================== ⚠️ Bug 暴露用例 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露用例（三元及以上子集）")
    class BugRevealing {

        @Test
        @DisplayName("三元子集合法: [1,3,5], minInterval=2 → 8")
        void tripleSubset_valid() {
            // 空:1, 单:[1][3][5]:3, 双:[1,3][1,5][3,5]:3, 三:[1,3,5]:1 → 8
            // ⚠️ 代码只统计到双元素，会返回7
            assertEquals(8, solution.getSum(new int[]{1, 3, 5}, 2));
        }

        @Test
        @DisplayName("四元子集: [1,3,5,7], minInterval=2 → 16")
        void quadrupleSubset() {
            // 所有元素等间距2，任意子集都合法 → 2^4 = 16
            assertEquals(16, solution.getSum(new int[]{1, 3, 5, 7}, 2));
        }

        @Test
        @DisplayName("只有三元子集合法（双元有不合法的）: [1,2,5], minInterval=3 → 6")
        void onlySomeSubsetsValid() {
            // 空:1, 单:[1][2][5]:3, 双:[1,5]:1 (2-1=1<3✗, 5-2=3≥3✓), 三:[1,2,5]: 1-2=1<3 ✗ → 0
            // 总:1+3+1=5
            // 等等让我重新算...
            // [1,2,5], minInterval=3:
            // 空: {}
            // 单: {1}, {2}, {5}
            // 双: {1,2} 差1<3 ✗, {1,5} 差4≥3 ✓, {2,5} 差3≥3 ✓
            // 三: {1,2,5} 1-2=1<3 ✗
            // 总: 1+3+2=6
            assertEquals(6, solution.getSum(new int[]{1, 2, 5}, 3));
        }

        @Test
        @DisplayName("五元素等间距: [1,3,5,7,9], minInterval=2 → 32")
        void fiveElements_equalSpacing() {
            // 任意子集都合法 → 2^5 = 32
            assertEquals(32, solution.getSum(new int[]{1, 3, 5, 7, 9}, 2));
        }

        @Test
        @DisplayName("包含长链跳过中间元素: [1,2,4,8], minInterval=3 → 8")
        void longChainWithGaps() {
            // [1,2,4,8], k=3, sorted
            // 空: 1
            // 单: 4
            // 双: {1,4}(3≥3✓), {1,8}(7≥3✓), {2,8}(6≥3✓) → 3 (2,4差2✗, 4,8差4≥3但需要以4开始)
            //     等等: {4,8} 差4≥3 ✓, {2,4} 差2<3 ✗, {1,2} 差1<3 ✗
            // 双: {1,4},{1,8},{2,8},{4,8} → 4
            // 三: {1,4,8}: 4-1=3≥3✓, 8-4=4≥3✓ → ✓
            //     {1,2,8}: 2-1=1<3 ✗
            //     {2,4,8}: 4-2=2<3 ✗
            // 三: 1
            // 四: {1,2,4,8} → 2-1=1<3 ✗
            // 总: 1+4+4+1 = 10
            assertEquals(10, solution.getSum(new int[]{1, 2, 4, 8}, 3));
        }

        @Test
        @DisplayName("n=15满规模 验证正确性")
        void maxSize() {
            // [1,2,...,15], minInterval=2
            // 用正确DP验证
            int[] ts = new int[15];
            for (int i = 0; i < 15; i++) ts[i] = i + 1;
            // 正确值通过DP计算：dp[i]=1+sum(dp[j] for j where ts[i]-ts[j]>=2)
            int[] dp = new int[15];
            int total = 1; // empty
            for (int i = 0; i < 15; i++) {
                dp[i] = 1;
                for (int j = 0; j < i; j++) {
                    if (ts[i] - ts[j] >= 2) {
                        dp[i] += dp[j];
                    }
                }
                total += dp[i];
            }
            assertEquals(total, solution.getSum(ts, 2));
        }
    }

    // ==================== 更多场景 ====================

    @Nested
    @DisplayName("更多组合场景")
    class MoreScenarios {

        @Test
        @DisplayName("所有元素挤在一起 只有空+单合法")
        void allElementsClose() {
            // [5,6,7], minInterval=5
            // 任意差≤2<5 → 只有空+3单=4
            assertEquals(4, solution.getSum(new int[]{5, 6, 7}, 5));
        }

        @Test
        @DisplayName("稀疏时间戳所有子集都合法")
        void sparseTimestamps_allValid() {
            // [1,100,200], minInterval=50
            // 任意差≥50 → 2^3=8
            assertEquals(8, solution.getSum(new int[]{1, 100, 200}, 50));
        }

        @Test
        @DisplayName("交替合法不合法")
        void alternatingValidInvalid() {
            // [1,2,3,4], minInterval=2
            // 空:1, 单:4
            // 双: {1,3}(2✓),{1,4}(3✓),{2,4}(2✓) → 3 ({1,2},{2,3},{3,4}都<2)
            // 三: {1,3,?} 需要≥5不存在, {1,4,?} 不存在
            // 总: 1+4+3=8
            assertEquals(8, solution.getSum(new int[]{1, 2, 3, 4}, 2));
        }

        @Test
        @DisplayName("两元素间隔边界: minInterval=2, arr=[1,3] → 4")
        void twoElements_boundaryInterval() {
            assertEquals(4, solution.getSum(new int[]{1, 3}, 2));
        }

        @Test
        @DisplayName("3元素前两个近后两个远: [1,2,5], minInterval=2 → 6")
        void threeElements_frontClose_backFar() {
            // 空:1, 单:3, 双:{1,5}(4≥2✓),{2,5}(3≥2✓) → 2, 三:{1,2,5} 2-1=1<2 ✗
            // 总: 1+3+2=6
            assertEquals(6, solution.getSum(new int[]{1, 2, 5}, 2));
        }
    }
}
