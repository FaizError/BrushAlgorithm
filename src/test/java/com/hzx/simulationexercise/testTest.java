package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例
 * 2. 边界条件（空数组、单元素、S极大极小等）
 * 3. 基本功能（全达标、全不达标、恰好等于等）
 * 4. ⚠️ Bug暴露：单元素子数组 nums[i] 不被判断（仅末位特殊处理）
 * 5. 综合场景
 */
@DisplayName("统计达标区间数量（子数组和 ≥ S）算法测试")
class testTest {

    private test solution;

    @BeforeEach
    void setUp() {
        solution = new test();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例: [1,2,3,4], S=5 → 5")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void example1() {
            // 达标子数组: [2,3],[1,2,3],[3,4],[2,3,4],[1,2,3,4]
            assertEquals(5, solution.countSubarrays(new int[]{1, 2, 3, 4}, 5));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("空数组 → 0")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void emptyArray() {
            assertEquals(0, solution.countSubarrays(new int[]{}, 5));
        }

        @Test
        @DisplayName("单元素 ≥ S → 1")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void singleElement_aboveS() {
            assertEquals(1, solution.countSubarrays(new int[]{5}, 3));
        }

        @Test
        @DisplayName("单元素 < S → 0")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void singleElement_belowS() {
            assertEquals(0, solution.countSubarrays(new int[]{3}, 5));
        }

        @Test
        @DisplayName("S = 1 → 所有子数组都达标（含单元素）")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void sEquals1_allSubarraysValid() {
            // [2,3,4]: 子数组总数 = 3*4/2 = 6，所有和都≥1
            assertEquals(6, solution.countSubarrays(new int[]{2, 3, 4}, 1));
        }

        @Test
        @DisplayName("S 超大 → 没有子数组达标")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void sVeryLarge_noneValid() {
            assertEquals(0, solution.countSubarrays(new int[]{1, 2, 3}, 100));
        }

        @Test
        @DisplayName("S 恰好等于某个单元素值")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void sEqualsSingleElement() {
            // [3, 1, 2], S=3
            // 达标: [3]=3✓, [3,1]=4✓, [3,1,2]=6✓, [1]=1✗, [1,2]=3✓, [2]=2✗
            // count=4
            assertEquals(4, solution.countSubarrays(new int[]{3, 1, 2}, 3));
        }
    }

    // ==================== 基本功能 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicFunctionality {

        @Test
        @DisplayName("全部子数组都达标")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void allSubarraysValid() {
            // [5,5,5]: 共6个子数组，每个都≥3
            assertEquals(6, solution.countSubarrays(new int[]{5, 5, 5}, 3));
        }

        @Test
        @DisplayName("全部子数组都不达标（除了空子数组不计）")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void noSubarrayValid() {
            // [1,1,1], S=10 → 0
            assertEquals(0, solution.countSubarrays(new int[]{1, 1, 1}, 10));
        }

        @Test
        @DisplayName("恰好等于阈值的边界情况")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void exactlyAtThreshold() {
            // [3,2], S=5
            // [3]=3<5✗, [3,2]=5≥5✓, [2]=2<5✗ → 1
            assertEquals(1, solution.countSubarrays(new int[]{3, 2}, 5));
        }

        @Test
        @DisplayName("连续多个短子数组达标")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void multipleShortSubarrays() {
            // [2,2,2,2], S=4
            // [0]=2✗, [0,1]=4✓, [0,2]=6✓, [0,3]=8✓ → 3
            // [1]=2✗, [1,2]=4✓, [1,3]=6✓ → 2
            // [2]=2✗, [2,3]=4✓ → 1
            // [3]=2✗ → 0
            // 总计 = 6
            assertEquals(6, solution.countSubarrays(new int[]{2, 2, 2, 2}, 4));
        }

        @Test
        @DisplayName("前缀逐渐累加才达标")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void prefixAccumulation() {
            // [1,1,1,1,1], S=3
            // [0]=1, [0,1]=2, [0,2]=3✓, [0,3]=4✓, [0,4]=5✓ → 3
            // [1]=1, [1,2]=2, [1,3]=3✓, [1,4]=4✓ → 2
            // [2]=1, [2,3]=2, [2,4]=3✓ → 1
            // [3]=1, [3,4]=2 → 0
            // [4]=1 → 0
            // 总计 = 6
            assertEquals(6, solution.countSubarrays(new int[]{1, 1, 1, 1, 1}, 3));
        }
    }

    // ==================== ⚠️ Bug 暴露 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露")
    class BugRevealing {

        @Test
        @DisplayName("⚠️ 遗漏单元素子数组：nums[0]=5≥4 应被计入但未计入")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void missingSingleElement_notLastPosition() {
            // [5, 1, 2], S=4
            // 达标子数组: [5]=5≥4✓, [5,1]=6≥4✓, [5,1,2]=8≥4✓, [1,2]=3<4✗
            // 期望=3
            // 代码实际: i=0时 sum=5不判断直接进while: [5,1]=6✓✓, [5,1,2]=8✓✓
            //          i=1: [1,2]=3<4✗, i==2?No, [1]不判断
            //          i=2: sum=2, while不进, i==2?Yes, 2≥4?✗
            //          结果=2（漏了 [5]）
            assertEquals(3, solution.countSubarrays(new int[]{5, 1, 2}, 4));
        }

        @Test
        @DisplayName("⚠️ 多个单元素都达标但全部漏掉")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void multipleSingleElementsMissed() {
            // [10, 20, 30], S=5
            // 所有子数组都≥5: 共n*(n+1)/2=6个
            // 代码: [10]=10不判断, [10,20]=30✓, [10,20,30]=60✓ → 2
            //      [20]=20不判断, [20,30]=50✓ → 1
            //      [30]=30 i==2末位判断✓ → 1
            //      结果=4（漏了 [10] 和 [20]）
            assertEquals(6, solution.countSubarrays(new int[]{10, 20, 30}, 5));
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class ComprehensiveScenarios {

        @Test
        @DisplayName("大值在两端")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void largeValuesAtEnds() {
            // [10, 1, 1, 10], S=11
            // [0]=10<11, [0,1]=11✓, [0,2]=12✓, [0,3]=22✓ → 3
            // [1]=1, [1,2]=2, [1,3]=12✓ → 1
            // [2]=1, [2,3]=11✓ → 1
            // [3]=10<11 → 0
            // 总计 = 5
            assertEquals(5, solution.countSubarrays(new int[]{10, 1, 1, 10}, 11));
        }

        @Test
        @DisplayName("全部元素相同，S恰为两倍元素值")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void uniformArray_sIsDouble() {
            // [3,3,3,3], S=6
            // [0]=3✗, [0,1]=6✓, [0,2]=9✓, [0,3]=12✓ → 3
            // [1]=3✗, [1,2]=6✓, [1,3]=9✓ → 2
            // [2]=3✗, [2,3]=6✓ → 1
            // [3]=3✗ → 0
            // 总计 = 6
            assertEquals(6, solution.countSubarrays(new int[]{3, 3, 3, 3}, 6));
        }

        @Test
        @DisplayName("递减数组")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void decreasingArray() {
            // [4,3,2,1], S=5
            // [0]=4<5, [0,1]=7✓, [0,2]=9✓, [0,3]=10✓ → 3
            // [1]=3<5, [1,2]=5✓, [1,3]=6✓ → 2
            // [2]=2<5, [2,3]=3<5 → 0
            // [3]=1<5 → 0
            // 总计 = 5
            assertEquals(5, solution.countSubarrays(new int[]{4, 3, 2, 1}, 5));
        }

        @Test
        @DisplayName("较大规模数组验证算法效率")
        @Timeout(value = 2, unit = TimeUnit.SECONDS)
        void largerArray() {
            // [1..20], S=100
            // sum(1..20)=210, 多数子数组达标
            // 正确答案可以用滑动窗口 O(n) 求出，这里先验证能跑通
            // 滑动窗口: 对于每个left，找到最小的right使sum[left..right]≥100
            int[] arr = new int[20];
            for (int i = 0; i < 20; i++) arr[i] = i + 1;

            // 计算正确答案
            int expected = 0;
            for (int left = 0; left < arr.length; left++) {
                int sum = 0;
                for (int right = left; right < arr.length; right++) {
                    sum += arr[right];
                    if (sum >= 100) {
                        // 一旦达标，right及之后的所有right都达标
                        expected += arr.length - right;
                        break;
                    }
                }
            }
            // expected = 55
            assertEquals(expected, solution.countSubarrays(arr, 100));
        }
    }
}
