package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * 测试用例覆盖：
 * 1. 边界条件（空数组、单任务、全重叠、全不重叠等）
 * 2. 基本功能（贪心恰好得到最优解的场景）
 * 3. ⚠️ Bug暴露①：按开始时间排序导致遗漏更多任务（应排序结束时间）
 * 4. ⚠️ Bug暴露②：贪心优先选第一个可用任务导致总占用时长非最短
 * 5. 综合场景
 */
@DisplayName("任务调度（最多任务 + 最短总占用时长）算法测试")
class test2Test {

    private test2 solution;

    @BeforeEach
    void setUp() {
        solution = new test2();
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("空数组 → [0, 0]")
        void emptyArray() {
            assertArrayEquals(
                    new int[]{0, 0},
                    solution.bestSchedule(new int[][]{})
            );
        }

        @Test
        @DisplayName("单任务 → count=1, sum=duration")
        void singleTask() {
            // [5,10] duration=5
            assertArrayEquals(
                    new int[]{1, 5},
                    solution.bestSchedule(new int[][]{{5, 10}})
            );
        }

        @Test
        @DisplayName("两个完全不重叠的任务（中间有间隙）")
        void twoNonOverlappingWithGap() {
            // [1,2] duration=1, [5,6] duration=1
            assertArrayEquals(
                    new int[]{2, 2},
                    solution.bestSchedule(new int[][]{{1, 2}, {5, 6}})
            );
        }

        @Test
        @DisplayName("两个任务首尾相接（前一个结束 = 后一个开始）")
        void twoBackToBack() {
            // [1,3] end=3, [3,5] start=3, 条件 end < start → 3<3 false → 不能同时选
            // maxCount=1, minSum: min(2,2)=2
            assertArrayEquals(
                    new int[]{1, 2},
                    solution.bestSchedule(new int[][]{{1, 3}, {3, 5}})
            );
        }

        @Test
        @DisplayName("所有任务完全重叠 → 只能选1个，选最短的")
        void allOverlapping() {
            // [1,10] dur=9, [2,5] dur=3, [4,6] dur=2
            // 全部重叠，maxCount=1, minSum=min(9,3,2)=2
            assertArrayEquals(
                    new int[]{1, 2},
                    solution.bestSchedule(new int[][]{{1, 10}, {2, 5}, {4, 6}})
            );
        }

        @Test
        @DisplayName("完全重叠且最短任务在最后")
        void allOverlapping_shortestLast() {
            // [1,8] dur=7, [2,9] dur=7, [3,4] dur=1
            assertArrayEquals(
                    new int[]{1, 1},
                    solution.bestSchedule(new int[][]{{1, 8}, {2, 9}, {3, 4}})
            );
        }

        @Test
        @DisplayName("两个完全相同的任务")
        void twoIdenticalTasks() {
            // [1,2], [1,2] 同时开始不能一起选，maxCount=1, minSum=1
            assertArrayEquals(
                    new int[]{1, 1},
                    solution.bestSchedule(new int[][]{{1, 2}, {1, 2}})
            );
        }
    }

    // ==================== 基本功能（贪心恰好得到最优解） ====================

    @Nested
    @DisplayName("基本功能（贪心可得最优）")
    class BasicFunctionality {

        @Test
        @DisplayName("全部不重叠且按开始时间排好序")
        void allDisjoint_sortedByStart() {
            // [1,2] dur=1, [3,4] dur=1, [5,6] dur=1
            assertArrayEquals(
                    new int[]{3, 3},
                    solution.bestSchedule(new int[][]{{1, 2}, {3, 4}, {5, 6}})
            );
        }

        @Test
        @DisplayName("全部不重叠但乱序输入")
        void allDisjoint_unsorted() {
            // [5,6] dur=1, [1,2] dur=1, [3,4] dur=1 → 排序后 [1,2],[3,4],[5,6]
            assertArrayEquals(
                    new int[]{3, 3},
                    solution.bestSchedule(new int[][]{{5, 6}, {1, 2}, {3, 4}})
            );
        }

        @Test
        @DisplayName("零时长任务")
        void zeroDurationTasks() {
            // [1,1] dur=0, [2,2] dur=0, [3,3] dur=0
            assertArrayEquals(
                    new int[]{3, 0},
                    solution.bestSchedule(new int[][]{{1, 1}, {2, 2}, {3, 3}})
            );
        }

        @Test
        @DisplayName("第一个任务很长但贪心选它仍然最优")
        void longFirstTask_stillOptimal() {
            // [1,3] dur=2, [4,5] dur=1, [6,7] dur=1
            // 选 [1,3]+[4,5]+[6,7] → count=3, sum=4，就是最优
            assertArrayEquals(
                    new int[]{3, 4},
                    solution.bestSchedule(new int[][]{{1, 3}, {4, 5}, {6, 7}})
            );
        }
    }

    // ==================== ⚠️ Bug 暴露①：按开始时间排序遗漏任务 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露①：按开始时间排序导致遗漏更多任务")
    class BugSortByStart {

        @Test
        @DisplayName("⚠️ 第一个任务跨度大，覆盖了后面两个短任务")
        void longFirstTask_blocksTwoShort() {
            // [1,10] dur=9, [2,3] dur=1, [4,5] dur=1
            // 代码（按开始排序）: [1,10]→end=10, 后面全跳过 → count=1, sum=9
            // 最优（按结束排序）: [2,3]+[4,5] → count=2, sum=2
            assertArrayEquals(
                    new int[]{2, 2},  // 正确答案
                    solution.bestSchedule(new int[][]{{1, 10}, {2, 3}, {4, 5}})
            );
        }

        @Test
        @DisplayName("⚠️ 开始早结束晚的任务挡住多个短任务")
        void earlyLongTask_blocksMany() {
            // [1,20] dur=19, [2,3] dur=1, [4,5] dur=1, [6,7] dur=1
            // 代码: 选[1,20] 后面全跳过 → count=1, sum=19
            // 最优: [2,3]+[4,5]+[6,7] → count=3, sum=3
            assertArrayEquals(
                    new int[]{3, 3},
                    solution.bestSchedule(new int[][]{{1, 20}, {2, 3}, {4, 5}, {6, 7}})
            );
        }

        @Test
        @DisplayName("⚠️ 中等跨度任务挡住后一个短任务")
        void mediumTask_blocksShort() {
            // [1,5] dur=4, [2,3] dur=1, [6,7] dur=1
            // 代码（按开始）: [1,5]→end=5, [2,3]跳过, [6,7]选 → count=2, sum=4+1=5
            // 最优（按结束）: [2,3]+[6,7] → count=2, sum=2
            // 都count=2 但 sum不同 → sum的bug暴露
            assertArrayEquals(
                    new int[]{2, 2},  // 正确答案
                    solution.bestSchedule(new int[][]{{1, 5}, {2, 3}, {6, 7}})
            );
        }

        @Test
        @DisplayName("⚠️ 输入乱序加剧按开始排序的缺陷")
        void unsortedExposesSortBug() {
            // 按输入顺序: [5,7], [1,10], [2,3], [4,5], [8,9]
            // 排序后（按开始）: [1,10],[2,3],[4,5],[5,7],[8,9]
            // 代码: [1,10]→end=10, 后面全跳过 → count=1, sum=9
            // 最优: [2,3]+[4,5]+[5,7]+[8,9]? [4,5] end=5, [5,7] start=5, 5<5? No.
            //       最优: [2,3]+[5,7]+[8,9]? [2,3] end=3, [5,7] start=5, 3<5 Yes. [8,9] 7<8 Yes. count=3 sum=1+2+1=4
            assertArrayEquals(
                    new int[]{3, 4},
                    solution.bestSchedule(new int[][]{{5, 7}, {1, 10}, {2, 3}, {8, 9}})
            );
        }
    }

    // ==================== ⚠️ Bug 暴露②：贪心优先选第一个导致总时长非最短 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露②：贪心优先选第一个可用 → 总占用时长非最短")
    class BugMinSum {

        @Test
        @DisplayName("⚠️ 跳过第一个可选任务选后面的更省时")
        void skipFirstAvailable_savesTime() {
            // [2,6] dur=4, [5,7] dur=2, [8,10] dur=2
            // 代码（按开始排序后）: [2,6]+[8,10] → count=2, sum=4+2=6
            // 最优: [5,7]+[8,10] → count=2, sum=2+2=4
            assertArrayEquals(
                    new int[]{2, 4},  // 正确答案
                    solution.bestSchedule(new int[][]{{2, 6}, {5, 7}, {8, 10}})
            );
        }

        @Test
        @DisplayName("⚠️ 选前期短任务比选后期短任务开销大")
        void earlyPickCostsMore() {
            // [1,4] dur=3, [5,8] dur=3, [6,7] dur=1
            // 代码: [1,4]+[5,8] → count=2, sum=3+3=6
            // 最优: [1,4]+[6,7] → count=2, sum=3+1=4
            assertArrayEquals(
                    new int[]{2, 4},
                    solution.bestSchedule(new int[][]{{1, 4}, {5, 8}, {6, 7}})
            );
        }

        @Test
        @DisplayName("⚠️ 多个重叠区间中选最短的才对")
        void multipleOverlapping_pickShortest() {
            // [1,6] dur=5, [2,5] dur=3, [3,4] dur=1, [7,8] dur=1
            // 代码: [1,6]+[7,8] → count=2, sum=5+1=6
            // 最优: [3,4]+[7,8] → count=2, sum=1+1=2
            assertArrayEquals(
                    new int[]{2, 2},
                    solution.bestSchedule(new int[][]{{1, 6}, {2, 5}, {3, 4}, {7, 8}})
            );
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class ComprehensiveScenarios {

        @Test
        @DisplayName("三组任务，每组内重叠，组间不重叠")
        void threeClusters() {
            // 组1: [1,6](5) [2,3](1) → 选[2,3]
            // 组2: [5,10](5) [7,8](1) → 选[7,8]
            // 组3: [10,12](2) [11,13](2) → 选[10,12]
            // 代码按开始排序: [1,6],[2,3],[5,10],[7,8],[10,12],[11,13]
            // 代码: [1,6]→[5,10]skip→[7,8] skip? 6<7? Yes→[7,8]→[10,12]→count=3, sum=5+1+2=8
            // 最优: [2,3]+[7,8]+[10,12] → count=3, sum=1+1+2=4
            assertArrayEquals(
                    new int[]{3, 4},
                    solution.bestSchedule(new int[][]{
                            {1, 6}, {2, 3},
                            {5, 10}, {7, 8},
                            {10, 12}, {11, 13}
                    })
            );
        }

        @Test
        @DisplayName("较大规模：链式重叠")
        void chainOverlap() {
            // [1,3], [2,4], [3,5], [4,6], [5,7], [6,8]
            // 严格<: 最多选 [1,3]+[4,6]? 3<4 Yes. [6,8] 6<6 No. → 2
            //          [2,4]+[5,7]? 4<5 Yes. → 2
            //          [1,3]+[5,7]? 3<5 Yes. → 2
            //          [2,4]+[6,8]? 4<6 Yes. → 2
            // 都不可能3个，maxCount=2
            // minSum: [1,3]+[4,6] dur=2+2=4, or [2,4]+[5,7] dur=2+2=4, ...
            // 代码: [1,3]→[2,4]skip→[3,5]skip→[4,6] 3<4 Yes pick → [5,7]skip→[6,8]skip
            // count=2, sum=4
            assertArrayEquals(
                    new int[]{2, 4},
                    solution.bestSchedule(new int[][]{
                            {1, 3}, {2, 4}, {3, 5}, {4, 6}, {5, 7}, {6, 8}
                    })
            );
        }

        @Test
        @DisplayName("所有任务零时长")
        void allZeroDuration() {
            // 严格<: 前一个end < 后一个start
            // [1,1],[2,2],[3,3] → all disjoint → count=3, sum=0
            // [1,1],[1,1] → same time, can only pick 1
            assertArrayEquals(
                    new int[]{3, 0},
                    solution.bestSchedule(new int[][]{{1, 1}, {2, 2}, {3, 3}})
            );
        }

        @Test
        @DisplayName("任务时间乱序且包含重叠")
        void mixedUnsorted() {
            // 输入乱序: [8,10](2), [1,3](2), [5,6](1), [2,7](5)
            // 按开始排序后: [1,3],[2,7],[5,6],[8,10]
            // 代码: [1,3]→[2,7]skip→[5,6]pick? 3<5 Yes → [8,10]pick? 6<8 Yes → count=3, sum=2+1+2=5
            // 最优: [1,3]+[5,6]+[8,10] = count=3, sum=5
            // 这也恰好是最优了
            assertArrayEquals(
                    new int[]{3, 5},
                    solution.bestSchedule(new int[][]{{8, 10}, {1, 3}, {5, 6}, {2, 7}})
            );
        }
    }
}
