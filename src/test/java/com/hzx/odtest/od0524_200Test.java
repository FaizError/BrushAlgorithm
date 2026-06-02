package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（2个）
 * 2. 边界条件（桩数≥车数、单桩单车、全部失败）
 * 3. 基本调度场景
 * 4. 等待时间逻辑
 * 5. 先到先服务（FCFS）验证
 * 6. 复杂场景
 */
@DisplayName("充电桩调度算法测试")
class od0524_200Test {

    private od0524_200 solution;

    @BeforeEach
    void setUp() {
        solution = new od0524_200();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: N=3,M=5,全[10,1,0] → 2失败")
        void example1() {
            int[][] cars = {
                    {10, 1, 0},
                    {10, 1, 0},
                    {10, 1, 0},
                    {10, 1, 0},
                    {10, 1, 0}
            };
            // 3个桩，5辆车同时到达且WT=0，只有3辆能充
            assertEquals(2, solution.scheduleCharging(3, 5, cars));
        }

        @Test
        @DisplayName("示例2: N=2,M=4 → 1失败")
        void example2() {
            int[][] cars = {
                    {1, 10, 0},
                    {2, 2, 1},
                    {3, 1, 0},
                    {4, 1, 0}
            };
            // 车1:桩1(1→11), 车2:桩2(2→4), 车3:无桩且WT=0→失败, 车4:桩2(4→5)成功
            assertEquals(1, solution.scheduleCharging(2, 4, cars));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("桩数=车数 → 永不失败")
        void equalPilesAndCars() {
            int[][] cars = {
                    {1, 10, 0},
                    {1, 10, 0},
                    {1, 10, 0}
            };
            assertEquals(0, solution.scheduleCharging(3, 3, cars));
        }

        @Test
        @DisplayName("桩数>车数 → 永不失败")
        void morePilesThanCars() {
            int[][] cars = {
                    {5, 100, 0},
                    {5, 100, 0}
            };
            assertEquals(0, solution.scheduleCharging(5, 2, cars));
        }

        @Test
        @DisplayName("单桩单车 → 0失败")
        void singlePileSingleCar() {
            assertEquals(0, solution.scheduleCharging(1, 1, new int[][]{{10, 5, 2}}));
        }

        @Test
        @DisplayName("单桩多车 错峰到达")
        void singlePile_multipleCars_staggered() {
            int[][] cars = {
                    {1, 3, 0},   // 1→4
                    {4, 2, 0},   // 4→6, WT=0正好接上
                    {6, 1, 0}    // 6→7, WT=0正好接上
            };
            // 单桩，完美衔接
            assertEquals(0, solution.scheduleCharging(1, 3, cars));
        }

        @Test
        @DisplayName("全部失败：1桩多车同时到达且WT=0")
        void allFail() {
            int[][] cars = {
                    {10, 5, 0},
                    {10, 5, 0},
                    {10, 5, 0}
            };
            // 1桩3车同时到达，只有第1辆能充
            assertEquals(2, solution.scheduleCharging(1, 3, cars));
        }
    }

    // ==================== 基本调度 ====================

    @Nested
    @DisplayName("基本调度")
    class BasicScheduling {

        @Test
        @DisplayName("全部成功 — 错峰到达无等待")
        void allSuccess_staggered() {
            int[][] cars = {
                    {1, 2, 0},
                    {3, 2, 0},
                    {5, 2, 0}
            };
            // 2桩，3车错峰到达
            assertEquals(0, solution.scheduleCharging(2, 3, cars));
        }

        @Test
        @DisplayName("满载 — 恰好填满")
        void exactlyFull() {
            int[][] cars = {
                    {1, 10, 0},
                    {1, 10, 0}
            };
            assertEquals(0, solution.scheduleCharging(2, 2, cars));
        }

        @Test
        @DisplayName("不同充电时长")
        void differentChargeTimes() {
            int[][] cars = {
                    {1, 10, 0},  // 桩1: 1→11
                    {2, 1, 0},   // 桩2: 2→3
                    {3, 2, 0},   // 桩2: 3→5 (桩1忙, 桩2空闲)
                    {4, 1, 0}    // 桩2: 5→6 (桩1忙, 桩2在5空闲)
            };
            assertEquals(0, solution.scheduleCharging(2, 4, cars));
        }
    }

    // ==================== 等待时间逻辑 ====================

    @Nested
    @DisplayName("等待时间（WT）逻辑")
    class WaitTimeLogic {

        @Test
        @DisplayName("WT>0 等待后成功")
        void waitAndSucceed() {
            int[][] cars = {
                    {1, 5, 0},   // 桩1: 1→6
                    {2, 1, 5}    // 桩1: 等到6→7 (WT=5, 可等5秒到时刻7)
            };
            // 1桩，车2可等待到7，桩1在6空闲 → 成功
            assertEquals(0, solution.scheduleCharging(1, 2, cars));
        }

        @Test
        @DisplayName("WT=0 不能等 → 失败")
        void noWait_fail() {
            int[][] cars = {
                    {1, 5, 0},   // 桩1: 1→6
                    {2, 1, 0}    // WT=0, 桩1在6才空闲 → 失败
            };
            assertEquals(1, solution.scheduleCharging(1, 2, cars));
        }

        @Test
        @DisplayName("WT不足 → 失败")
        void insufficientWait_fail() {
            int[][] cars = {
                    {1, 10, 0},  // 桩1: 1→11
                    {2, 1, 3}    // 可等到5, 但桩1到11才空闲 → 失败
            };
            assertEquals(1, solution.scheduleCharging(1, 2, cars));
        }

        @Test
        @DisplayName("WT刚好够")
        void exactWait_succeed() {
            int[][] cars = {
                    {1, 5, 0},   // 桩1: 1→6
                    {3, 1, 3}    // 可等到6, 桩1在6刚好空闲 → 成功
            };
            assertEquals(0, solution.scheduleCharging(1, 2, cars));
        }

        @Test
        @DisplayName("WT刚好差1 → 失败")
        void waitJustShort_fail() {
            int[][] cars = {
                    {1, 5, 0},   // 桩1: 1→6
                    {3, 1, 2}    // 可等到5, 桩1在6空闲 > 5 → 失败
            };
            assertEquals(1, solution.scheduleCharging(1, 2, cars));
        }
    }

    // ==================== FCFS验证 ====================

    @Nested
    @DisplayName("先到先服务（FCFS）")
    class FCFS {

        @Test
        @DisplayName("FCFS基本: 先到先得")
        void fcfs_basic() {
            int[][] cars = {
                    {1, 100, 5},  // 桩1: 1→101
                    {2, 1, 10},   // 桩2: 2→3
                    {3, 1, 10},   // 桩2: 3→4 (此时只有桩2空闲)
                    {4, 1, 0},    // WT=0, 但桩1在101才空闲, 桩2在4空闲 → 车4到达时(4)桩2刚好空闲 → 成功
            };
            // 2桩，车4在时刻4到达，桩2在4释放 → 成功
            assertEquals(0, solution.scheduleCharging(2, 4, cars));
        }

        @Test
        @DisplayName("FCFS: 排序后先到先服务")
        void fcfs_sorted() {
            int[][] cars = {
                    {5, 1, 0},   // 虽然索引靠后但到达时间靠后
                    {1, 10, 0},  // 先到（到达时间1）
                    {2, 1, 0},   // 第二到
            };
            // 1桩，车1(AT=1):1→11, 车2(AT=2):等到11→12, 车3(AT=5):等到12→13
            assertEquals(0, solution.scheduleCharging(1, 3, cars));
        }

        @Test
        @DisplayName("乱序输入 自动按到达时间排序")
        void unsortedInput() {
            int[][] cars = {
                    {3, 1, 0},
                    {1, 5, 0},
                    {2, 2, 0}
            };
            // 1桩，排序后: AT=1:1→6, AT=2:等到6→8, AT=3:等到8→9 → 全成功
            assertEquals(0, solution.scheduleCharging(1, 3, cars));
        }
    }

    // ==================== 复杂场景 ====================

    @Nested
    @DisplayName("复杂场景")
    class ComplexScenarios {

        @Test
        @DisplayName("多桩多车混合WT")
        void mixedWaitTimes() {
            // N=2
            int[][] cars = {
                    {1, 5, 0},   // 桩1: 1→6
                    {2, 3, 0},   // 桩2: 2→5
                    {3, 1, 3},   // 桩2: 3→4 (桩2在5空闲? 不对，车2占桩2到5)
                                  // 等等，车3到达时(3)，桩1忙(到6)，桩2忙(到5)
                                  // 下一个空闲是桩2在5，可等到6(WT=3, AT+WT=6)
                                  // 5 ≤ 6 → 等待后成功，桩2: 5→6
                    {5, 2, 1}    // 到达5，桩1在6空闲，可等到6 → 成功
            };
            assertEquals(0, solution.scheduleCharging(2, 4, cars));
        }

        @Test
        @DisplayName("拼车高峰")
        void rushHour() {
            // N=2, 8辆车同时到达但WT不同
            int[][] cars = new int[8][3];
            for (int i = 0; i < 8; i++) {
                cars[i] = new int[]{10, 5, i < 4 ? 0 : 10};
            }
            // 前4辆WT=0，后4辆WT=10
            // 2桩：前2辆立刻充(10→15)，后2辆WT=0无桩可用→失败
            // 后4辆WT=10，可等到15（最早的桩释放时间），充到20
            assertEquals(2, solution.scheduleCharging(2, 8, cars));
        }

        @Test
        @DisplayName("长充电时间+短WT")
        void longCharge_shortWait() {
            int[][] cars = {
                    {1, 100, 0},   // 桩1: 1→101
                    {1, 100, 0},   // 桩2: 1→101
                    {2, 1, 5},     // 桩都在忙到101, 可等到7 → 失败
                    {3, 1, 0},     // 失败
            };
            assertEquals(2, solution.scheduleCharging(2, 4, cars));
        }
    }

    // ==================== 较大规模 ====================

    @Nested
    @DisplayName("较大规模")
    class LargerScale {

        @Test
        @DisplayName("顺序到达全成功")
        void sequentialAllSuccess() {
            int m = 100;
            int[][] cars = new int[m][3];
            for (int i = 0; i < m; i++) {
                cars[i] = new int[]{i * 2, 1, 0};
            }
            // 10桩，100辆车每2秒到一辆、充1秒 → 全成功
            assertEquals(0, solution.scheduleCharging(10, m, cars));
        }

        @Test
        @DisplayName("大规模同时到达")
        void massSimultaneousArrival() {
            int m = 50;
            int[][] cars = new int[m][3];
            for (int i = 0; i < m; i++) {
                cars[i] = new int[]{10, 3, 0};
            }
            // 10桩，50辆车同时到达且WT=0 → 40失败
            assertEquals(40, solution.scheduleCharging(10, m, cars));
        }
    }
}
