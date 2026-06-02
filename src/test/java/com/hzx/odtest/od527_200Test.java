package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（3个）
 * 2. 边界条件（m=1, m=0, 全0需求, k=1）
 * 3. 基本场景（最优选两端的、跳过中间低值的）
 * 4. 距离约束的严格测试
 * 5. ⚠️ Bug 暴露用例
 * 6. 较大规模组合
 */
@DisplayName("充电站布局最大需求算法测试")
class od527_200Test {

    private od527_200 solution;

    @BeforeEach
    void setUp() {
        solution = new od527_200();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: n=5,m=2,k=3,arr=[10,20,30,40,50] → 70")
        void example1() {
            assertEquals(70, solution.maxChargeSum(5, 2, 3, new int[]{10, 20, 30, 40, 50}));
        }

        @Test
        @DisplayName("示例2: n=5,m=2,k=2,arr=[5,10,5,10,5] → 20")
        void example2() {
            assertEquals(20, solution.maxChargeSum(5, 2, 2, new int[]{5, 10, 5, 10, 5}));
        }

        @Test
        @DisplayName("示例3: n=1,m=1,k=1,arr=[10] → 10")
        void example3() {
            assertEquals(10, solution.maxChargeSum(1, 1, 1, new int[]{10}));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("m=0 → 0")
        void zeroStations() {
            assertEquals(0, solution.maxChargeSum(5, 0, 2, new int[]{10, 20, 30, 40, 50}));
        }

        @Test
        @DisplayName("m=1 选最大需求的单个区域")
        void singleStation_pickMax() {
            assertEquals(50, solution.maxChargeSum(5, 1, 3, new int[]{10, 20, 30, 40, 50}));
        }

        @Test
        @DisplayName("m=1 全部相同需求")
        void singleStation_allSame() {
            assertEquals(5, solution.maxChargeSum(4, 1, 2, new int[]{5, 5, 5, 5}));
        }

        @Test
        @DisplayName("所有需求为0")
        void allZeroDemand() {
            assertEquals(0, solution.maxChargeSum(5, 3, 2, new int[]{0, 0, 0, 0, 0}));
        }

        @Test
        @DisplayName("k=1 相邻区域可选")
        void minDistanceOne() {
            // 可选任意相邻区域，选最大的m个
            assertEquals(9, solution.maxChargeSum(4, 3, 1, new int[]{3, 1, 4, 2}));
            // 最大3个: 4+3+2=9
        }

        @Test
        @DisplayName("单个区域选m=1")
        void singleRegion_oneStation() {
            assertEquals(100, solution.maxChargeSum(1, 1, 1, new int[]{100}));
        }
    }

    // ==================== 基本场景 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicScenarios {

        @Test
        @DisplayName("最优选两端（距离刚好够）")
        void pickEnds() {
            // m=2, k=3, 只有(0,3)满足距离≥3
            assertEquals(110, solution.maxChargeSum(4, 2, 3, new int[]{100, 1, 1, 10}));
        }

        @Test
        @DisplayName("跳过中间低值选高值")
        void skipLowValues() {
            // arr=[10,1,1,40,1,50], m=2, k=4
            // (0,5): 10+50=60, (1,5): 1+50=51, (2,5): 1+50=51
            assertEquals(60, solution.maxChargeSum(6, 2, 4, new int[]{10, 1, 1, 40, 1, 50}));
        }

        @Test
        @DisplayName("密集区域选择")
        void denseSelection() {
            // n=7, m=3, k=3, arr=[10,1,1,10,1,1,10]
            // 最优: (0,3,6) → 30
            assertEquals(30, solution.maxChargeSum(7, 3, 3, new int[]{10, 1, 1, 10, 1, 1, 10}));
        }

        @Test
        @DisplayName("需求递减但必须选后面的")
        void decreasingDemand() {
            // 只能选(0,2,4)距离各为2
            assertEquals(15, solution.maxChargeSum(5, 3, 2, new int[]{10, 0, 5, 0, 1}));
        }

        @Test
        @DisplayName("最小间距恰好满足")
        void exactMinDistance() {
            // (0,2): distance=2 ≥ 2 ✓, sum=5+5=10. (1,2): distance=1 ✗
            assertEquals(10, solution.maxChargeSum(3, 2, 2, new int[]{5, 3, 5}));
        }
    }

    // ==================== 距离约束测试 ====================

    @Nested
    @DisplayName("距离约束严格性")
    class DistanceConstraint {

        @Test
        @DisplayName("k很大必须间隔很远")
        void largeK() {
            // n=6,m=2,k=5, arr=[1,2,3,4,5,6]
            // (0,5): 1+6=7, (1,6): 2+? no, only 6 positions
            assertEquals(7, solution.maxChargeSum(6, 2, 5, new int[]{1, 2, 3, 4, 5, 6}));
        }

        @Test
        @DisplayName("k等于n-1只有两端可选")
        void kEqualsNMinusOne() {
            // n=4,m=2,k=3, (0,3): 10+40=50. (1,?): 1→? distance≥3 → ?=4不存在
            assertEquals(50, solution.maxChargeSum(4, 2, 3, new int[]{10, 1, 1, 40}));
        }

        @Test
        @DisplayName("贪心选最高值但受距离约束")
        void greedyConstrained() {
            // 最高的是idx1=100，但选了idx1后距离≥3只能选idx4=20，sum=120
            // 选idx0=80, idx3=60, distance=3≥3, sum=140 ← 更优
            assertEquals(140, solution.maxChargeSum(5, 2, 3, new int[]{80, 100, 10, 60, 20}));
        }
    }

    // ==================== Bug 暴露用例 ⚠️ ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露用例（DP else分支设值错误）")
    class BugRevealing {

        @Test
        @DisplayName("Bug反例: n=5,m=3,k=2,arr=[5,10,5,10,5] → 期望15, 代码可能返回20")
        void bug_counterExample1() {
            // 正确：3站唯一可行组合 (0,2,4) → 5+5+5=15
            // Bug: 代码可能返回20（2站(1,3)的max值被错误传播）
            assertEquals(15, solution.maxChargeSum(5, 3, 2, new int[]{5, 10, 5, 10, 5}));
        }

        @Test
        @DisplayName("Bug变体: 高值集中在无法同时选的位置")
        void bug_variant1() {
            // n=6,m=3,k=2, arr=[100,100,1,100,1,1]
            // 正确：(0,2,4): 100+1+1=102, (1,3,5): 100+100+1=201 ← 最优
            assertEquals(201, solution.maxChargeSum(6, 3, 2,
                    new int[]{100, 100, 1, 100, 1, 1}));
        }

        @Test
        @DisplayName("Bug变体: 小k但m较大")
        void bug_variant2() {
            // n=7,m=4,k=2, arr=[1,9,1,9,1,9,1]
            // 可行: (1,3,5,?): need 4th at 7 → OK (indices 1,3,5,6 sum=9+9+9+1=28)
            // 或: (1,3,5,?): (1,3,5,7) no position 7. (0,2,4,6): 1+1+1+1=4
            // Best: (1,3,5,6): 9+9+9+1=28
            assertEquals(28, solution.maxChargeSum(7, 4, 2,
                    new int[]{1, 9, 1, 9, 1, 9, 1}));
        }

        @Test
        @DisplayName("Bug变体: 第一个区域需求最高但被迫跳过")
        void bug_variant3() {
            // n=6,m=2,k=3, arr=[100,1,1,10,1,100]
            // (0,3): 100+10=110. (0,5): 100+100=200 ✓ (dist=5≥3)
            assertEquals(200, solution.maxChargeSum(6, 2, 3,
                    new int[]{100, 1, 1, 10, 1, 100}));
        }
    }

    // ==================== 更多组合场景 ====================

    @Nested
    @DisplayName("多种组合")
    class MoreCombinations {

        @Test
        @DisplayName("需求单调递增选后面的")
        void increasingDemand() {
            // n=6,m=2,k=3, arr=[10,20,30,40,50,60]
            // (0,3): 10+40=50, (1,4): 20+50=70, (2,5): 30+60=90 ✓
            assertEquals(90, solution.maxChargeSum(6, 2, 3,
                    new int[]{10, 20, 30, 40, 50, 60}));
        }

        @Test
        @DisplayName("需求单调递减选前面的")
        void decreasingDemand_pickFront() {
            // n=6,m=2,k=3, arr=[60,50,40,30,20,10]
            // (0,3): 60+30=90 ✓
            assertEquals(90, solution.maxChargeSum(6, 2, 3,
                    new int[]{60, 50, 40, 30, 20, 10}));
        }

        @Test
        @DisplayName("m=n需要所有区域（k=1时）")
        void allRegions_kEquals1() {
            assertEquals(15, solution.maxChargeSum(5, 5, 1,
                    new int[]{1, 2, 3, 4, 5}));
        }

        @Test
        @DisplayName("三站精确间隔")
        void threeStations_exactSpacing() {
            // n=7,m=3,k=3, (0,3,6): sum = 10+20+30=60
            assertEquals(60, solution.maxChargeSum(7, 3, 3,
                    new int[]{10, 1, 1, 20, 1, 1, 30}));
        }
    }

    // ==================== 较大规模测试 ====================

    @Nested
    @DisplayName("较大规模")
    class LargerScale {

        @Test
        @DisplayName("n接近200，简单验证")
        void mediumSized() {
            // 构造：每k个位置放一个高值
            int n = 100, m = 10, k = 10;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) {
                arr[i] = (i % k == 0) ? 100 : 1;
            }
            // 最优：选所有k的倍数位置（10个），每个值100，sum=1000
            assertEquals(1000, solution.maxChargeSum(n, m, k, arr));
        }

        @Test
        @DisplayName("全高值随便选")
        void allHighValues() {
            int n = 10, m = 5, k = 2;
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = 10;
            assertEquals(50, solution.maxChargeSum(n, m, k, arr));
        }
    }
}
