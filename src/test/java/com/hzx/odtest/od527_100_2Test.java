package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（4个）
 * 2. 边界条件（空数组、单元素）
 * 3. 纯基础类型链
 * 4. 交替模式（0,1,0,1,...）
 * 5. 高级类型模式（0,0,2,...）
 * 6. 违规中断场景
 * 7. 复杂组合
 */
@DisplayName("最长有效Skill子链算法测试")
class od527_100_2Test {

    private od527_100_2 solution;

    @BeforeEach
    void setUp() {
        solution = new od527_100_2();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: 全为基础类型 [0,0,0] → 3")
        void example1_allBasics() {
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 0, 0}));
        }

        @Test
        @DisplayName("示例2: 基础-扩展交替 [0,1,0,1] → 4")
        void example2_alternating() {
            assertEquals(4, solution.longestValidSubchain(new int[]{0, 1, 0, 1}));
        }

        @Test
        @DisplayName("示例3: 首元素违规 [2,0,0] → 2")
        void example3_firstElementViolation() {
            assertEquals(2, solution.longestValidSubchain(new int[]{2, 0, 0}));
        }

        @Test
        @DisplayName("示例4: 混合类型 [0,1,0,0,2] → 5")
        void example4_mixed() {
            assertEquals(5, solution.longestValidSubchain(new int[]{0, 1, 0, 0, 2}));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("空数组 → 0")
        void emptyArray() {
            assertEquals(0, solution.longestValidSubchain(new int[]{}));
        }

        @Test
        @DisplayName("单个元素: 基础类型 [0] → 1")
        void singleBasic() {
            assertEquals(1, solution.longestValidSubchain(new int[]{0}));
        }

        @Test
        @DisplayName("单个元素: 扩展类型 [1] → 0（不能以扩展类型开头）")
        void singleExtended_invalid() {
            assertEquals(0, solution.longestValidSubchain(new int[]{1}));
        }

        @Test
        @DisplayName("单个元素: 高级类型 [2] → 0（不能以高级类型开头）")
        void singleAdvanced_invalid() {
            assertEquals(0, solution.longestValidSubchain(new int[]{2}));
        }

        @Test
        @DisplayName("全部是扩展类型 [1,1,1] → 0")
        void allExtended() {
            assertEquals(0, solution.longestValidSubchain(new int[]{1, 1, 1}));
        }

        @Test
        @DisplayName("全部是高级类型 [2,2,2] → 0")
        void allAdvanced() {
            assertEquals(0, solution.longestValidSubchain(new int[]{2, 2, 2}));
        }

        @Test
        @DisplayName("只有一个0和多个1 [1,1,0,1,1] → 1")
        void onlyOneBasic_surroundedByExtended() {
            assertEquals(1, solution.longestValidSubchain(new int[]{1, 1, 0, 1, 1}));
        }
    }

    // ==================== 纯基础类型 ====================

    @Nested
    @DisplayName("纯基础类型链")
    class AllBasics {

        @Test
        @DisplayName("两个基础类型 [0,0] → 2")
        void twoBasics() {
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 0}));
        }

        @Test
        @DisplayName("长基础类型链 [0,0,0,0,0] → 5")
        void longBasicChain() {
            assertEquals(5, solution.longestValidSubchain(new int[]{0, 0, 0, 0, 0}));
        }

        @Test
        @DisplayName("基础类型被1隔开但有效 [0,0,1,0,0] → 4")
        void basicsWithExtended_valid() {
            // 链: [0,0,1,0] 或 [0,1,0,0]，长度4
            assertEquals(4, solution.longestValidSubchain(new int[]{0, 0, 1, 0, 0}));
        }
    }

    // ==================== 扩展类型（type=1）模式 ====================

    @Nested
    @DisplayName("扩展类型（type=1）模式")
    class ExtendedTypePatterns {

        @Test
        @DisplayName("标准交替: [0,1,0,1,0] → 5")
        void standardAlternating() {
            assertEquals(5, solution.longestValidSubchain(new int[]{0, 1, 0, 1, 0}));
        }

        @Test
        @DisplayName("长交替: [0,1,0,1,0,1,0] → 7")
        void longAlternating() {
            assertEquals(7, solution.longestValidSubchain(new int[]{0, 1, 0, 1, 0, 1, 0}));
        }

        @Test
        @DisplayName("扩展类型前驱不是基础: [0,1,1,0] → 2")
        void extendedWithoutBasicPredecessor() {
            // idx2的1前驱是1（不是0）→ 违规 → 最长链 [0,1] 或末尾 [0]
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 1, 1, 0}));
        }

        @Test
        @DisplayName("扩展类型后跟扩展类型: [0,1,1] → 2")
        void consecutiveExtendedTypes() {
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 1, 1}));
        }

        @Test
        @DisplayName("扩展类型后跟高级类型: [0,1,2] → 2")
        void extendedThenAdvanced_invalid() {
            // type=2需要两个前驱都是0，idx1=1不是0
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 1, 2}));
        }

        @Test
        @DisplayName("从0,1重新开始: [0,1,2,0,1,0] → 3")
        void restartAfterViolation() {
            // idx2=2违规，从idx3重新开始: [0,1,0] 长度3
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 1, 2, 0, 1, 0}));
        }
    }

    // ==================== 高级类型（type=2）模式 ====================

    @Nested
    @DisplayName("高级类型（type=2）模式")
    class AdvancedTypePatterns {

        @Test
        @DisplayName("标准高级类型: [0,0,2] → 3")
        void standardAdvanced() {
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 0, 2}));
        }

        @Test
        @DisplayName("高级类型后接基础: [0,0,2,0] → 4")
        void advancedThenBasic() {
            assertEquals(4, solution.longestValidSubchain(new int[]{0, 0, 2, 0}));
        }

        @Test
        @DisplayName("高级类型需要两个前驱都是0: [0,2,0] → 1")
        void advancedWithOnlyOnePredecessor() {
            // idx1=2 只有 idx0=0 一个前驱（需要两个）
            assertEquals(1, solution.longestValidSubchain(new int[]{0, 2, 0}));
        }

        @Test
        @DisplayName("高级类型前驱不是两个0: [0,1,0,2] → 3")
        void advancedWithInvalidPredecessors() {
            // idx3=2: 前驱 idx2=0, idx1=1 — idx1不是0 → 违规
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 1, 0, 2}));
        }

        @Test
        @DisplayName("两个连续高级类型: [0,0,2,2] → 3")
        void consecutiveAdvancedTypes() {
            // idx3=2: 前驱 idx2=2 (不是0) → 违规
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 0, 2, 2}));
        }

        @Test
        @DisplayName("高级类型后接扩展类型: [0,0,2,1] → 3")
        void advancedThenExtended_invalid() {
            // idx3=1: 前驱 idx2=2 (不是0) → 违规
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 0, 2, 1}));
        }

        @Test
        @DisplayName("重复高级类型模式: [0,0,2,0,0,2] → 6")
        void repeatedAdvancedPattern() {
            assertEquals(6, solution.longestValidSubchain(new int[]{0, 0, 2, 0, 0, 2}));
        }
    }

    // ==================== 违规中断与重启 ====================

    @Nested
    @DisplayName("违规中断与重启")
    class ViolationAndRestart {

        @Test
        @DisplayName("首元素为扩展类型，从后续基础类型开始: [1,0,0,0] → 3")
        void firstIsExtended_startFromNextBasic() {
            assertEquals(3, solution.longestValidSubchain(new int[]{1, 0, 0, 0}));
        }

        @Test
        @DisplayName("首元素为高级类型，从后续基础类型开始: [2,0,1,0] → 3")
        void firstIsAdvanced_startFromNext() {
            assertEquals(3, solution.longestValidSubchain(new int[]{2, 0, 1, 0}));
        }

        @Test
        @DisplayName("中间断链，选择更长的一段: [0,1,0, 1, 0,1,0,1,0] → 5")
        void pickLongerSegment() {
            // idx3=1: 前驱 idx2=0 ✓, 但 idx2=0 的 dp 是3 → dp[3]=4
            // 等等，[0,1,0] 然后 [1,0,1,0,1,0]？
            // idx3=1, type[2]=0 → dp[3]=dp[2]+1=4
            // idx4=0 → dp[4]=5
            // idx5=1, type[4]=0 → dp[5]=6
            // idx6=0 → dp[6]=7
            // idx7=1, type[6]=0 → dp[7]=8
            // idx8=0 → dp[8]=9
            // max=9
            assertEquals(9, solution.longestValidSubchain(
                    new int[]{0, 1, 0, 1, 0, 1, 0, 1, 0}));
        }

        @Test
        @DisplayName("断开后中间有孤立的基础类型: [0,1,2,0,0,2] → 3")
        void breakWithIsolatedBasic() {
            // idx2=2违规，idx3=0重启→1，idx4=0→2，idx5=2→3
            // 前段 [0,1] 长度2，后段 [0,0,2] 长度3
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 1, 2, 0, 0, 2}));
        }
    }

    // ==================== 复杂组合 ====================

    @Nested
    @DisplayName("复杂组合")
    class ComplexCombinations {

        @Test
        @DisplayName("基础+扩展+基础+基础+高级: [0,1,0,0,2] → 5")
        void pattern_basicExtBasicBasicAdvanced() {
            assertEquals(5, solution.longestValidSubchain(new int[]{0, 1, 0, 0, 2}));
        }

        @Test
        @DisplayName("高级类型嵌入交替链: [0,0,2,0,1,0] → 6")
        void advancedEmbeddedInAlternating() {
            assertEquals(6, solution.longestValidSubchain(new int[]{0, 0, 2, 0, 1, 0}));
        }

        @Test
        @DisplayName("交替中包含多个高级类型: [0,0,2,0,0,2,0,1,0] → 9")
        void multipleAdvancedInChain() {
            assertEquals(9, solution.longestValidSubchain(
                    new int[]{0, 0, 2, 0, 0, 2, 0, 1, 0}));
        }

        @Test
        @DisplayName("全部有效的大混合: [0,1,0,0,2,0,1,0,0,2] → 10")
        void largeMixed_allValid() {
            assertEquals(10, solution.longestValidSubchain(
                    new int[]{0, 1, 0, 0, 2, 0, 1, 0, 0, 2}));
        }

        @Test
        @DisplayName("基础类型密集区+扩展: [0,0,0,1,0,0] → 6")
        void denseBasicsWithExtended() {
            assertEquals(6, solution.longestValidSubchain(new int[]{0, 0, 0, 1, 0, 0}));
        }
    }

    // ==================== 规则3专项：链式依赖 ====================

    @Nested
    @DisplayName("规则3：相邻基础类型间最多一个非基础")
    class Rule3_ChainDependency {

        @Test
        @DisplayName("两个基础类型相邻: [0,0] → 2（0个非基础，合法）")
        void adjacentBasics_valid() {
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 0}));
        }

        @Test
        @DisplayName("基础间有一个扩展: [0,1,0] → 3（1个非基础，合法）")
        void oneExtendedBetweenBasics_valid() {
            assertEquals(3, solution.longestValidSubchain(new int[]{0, 1, 0}));
        }

        @Test
        @DisplayName("基础间有一个高级: [0,0,2,0] → 4（高级前两个基础，高级后一个基础）")
        void oneAdvancedBetweenBasics_valid() {
            assertEquals(4, solution.longestValidSubchain(new int[]{0, 0, 2, 0}));
        }

        @Test
        @DisplayName("两个连续扩展在基础之间: [0,1,1,0] → 2（违反规则2和规则3）")
        void twoExtendedBetweenBasics_invalid() {
            // idx2=1前驱不是0 → 违规
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 1, 1, 0}));
        }

        @Test
        @DisplayName("扩展+高级在基础之间: [0,1,2,0] → 2")
        void extendedAndAdvancedBetweenBasics_invalid() {
            assertEquals(2, solution.longestValidSubchain(new int[]{0, 1, 2, 0}));
        }
    }
}
