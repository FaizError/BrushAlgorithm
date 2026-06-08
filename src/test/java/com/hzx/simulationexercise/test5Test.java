package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例
 * 2. 边界条件（起点终点相邻、单行单列、无路径等）
 * 3. 基本功能（无增强器、有增强器、多增强器）
 * 4. 增强器策略（绕路取增强器、增强器死路、buff不叠加）
 * 5. 综合场景（多路径、大网格模拟）
 */
@DisplayName("机房网线最低铺设成本算法测试")
class test5Test {

    private test5 solution;

    @BeforeEach
    void setUp() {
        solution = new test5();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例: 经过增强器, 最低成本=2")
        void example() {
            int[][] grid = {
                {2, 0, 0, 1},
                {0, 1, 4, 0},
                {0, 0, 0, 3}
            };
            // 路径: (0,0)→(0,1)[1]→(0,2)[1]→(1,2)[增强器,buff=2]
            // →(1,3)[buff=1,0]→(2,3)[buff=0,0] 总成本=2
            assertEquals(2, solution.minWireCost(grid));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("起点和终点相邻 → 0")
        void startAdjacentToEnd() {
            int[][] grid = {{2, 3}};
            // 一步到达终点，终点不计成本
            assertEquals(0, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("起点和终点之间有一个空位 → 1")
        void singleEmptyBetween() {
            int[][] grid = {{2, 0, 3}};
            // (0,0)→(0,1)[空位,cost=1]→(0,2)[终点,cost不变]
            assertEquals(1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("单行网格，无增强器")
        void singleRow_noBooster() {
            int[][] grid = {{2, 0, 0, 0, 3}};
            // 经过3个空位，每个成本1
            assertEquals(3, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("单列网格，无增强器")
        void singleColumn_noBooster() {
            int[][] grid = {
                {2},
                {0},
                {0},
                {3}
            };
            assertEquals(2, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("起点被障碍物包围 → -1")
        void startSurroundedByObstacles() {
            int[][] grid = {
                {1, 1, 1},
                {1, 2, 1},
                {1, 1, 3}
            };
            assertEquals(-1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("障碍物阻断唯一路径 → -1")
        void blockedPath() {
            int[][] grid = {{2, 1, 3}};
            assertEquals(-1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("起点不在(0,0)")
        void startNotAtOrigin() {
            int[][] grid = {
                {1, 0, 0},
                {2, 0, 3}
            };
            // (1,0)→(1,1)[1]→(1,2)[end,1] 或 (1,0)→(0,0)[obstacle]→...
            // 实际上 (1,0)→(1,1)[1]→(1,2)[end] = 1
            assertEquals(1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("1×1 网格 (起点即终点理论上不可能, 但题目保证各一个)")
        void minimalGrid() {
            // 根据题目约束，起点和终点各一个，1×1 不可能同时存在
            // 这里仅测试解析不报错
            int[][] grid = {{2}};
            assertEquals(-1, solution.minWireCost(grid));
        }
    }

    // ==================== 基本功能：无增强器 ====================

    @Nested
    @DisplayName("基本功能：无增强器")
    class NoBooster {

        @Test
        @DisplayName("直线路径，曼哈顿距离")
        void straightPath() {
            int[][] grid = {
                {2, 0, 0, 0},
                {0, 0, 0, 3}
            };
            // 多条等价路径，每步经过空位成本+1
            // 从(0,0)到(1,3)，需要经过3个空位（最少步数=4包含终点，减1=3个空位）
            assertEquals(3, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("必须绕行障碍物")
        void detourAroundObstacle() {
            int[][] grid = {
                {2, 0, 1, 3},
                {0, 0, 0, 0}
            };
            // 直接路径被(0,2)挡住
            // 绕行: (0,0)→(1,0)[1]→(1,1)[2]→(1,2)[3]→(1,3)[4]→(0,3)[end,4]
            assertEquals(4, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("多条等价路径")
        void multiplePaths() {
            int[][] grid = {
                {2, 0, 0},
                {0, 0, 3}
            };
            // 任意路径都经过2个空位
            assertEquals(2, solution.minWireCost(grid));
        }
    }

    // ==================== 基本功能：有增强器 ====================

    @Nested
    @DisplayName("基本功能：有增强器")
    class WithBooster {

        @Test
        @DisplayName("单个增强器节省成本")
        void singleBoosterSavesCost() {
            int[][] grid = {{2, 0, 4, 0, 0, 3}};
            // 无增强器: 经过4个空位=4
            // 有增强器: (0,1)成本1, (0,2)增强器成本0→buff=2, (0,3)buff覆盖成本0, (0,4)buff覆盖成本0
            // 总成本=1
            assertEquals(1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("增强器在起点旁边，立即生效")
        void boosterRightNextToStart() {
            int[][] grid = {{2, 4, 0, 0, 3}};
            // (0,0)→(0,1)[增强器,cost=0,buff=2]→(0,2)[buff=1,cost=0]→(0,3)[buff=0,cost=0]→(0,4)[end]
            // 总成本=0
            assertEquals(0, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("增强器在终点旁边，不影响终点计入")
        void boosterNextToEnd() {
            int[][] grid = {{2, 0, 4, 3}};
            // (0,0)→(0,1)[空位,cost=1]→(0,2)[增强器,cost=1,buff=2]→(0,3)[end,cost=1]
            assertEquals(1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("两个增强器，buff不叠加（取最新）")
        void twoBoosters_buffDoesNotStack() {
            int[][] grid = {{2, 0, 4, 0, 4, 0, 3}};
            // (0,0)→(0,1)[1]→(0,2)[增强器,cost=1,buff=2]→(0,3)[buff=1,cost=1]
            // →(0,4)[增强器,cost=1,buff=2]→(0,5)[buff=1,cost=1]→(0,6)[end,cost=1]
            assertEquals(1, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("连续两个增强器相邻")
        void twoAdjacentBoosters() {
            int[][] grid = {{2, 4, 4, 0, 3}};
            // (0,0)→(0,1)[增强器,cost=0,buff=2]→(0,2)[增强器,cost=0,buff=2]
            // →(0,3)[buff=1,cost=0]→(0,4)[end,cost=0]
            assertEquals(0, solution.minWireCost(grid));
        }
    }

    // ==================== 增强器策略 ====================

    @Nested
    @DisplayName("增强器策略")
    class BoosterStrategy {

        @Test
        @DisplayName("绕路取增强器更优")
        void detourForBooster_isWorthwhile() {
            int[][] grid = {
                {2, 0, 0, 0, 0, 3},
                {0, 0, 4, 0, 0, 0}
            };
            // 直行(全走上排): (0,0)→(0,1)[1]→(0,2)[2]→(0,3)[3]→(0,4)[4]→(0,5)[end] = 4
            // 绕行取增强器: (0,0)→(1,0)[1]→(1,1)[2]→(1,2)[增强器,cost=2,buff=2]
            // →(1,3)[buff=1,cost=2]→(1,4)[buff=0,cost=2]→(1,5)[3]→(0,5)[end,3]
            assertEquals(3, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("绕路取增强器反而更贵")
        void detourForBooster_notWorthwhile() {
            int[][] grid = {
                {2, 0, 0, 0, 3},
                {0, 0, 0, 0, 0},
                {0, 0, 4, 0, 0}
            };
            // 直行: (0,0)→(0,1)[1]→(0,2)[2]→(0,3)[3]→(0,4)[end] = 3
            // 绕行取增强器(多走3步, but buff saves 2): 净吃亏
            // (0,0)→(1,0)[1]→(2,0)[2]→(2,1)[3]→(2,2)[增强器,3,buff=2]
            // →(1,2)[buff=1,3]→(0,2)[buff=0,3]→(0,3)[4]→(0,4)[end,4]
            // 增强器只抵消2步、绕路多花3步，总成本4 > 直行3
            assertEquals(3, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("增强器在死路，取完仍需返回")
        void boosterInDeadEnd() {
            int[][] grid = {
                {2, 0, 0, 3},
                {0, 0, 4, 1}
            };
            // 直行上排: (0,0)→(0,1)[1]→(0,2)[2]→(0,3)[end,2] = 2
            // 走增强器: (0,0)→(1,0)[1]→(1,1)[2]→(1,2)[增强器,2,buff=2]→(0,2)[buff=1,2]→(0,3)[end,2]
            // 也是2（增强器并未节省，因为buff覆盖的空位路径上只有(0,2)→(0,3)=end不计成本）
            assertEquals(2, solution.minWireCost(grid));
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class Comprehensive {

        @Test
        @DisplayName("较大网格，多个增强器和障碍物")
        void largerGrid_multiBoosters() {
            int[][] grid = {
                {2, 0, 0, 1, 0, 3},
                {0, 1, 4, 0, 0, 0},
                {0, 0, 0, 1, 4, 0}
            };
            // 最优路径: (0,0)→(0,1)[1]→(0,2)[2]→(1,2)[增强器,cost→2,buff=2]
            // →(1,3)[buff=1,cost→2]→(1,4)[buff=0,cost→2]
            // →(2,4)[增强器,cost→2,buff=2]→(2,5)[buff=1,cost→2]
            // →(1,5)[buff=0,cost→2]→(0,5)[end,cost=2] 总成本=2
            assertEquals(2, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("只有通过增强器buff才能零成本到达")
        void onlyPossibleWithBuff() {
            int[][] grid = {{2, 4, 0, 3}};
            // (0,0)→(0,1)[增强器,cost=0,buff=2]→(0,2)[buff=1,cost=0]→(0,3)[end,cost=0]
            assertEquals(0, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("所有空位都必须经过，无增强器")
        void allEmptiesRequired_noBooster() {
            int[][] grid = {
                {2, 0, 0},
                {1, 1, 0},
                {1, 1, 3}
            };
            // 唯一路径: (0,0)→(0,1)[1]→(0,2)[2]→(1,2)[3]→(2,2)[end,3]
            assertEquals(3, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("增强器被障碍物包围不可达")
        void boosterUnreachable() {
            int[][] grid = {
                {2, 0, 0, 3},
                {1, 1, 4, 1}
            };
            // 直行: (0,0)→(0,1)[1]→(0,2)[2]→(0,3)[end,2] = 2
            // 增强器(1,2)上下左右均是障碍物，无法到达
            assertEquals(2, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("蛇形路径经过多个增强器")
        void snakePath_multiBoosters() {
            int[][] grid = {
                {2, 0, 1, 0, 3},
                {1, 0, 1, 0, 1},
                {0, 0, 4, 0, 4}
            };
            // 最优路径: (0,0)→(0,1)[1]→(1,1)[2]→(2,1)[3]
            // →(2,2)[增强器,cost→3,buff=2]→(2,3)[buff=1,cost→3]
            // →(1,3)[buff=0,cost→3]→(0,3)[4]→(0,4)[end,4] 总成本=4
            assertEquals(4, solution.minWireCost(grid));
        }

        @Test
        @DisplayName("最大坐标范围压力测试")
        void stressTest_largeGrid() {
            int M = 100, N = 100;
            int[][] grid = new int[M][N];
            // 全部初始化为0
            for (int i = 0; i < M; i++) {
                for (int j = 0; j < N; j++) {
                    grid[i][j] = 0;
                }
            }
            grid[0][0] = 2;           // 起点
            grid[M - 1][N - 1] = 3;   // 终点
            grid[50][50] = 4;         // 中间一个增强器
            // 至少应该能到达，且返回合理成本
            int result = solution.minWireCost(grid);
            // 曼哈顿路径经过198个单元格:
            // 99个空位(到增强器前) + 增强器(成本0) + 2个buff空位(成本0)
            // + 95个空位(buff后) + 终点(成本0) = 99+95 = 194
            assertEquals(194, solution.minWireCost(grid));
        }
    }
}
