package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例
 * 2. 边界条件（数组长度<k、k=1、恰好=k等）
 * 3. 基本功能（全达标、全不达标、部分达标）
 * 4. ⚠️ Bug暴露：非对齐位置窗口被贪心计入导致 count 偏大/偏小
 * 5. 较大数据量验证
 */
@DisplayName("峰值流量告警阈值算法测试")
class test1Test {

    private test1 solution;

    @BeforeEach
    void setUp() {
        solution = new test1();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例: requests=[1,3,2,5,4,6,2,1], k=3, t=8 → [15, 1]")
        void example1() {
            assertArrayEquals(
                new int[]{15, 1},
                solution.peakTraffic(new int[]{1, 3, 2, 5, 4, 6, 2, 1}, 3, 8)
            );
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("数组长度 < k → 返回 [0, 0]")
        void arrayShorterThanK() {
            assertArrayEquals(
                new int[]{0, 0},
                solution.peakTraffic(new int[]{1, 2}, 3, 5)
            );
        }

        @Test
        @DisplayName("数组长度 == k 且 sum ≥ t → maxSum=sum, count=1")
        void arrayLengthEqualsK_aboveThreshold() {
            assertArrayEquals(
                new int[]{18, 1},
                solution.peakTraffic(new int[]{5, 6, 7}, 3, 10)
            );
        }

        @Test
        @DisplayName("数组长度 == k 且 sum < t → maxSum=sum, count=0")
        void arrayLengthEqualsK_belowThreshold() {
            assertArrayEquals(
                new int[]{6, 0},
                solution.peakTraffic(new int[]{1, 2, 3}, 3, 10)
            );
        }

        @Test
        @DisplayName("k = 1 → 每个元素即为一个窗口")
        void kEquals1() {
            // maxSum = max(1,5,2,3) = 5
            // 固定块：[0]=1<3✗, [1]=5≥3✓, [2]=2<3✗, [3]=3≥3✓ → count=2
            assertArrayEquals(
                new int[]{5, 2},
                solution.peakTraffic(new int[]{1, 5, 2, 3}, 1, 3)
            );
        }

        @Test
        @DisplayName("t = 0 → 所有窗口都达标")
        void thresholdZero() {
            // maxSum: [1,2]=3, [2,1]=3, [1,2]=3 → 3
            // 固定块：[0-1]=3≥0✓, [2-3]=3≥0✓ → count=2
            assertArrayEquals(
                new int[]{3, 2},
                solution.peakTraffic(new int[]{1, 2, 1, 2}, 2, 0)
            );
        }

        @Test
        @DisplayName("t 非常大 → 所有窗口都不达标")
        void thresholdVeryLarge() {
            // maxSum: [10,20]=30, [20,30]=50, [30,40]=70 → 70
            // count=0（全部 < 1000）
            assertArrayEquals(
                new int[]{70, 0},
                solution.peakTraffic(new int[]{10, 20, 30, 40}, 2, 1000)
            );
        }

        @Test
        @DisplayName("空数组")
        void emptyArray() {
            assertArrayEquals(
                new int[]{0, 0},
                solution.peakTraffic(new int[]{}, 3, 5)
            );
        }

        @Test
        @DisplayName("单元素数组 k=1")
        void singleElement_k1() {
            assertArrayEquals(
                new int[]{7, 1},
                solution.peakTraffic(new int[]{7}, 1, 5)
            );
        }
    }

    // ==================== 基本功能 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicFunctionality {

        @Test
        @DisplayName("所有固定块都达标")
        void allBlocksAboveThreshold() {
            // 固定块: [0-1]=5+5=10≥8✓, [2-3]=5+5=10≥8✓, [4-5]=5+5=10≥8✓ → count=3
            // maxSum=10
            assertArrayEquals(
                new int[]{10, 3},
                solution.peakTraffic(new int[]{5, 5, 5, 5, 5, 5}, 2, 8)
            );
        }

        @Test
        @DisplayName("所有固定块都不达标")
        void allBlocksBelowThreshold() {
            // maxSum: [1,1]=2, [1,1]=2, [1,1]=2 → 2
            // 固定块: [0-1]=2<5✗, [2-3]=2<5✗ → count=0
            assertArrayEquals(
                new int[]{2, 0},
                solution.peakTraffic(new int[]{1, 1, 1, 1}, 2, 5)
            );
        }

        @Test
        @DisplayName("首尾块达标中间不达标")
        void firstAndLastBlocksAboveThreshold() {
            // 固定块: [0-1]=10+10=20≥15✓, [2-3]=1+1=2<15✗, [4-5]=10+10=20≥15✓ → count=2
            // maxSum=20
            assertArrayEquals(
                new int[]{20, 2},
                solution.peakTraffic(new int[]{10, 10, 1, 1, 10, 10}, 2, 15)
            );
        }

        @Test
        @DisplayName("只有中间块达标")
        void onlyMiddleBlockAboveThreshold() {
            // 滑动窗口max: [1,1]=2, [1,10]=11, [10,1]=11, [1,1]=2 → 11
            // 固定块: [0-1]=2<8✗, [2-3]=11≥8✓, [4-5]=2<8✗ → count=1
            assertArrayEquals(
                new int[]{11, 1},
                solution.peakTraffic(new int[]{1, 1, 10, 1, 1, 1}, 2, 8)
            );
        }

        @Test
        @DisplayName("恰好等于阈值 → 达标")
        void exactlyAtThreshold() {
            // 滑动窗口max: [1,4]=5, [4,5]=9 → 9
            // 固定块: [0-1]=5≥5✓, [2-3]=9≥5✓, [4]仅剩1个不够k=2 → count=2
            // 实际上 array length=5, k=2: blocks at [0-1],[2-3], index 4 left → count最多2
            assertArrayEquals(
                new int[]{9, 2},
                solution.peakTraffic(new int[]{1, 4, 4, 5, 1}, 2, 5)
            );
        }

        @Test
        @DisplayName("尾部不足k个元素的块不计入")
        void trailingIncompleteBlock() {
            // 固定块: [0-1]=3+3=6≥5✓, [2-3]=3+3=6≥5✓, [4]只剩1个 → count=2
            // maxSum=6
            assertArrayEquals(
                new int[]{6, 2},
                solution.peakTraffic(new int[]{3, 3, 3, 3, 3}, 2, 5)
            );
        }
    }

    // ==================== ⚠️ Bug 暴露用例 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露用例（非对齐位置窗口被贪心计入）")
    class BugRevealing {

        @Test
        @DisplayName("⚠️ 贪心偏移：达标窗口在非对齐位置导致 count 偏大")
        void greedyOffAlignment_overcount() {
            // 题目原例：requests=[1,3,2,5,4,6,2,1], k=3, t=8
            // 预期固定块: [0-2]=6<8✗, [3-5]=15≥8✓, [6+]不够 → count=1
            // ⚠️ 贪心会在 index=1 处找到 [3,2,5]=10≥8 并计入，导致 count 偏大
            assertArrayEquals(
                new int[]{15, 1},  // 正确答案
                solution.peakTraffic(new int[]{1, 3, 2, 5, 4, 6, 2, 1}, 3, 8)
            );
        }

        @Test
        @DisplayName("⚠️ 贪心偏移导致后续合法块被跳过 → count 偏小")
        void greedyOffAlignment_undercount() {
            // requests=[1,2,10,1,2,10], k=3, t=12
            // 固定块: [0-2]=1+2+10=13≥12✓, [3-5]=1+2+10=13≥12✓ → count=2
            // maxSum: [0-2]=13, [1-3]=13, [2-4]=13, [3-5]=13 → 13
            // ⚠️ 贪心: 初始[0-2]=13→count=1 index=2
            //        left=0: [1-3]=13, 0>2?No 跳过
            //        left=1: [2-4]=13, 1>2?No 跳过
            //        left=2: [3-5]=13, 2>2?No 跳过 → count=1（错误）
            assertArrayEquals(
                new int[]{13, 2},  // 正确答案
                solution.peakTraffic(new int[]{1, 2, 10, 1, 2, 10}, 3, 12)
            );
        }

        @Test
        @DisplayName("⚠️ 所有达标窗口都在非对齐位置 → count=0 但贪心返回 >0")
        void allQualifyingWindowsOffAlignment() {
            // requests=[0,5,5,0,5,5,0], k=3, t=10
            // 固定块: [0-2]=0+5+5=10≥10✓, [3-5]=0+5+5=10≥10✓, [6]不够 → count=2
            // 滑动窗口max: [0-2]=10, [1-3]=5+5+0=10, [2-4]=5+0+5=10, [3-5]=10, [4-6]=5+0+5=10 → 10
            // ⚠️ 贪心: 初始[0-2]=10→count=1 index=2
            //        left=0: [1-3]=10, 0>2?No
            //        left=1: [2-4]=10, 1>2?No
            //        left=2: [3-5]=10, 2>2?No → count=1（错误）
            assertArrayEquals(
                new int[]{10, 2},  // 正确答案
                solution.peakTraffic(new int[]{0, 5, 5, 0, 5, 5, 0}, 3, 10)
            );
        }

        @Test
        @DisplayName("⚠️ 贪心从非对齐位置开始计数导致漏掉后续对齐块")
        void greedySkipsAlignmentBlock() {
            // requests=[2,8,2,8,8,8], k=2, t=10
            // 固定块: [0-1]=10≥10✓, [2-3]=10≥10✓, [4-5]=16≥10✓ → count=3
            // maxSum: [0-1]=10, [1-2]=10, [2-3]=10, [3-4]=16, [4-5]=16 → 16
            // ⚠️ 贪心: 初始[0-1]=10→count=1 index=1
            //        left=0: [1-2]=10, 0>1?No 跳过
            //        left=1: [2-3]=10, 1>1?No 跳过
            //        left=2: [3-4]=16, 2>1?Yes→count=2 index=3
            //        left=3: [4-5]=16, 3>3?No 跳过 → count=2（错误）
            assertArrayEquals(
                new int[]{16, 3},  // 正确答案
                solution.peakTraffic(new int[]{2, 8, 2, 8, 8, 8}, 2, 10)
            );
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class ComprehensiveScenarios {

        @Test
        @DisplayName("全部零值数组")
        void allZeros() {
            // maxSum=0, 所有块sum=0, t=0时达标
            assertArrayEquals(
                new int[]{0, 3},
                solution.peakTraffic(new int[]{0, 0, 0, 0, 0, 0}, 2, 0)
            );
        }

        @Test
        @DisplayName("负值数组（如果业务允许）")
        void negativeValues() {
            // 固定块: [0-1]=-1+2=1<3✗, [2-3]=-1+4=3≥3✓, [4]不够 → count=1
            // maxSum: [-1,2]=1, [2,-1]=1, [-1,4]=3, [4,5]=9 → 9
            assertArrayEquals(
                new int[]{9, 1},
                solution.peakTraffic(new int[]{-1, 2, -1, 4, 5}, 2, 3)
            );
        }

        @Test
        @DisplayName("大k值（窗口覆盖大部分数组）")
        void largeK() {
            // k=4, len=6: blocks: [0-3]=1+2+3+4=10≥8✓, [4-5]不够 → count=1
            // maxSum: [0-3]=10, [1-4]=2+3+4+5=14, [2-5]=3+4+5+6=18 → 18
            assertArrayEquals(
                new int[]{18, 1},
                solution.peakTraffic(new int[]{1, 2, 3, 4, 5, 6}, 4, 8)
            );
        }

        @Test
        @DisplayName("较大n等间距数组 验证maxSum正确性")
        void largeArray_maxSum() {
            // 递增数组 [1..20], k=5
            // maxSum = 最后5个: 16+17+18+19+20 = 90
            // 固定块: [0-4]=15<50✗, [5-9]=40<50✗, [10-14]=65≥50✓, [15-19]=85≥50✓ → count=2
            int[] arr = new int[20];
            for (int i = 0; i < 20; i++) arr[i] = i + 1;
            assertArrayEquals(
                new int[]{90, 2},
                solution.peakTraffic(arr, 5, 50)
            );
        }

        @Test
        @DisplayName("极端值混合")
        void extremeValues() {
            // requests=[100, 0, 100, 0, 100, 0], k=2, t=100
            // 固定块: [0-1]=100≥100✓, [2-3]=100≥100✓, [4-5]=100≥100✓ → count=3
            // maxSum: [100,0]=100, [0,100]=100, ... → 100
            assertArrayEquals(
                new int[]{100, 3},
                solution.peakTraffic(new int[]{100, 0, 100, 0, 100, 0}, 2, 100)
            );
        }
    }
}
