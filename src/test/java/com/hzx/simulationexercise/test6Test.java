package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例
 * 2. 边界条件（n=1、无依赖、完全无依赖、依赖链等）
 * 3. 基本功能：DAG 无环（链状、树状、菱形依赖）
 * 4. 基本功能：有环（自环、二元环、复杂环）
 * 5. 字典序验证（分支选择、多入度节点）
 * 6. 综合场景（多连通分量、大图压力测试）
 */
@DisplayName("微服务启动顺序算法测试")
class test6Test {

    private test6 solution;

    @BeforeEach
    void setUp() {
        solution = new test6();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: 正常DAG, 返回字典序最小拓扑序")
        void example1() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
            // 0无依赖最先 → 1和2依赖0 → 字典序1先于2 → 3最后
            assertArrayEquals(new int[]{0, 1, 2, 3}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("示例2: 二元环, 返回空数组")
        void example2_cycle() {
            int n = 2;
            int[][] dependencies = {{1, 0}, {0, 1}};
            // 0依赖1，1依赖0 → 死锁
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("单个服务无依赖 → [0]")
        void singleService_noDependency() {
            int n = 1;
            int[][] dependencies = {};
            assertArrayEquals(new int[]{0}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("所有服务都无依赖 → 字典序")
        void allIndependent() {
            int n = 5;
            int[][] dependencies = {};
            // 无依赖，按字典序 0,1,2,3,4
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("单条依赖链 0→1→2→3")
        void singleDependencyChain() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {2, 1}, {3, 2}};
            // 0最先 → 1 → 2 → 3
            assertArrayEquals(new int[]{0, 1, 2, 3}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("反向依赖链 3→2→1→0")
        void reverseDependencyChain() {
            int n = 4;
            int[][] dependencies = {{2, 3}, {1, 2}, {0, 1}};
            // 3最先 → 2 → 1 → 0
            assertArrayEquals(new int[]{3, 2, 1, 0}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("空依赖数组, n=3")
        void emptyDependencies() {
            int n = 3;
            int[][] dependencies = {};
            assertArrayEquals(new int[]{0, 1, 2}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("依赖数组为空二维数组")
        void empty2DDependencies() {
            int n = 3;
            int[][] dependencies = new int[0][0];
            assertArrayEquals(new int[]{0, 1, 2}, solution.serviceOrder(n, dependencies));
        }
    }

    // ==================== 基本功能：DAG 无环 ====================

    @Nested
    @DisplayName("基本功能：DAG 无环")
    class DAGNoCycle {

        @Test
        @DisplayName("树状依赖：0是根，1和2依赖0，3和4依赖1")
        void treeDependency() {
            int n = 5;
            int[][] dependencies = {{1, 0}, {2, 0}, {3, 1}, {4, 1}};
            // 0最先 → 1和2(字典序1先) → 3和4
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("菱形依赖：0→1→3, 0→2→3")
        void diamondDependency() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {2, 0}, {3, 1}, {3, 2}};
            // 0最先 → 1和2(字典序1先) → 3
            assertArrayEquals(new int[]{0, 1, 2, 3}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("多个入口节点（多个 indegree=0）")
        void multipleEntryNodes() {
            int n = 5;
            int[][] dependencies = {{2, 0}, {3, 1}, {4, 2}, {4, 3}};
            // 0和1 indegree=0 → 字典序0先→1 → 2和3(依赖各自的) → 字典序2先→3 → 4
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("节点编号不连续但逻辑清晰")
        void sparseNodeIds() {
            int n = 3;
            int[][] dependencies = {{2, 0}};
            // 0无依赖 → 1无依赖 → 字典序0→1→2
            assertArrayEquals(new int[]{0, 1, 2}, solution.serviceOrder(n, dependencies));
        }
    }

    // ==================== 基本功能：有环 ====================

    @Nested
    @DisplayName("基本功能：有环")
    class WithCycle {

        @Test
        @DisplayName("三元环 0→1→2→0")
        void threeNodeCycle() {
            int n = 3;
            int[][] dependencies = {{1, 0}, {2, 1}, {0, 2}};
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("自依赖（自环）")
        void selfDependency() {
            int n = 3;
            int[][] dependencies = {{0, 0}, {1, 0}, {2, 1}};
            // 0依赖自己 → 无法启动0 → 环
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("四元环")
        void fourNodeCycle() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {2, 1}, {3, 2}, {0, 3}};
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("环+孤立节点 → 依然返回空")
        void cycleWithIsolatedNodes() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {0, 1}};
            // 0和1形成环，2和3孤立
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("长链中的自环")
        void selfLoopInChain() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {2, 2}, {3, 2}};
            // 2依赖自己 → 环
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("部分服务不存在依赖（多连通分量带环）")
        void partialCycle() {
            int n = 5;
            int[][] dependencies = {{0, 1}, {1, 2}, {2, 0}, {4, 3}};
            // 0→1→2→0 是环
            assertArrayEquals(new int[0], solution.serviceOrder(n, dependencies));
        }
    }

    // ==================== 字典序验证 ====================

    @Nested
    @DisplayName("字典序验证")
    class LexicographicOrder {

        @Test
        @DisplayName("分支处字典序较小的优先")
        void branchLexicographic() {
            int n = 4;
            int[][] dependencies = {{1, 0}, {3, 0}};
            // 0最先 → 1和3都只依赖0 → 字典序1先于3 → 2孤立
            assertArrayEquals(new int[]{0, 1, 2, 3}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("多入度释放时按字典序选择")
        void multiIndegreeRelease() {
            int n = 5;
            int[][] dependencies = {{3, 0}, {4, 0}, {3, 1}, {4, 1}, {3, 2}, {4, 2}};
            // 0,1,2 indegree=0 → 字典序0→1→2
            // 然后3和4 indegree变为0 → 字典序3→4
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("优先级队列确保不是简单BFS顺序")
        void priorityQueueNotBFS() {
            int n = 6;
            int[][] dependencies = {{2, 0}, {3, 1}, {4, 0}, {5, 1}};
            // 0和1 indegree=0 → 0先于1(字典序)
            // → 2和4 indegree变为0 → 字典序2先于4
            // → 3和5 indegree变为0 → 字典序3先于5
            assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("编号大的节点无依赖也应排在小的后面")
        void largerNodeIndependent() {
            int n = 3;
            int[][] dependencies = {{1, 2}};
            // 0和2 indegree=0 → 字典序0先于2 → 1在2后
            assertArrayEquals(new int[]{0, 2, 1}, solution.serviceOrder(n, dependencies));
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class Comprehensive {

        @Test
        @DisplayName("两个独立的连通分量")
        void twoDisconnectedComponents() {
            int n = 6;
            int[][] dependencies = {{1, 0}, {4, 3}, {5, 3}};
            // 分量1: 0→1
            // 分量2: 3→4, 3→5
            // indegree=0: {0,2,3} → poll 0 → 1入队, PQ={1,2,3}
            // → poll 1 → PQ={2,3} → poll 2 → poll 3 → 4,5入队
            // → poll 4 → poll 5
            assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("复杂DAG多层依赖")
        void complexMultiLayerDAG() {
            int n = 7;
            int[][] dependencies = {
                {1, 0}, {2, 0}, {3, 1}, {3, 2},
                {4, 1}, {5, 3}, {6, 3}, {6, 4}
            };
            // indegree=0: 0 → 1和2(字典序1先)→ 3和4(字典序3先)→ 5和6(字典序5先)
            assertArrayEquals(new int[]{0, 1, 2, 3, 4, 5, 6}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("所有节点排成倒V形依赖")
        void invertedVShape() {
            int n = 5;
            int[][] dependencies = {{2, 0}, {2, 1}, {3, 2}, {4, 2}};
            // indegree=0: 0,1 → 字典序0→1 → 2 → 3,4 → 字典序3→4
            assertArrayEquals(new int[]{0, 1, 2, 3, 4}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("密集依赖：每个后继节点有多个前驱")
        void denseDependencies() {
            int n = 4;
            int[][] dependencies = {{3, 0}, {3, 1}, {3, 2}};
            // indegree=0: 0,1,2 → 字典序0→1→2 → 3
            assertArrayEquals(new int[]{0, 1, 2, 3}, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("最大N=1000的链状依赖（线性拓扑序）")
        void largeChainGraph() {
            int n = 1000;
            int[][] dependencies = new int[n - 1][2];
            for (int i = 0; i < n - 1; i++) {
                dependencies[i] = new int[]{i + 1, i}; // i+1 依赖 i
            }
            // 必须按顺序 0→1→2→...→999
            int[] expected = new int[n];
            for (int i = 0; i < n; i++) {
                expected[i] = i;
            }
            assertArrayEquals(expected, solution.serviceOrder(n, dependencies));
        }

        @Test
        @DisplayName("较大规模DAG：多源多汇")
        void largeGraph_multiSource() {
            int n = 500;
            // 每个偶数节点依赖前一个偶数节点的下一个奇数节点
            // 构造方式: 2依赖1, 4依赖3, 6依赖5...
            int edgeCount = n / 2;
            int[][] dependencies = new int[edgeCount][2];
            for (int i = 0; i < edgeCount; i++) {
                int even = 2 * i + 2;       // 2,4,6,...
                int odd = 2 * i + 1;         // 1,3,5,...
                if (even < n) {
                    dependencies[i] = new int[]{even, odd};
                } else {
                    dependencies[i] = new int[]{0, 1}; // dummy
                }
            }
            int[] result = solution.serviceOrder(n, dependencies);
            // 只是检查无环即可
            assertEquals(n, result.length);
            // 验证依赖关系
            for (int[] dep : dependencies) {
                int a = dep[0], b = dep[1];
                int posA = -1, posB = -1;
                for (int i = 0; i < result.length; i++) {
                    if (result[i] == a) posA = i;
                    if (result[i] == b) posB = i;
                }
                // b 必须在 a 之前
                if (posA != -1 && posB != -1) {
                    assert posB < posA : "服务" + b + "应在" + a + "之前启动";
                }
            }
        }
    }
}
