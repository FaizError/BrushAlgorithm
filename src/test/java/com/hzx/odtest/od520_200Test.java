package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（5个）
 * 2. 输入校验（非法字符、重复节点、长度不等、元素不同）
 * 3. 树重建与遍历
 * 4. 删除规则（叶/内部/根）
 * 5. ⚠️ Bug暴露用例
 */
@DisplayName("二叉树遍历与节点删除算法测试")
class od520_200Test {

    private od520_200 solution;

    @BeforeEach
    void setUp() {
        solution = new od520_200();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: 非法字符 \"23a\",\"ABC\",A → \"\"")
        void example1_illegalChars() {
            assertEquals("", solution.postorderStr("23a", "ABC", "A"));
        }

        @Test
        @DisplayName("示例2: \"ADE\",\"DAE\",E → \"DA\"")
        void example2_deleteLeaf() {
            // 树: A(root), D(left), E(right). 前序:ADE, 中序:DAE
            // 删除E(叶子)，后序:DA
            assertEquals("DA", solution.postorderStr("ADE", "DAE", "E"));
        }

        @Test
        @DisplayName("示例3: 重复节点 \"AABCD\",\"BAACD\",A → \"\"")
        void example3_duplicateNode() {
            assertEquals("", solution.postorderStr("AABCD", "BAACD", "A"));
        }

        @Test
        @DisplayName("示例4: 根节点 \"ABC\",\"BAC\",A → \"BCA\"")
        void example4_rootNode() {
            // A是根节点，不删 → 后序BCA
            assertEquals("BCA", solution.postorderStr("ABC", "BAC", "A"));
        }

        @Test
        @DisplayName("示例5: 元素不同 \"ABCDE\",\"BAHDE\",E → \"\"")
        void example5_differentElements() {
            assertEquals("", solution.postorderStr("ABCDE", "BAHDE", "E"));
        }
    }

    // ==================== 输入校验 ====================

    @Nested
    @DisplayName("输入校验")
    class InputValidation {

        @Test
        @DisplayName("前序含非大写字母 → \"\"")
        void preorderHasInvalidChar() {
            assertEquals("", solution.postorderStr("ABc", "BAC", "A"));
        }

        @Test
        @DisplayName("中序含非大写字母 → \"\"")
        void inorderHasInvalidChar() {
            assertEquals("", solution.postorderStr("ABC", "BaC", "A"));
        }

        @Test
        @DisplayName("两串长度不等 → \"\"")
        void differentLengths() {
            assertEquals("", solution.postorderStr("ABCD", "BAC", "A"));
        }

        @Test
        @DisplayName("两串长度相等但元素不同 → \"\"")
        void sameLength_differentElements() {
            assertEquals("", solution.postorderStr("ABC", "DEF", "A"));
        }

        @Test
        @DisplayName("包含小写字母 → \"\"")
        void lowercaseLetter() {
            assertEquals("", solution.postorderStr("ABc", "BAC", "A"));
        }

        @Test
        @DisplayName("空字符串 → \"\"")
        void emptyStrings() {
            assertEquals("", solution.postorderStr("", "", "A"));
        }

        @Test
        @DisplayName("待删节点非大写字母 → \"\"")
        void deleteNodeNotUpperCase() {
            assertEquals("", solution.postorderStr("ABC", "BAC", "a"));
        }
    }

    // ==================== 树重建与遍历 ====================

    @Nested
    @DisplayName("树重建与遍历")
    class TreeReconstruction {

        @Test
        @DisplayName("单节点树: \"A\",\"A\",B → \"A\"（B不存在不做操作）")
        void singleNode() {
            // 树只有A，B不在树中 → 后序: A
            assertEquals("A", solution.postorderStr("A", "A", "B"));
        }

        @Test
        @DisplayName("两节点: 根+左子 \"AB\",\"BA\",C → \"BA\"")
        void rootAndLeftChild() {
            // 前序AB→A根B左, 中序BA→B左A根, C不存在
            assertEquals("BA", solution.postorderStr("AB", "BA", "C"));
        }

        @Test
        @DisplayName("两节点: 根+右子 \"AC\",\"AC\",B → \"CA\"")
        void rootAndRightChild() {
            // 前序AC→A根C右, 中序AC→A根C右
            assertEquals("CA", solution.postorderStr("AC", "AC", "B"));
        }

        @Test
        @DisplayName("三节点完全树 \"ABC\",\"BAC\",D → \"BCA\"")
        void completeThreeNodes() {
            assertEquals("BCA", solution.postorderStr("ABC", "BAC", "D"));
        }

        @Test
        @DisplayName("左斜树 \"CBA\",\"CBA\",D → \"ABC\"")
        void leftSkewed() {
            // C→B→A, 全左子
            assertEquals("ABC", solution.postorderStr("CBA", "CBA", "D"));
        }

        @Test
        @DisplayName("右斜树 \"ABC\",\"ABC\",D → \"CBA\"")
        void rightSkewed() {
            assertEquals("CBA", solution.postorderStr("ABC", "ABC", "D"));
        }
    }

    // ==================== 删除规则 ====================

    @Nested
    @DisplayName("删除规则")
    class DeleteRules {

        @Test
        @DisplayName("删除叶子节点: \"AB\",\"BA\",B → \"A\"")
        void deleteLeaf() {
            // A根B左叶, 删B → 只剩A, 后序: A
            assertEquals("A", solution.postorderStr("AB", "BA", "B"));
        }

        @Test
        @DisplayName("删除根节点不做操作: \"AB\",\"BA\",A → \"BA\"")
        void deleteRoot_noOp() {
            assertEquals("BA", solution.postorderStr("AB", "BA", "A"));
        }

        @Test
        @DisplayName("删除内部节点(左子): 保留左子树删右子树")
        void deleteInternal_leftChild() {
            // 树: A根, B左子(内部节点), C(B的左子), D(B的右子)
            // 前序: A B C D → 中序: C B D A
            // 删除B(左子): 保留B的左子树C挂到A, 删B和B的右子树D
            // 结果: A根, C左子 → 后序: CA
            assertEquals("CA", solution.postorderStr("ABCD", "CBDA", "B"));
        }

        @Test
        @DisplayName("删除内部节点(右子): 保留右子树删左子树")
        void deleteInternal_rightChild() {
            // 树: A根, B右子(内部节点), C(B的左子), D(B的右子)
            // 前序: A B C D → 中序: A C B D
            // 删除B(右子): 保留B的右子树D挂到A, 删B和B的左子树C
            // 结果: A根, D右子 → 后序: DA
            assertEquals("DA", solution.postorderStr("ABCD", "ACBD", "B"));
        }
    }

    // ==================== ⚠️ Bug 暴露用例 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露用例")
    class BugRevealing {

        @Test
        @DisplayName("Bug1: 树重建错误 — 用中序中间值当根")
        void treeReconstruction_bug() {
            // 前序:BCA 中序:BAC → 树应为: B根, A左, C右
            // 后序应为: ACB
            assertEquals("ACB", solution.postorderStr("BCA", "BAC", "D"));
        }

        @Test
        @DisplayName("Bug2: String不可变 — toPreorderStr 结果永远是\"\"")
        void stringImmutability_bug() {
            // 任何有效输入都会因为 newPreorderStr="" 而被误判为无效
            assertEquals("BCA", solution.postorderStr("ABC", "BAC", "D"));
        }

        @Test
        @DisplayName("Bug3: 删除只搜索一层深度")
        void deleteSearchDepth_bug() {
            // 四层树: 前序"ABCD",中序"DCBA"(左斜), 删除C(深度2)
            // deleteSubLeafNode只检查root的直接子节点，找不到C
            // 预期删除C(叶子)，后序"DBA"
            // 但代码找不到C
            assertEquals("DBA", solution.postorderStr("ABCD", "DCBA", "C"));
        }

        @Test
        @DisplayName("Bug4: 不处理叶子节点删除")
        void deleteLeaf_bug() {
            // 前序"AB",中序"BA" → A根B左叶
            // deleteSubLeafNode检查B: left=null && right=null → 条件false → 不删
            assertEquals("A", solution.postorderStr("AB", "BA", "B"));
        }

        @Test
        @DisplayName("Bug5: 循环索引用preorderStr长度访问inorderStr")
        void indexMismatch_bug() {
            // preorderStr="ABC", inorderStr="AB" → 长度不等
            // for i<3 但 inorderStr.charAt(2) → OOB
            assertEquals("", solution.postorderStr("ABC", "AB", "A"));
        }

        @Test
        @DisplayName("综合: 完整删除流程")
        void fullDeleteFlow() {
            // 树: D根, B左, E右, A(B的左), C(B的右)
            // 前序:D B A C E, 中序:A B C D E
            // 删除B(内部,左子): 保留A(左子树), 删B和C(右子树)
            // 结果: D根, A左, E右 → 后序: AED
            assertEquals("AED", solution.postorderStr("DBACE", "ABCDE", "B"));
        }
    }
}
