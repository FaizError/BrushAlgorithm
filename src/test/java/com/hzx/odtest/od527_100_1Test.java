package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 基本场景 — 明确胜出者
 * 2. 同名（重名）处理
 * 3. 废票（不存在的姓名）
 * 4. 票数 > 全班人数 → 选举失败
 * 5. 全部废票 → 无人当选 → 选举失败
 * 6. 平局按字母序打破
 * 7. 边界条件
 */
@DisplayName("班长选举算法测试")
class od527_100_1Test {

    private od527_100_1 solution;

    @BeforeEach
    void setUp() {
        solution = new od527_100_1();
    }

    // ==================== 示例测试 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: 简单多数票")
        void example1_simpleMajority() {
            List<String> students = Arrays.asList("Zhangsan", "Lisi", "Wangwu");
            List<String> votes = Arrays.asList("Zhangsan", "Lisi", "Zhangsan");
            assertEquals("Zhangsan", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("示例2: 存在废票（投票给不存在的人）")
        void example2_invalidVotes() {
            List<String> students = Arrays.asList("Zhangsan", "Lisi", "Wangwu");
            List<String> votes = Arrays.asList("Zhangsan", "Zhaoliu", "Zhaoliu");
            assertEquals("Zhangsan", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("示例3: 重名处理")
        void example3_duplicateNames() {
            List<String> students = Arrays.asList("Zhangsan", "Lisi", "Wangwu", "Zhangsan");
            List<String> votes = Arrays.asList("Zhangsan", "Zhangsan0", "Zhangsan1", "Zhangsan0");
            assertEquals("Zhangsan", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 基本功能测试 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicFunctionality {

        @Test
        @DisplayName("全票投给同一人")
        void unanimousVote() {
            List<String> students = Arrays.asList("Alice", "Bob", "Charlie");
            List<String> votes = Arrays.asList("Alice", "Alice", "Alice");
            assertEquals("Alice", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("每人各得一票（平局，字母序打破）")
        void eachOneVote_tieBreakByAlphabet() {
            List<String> students = Arrays.asList("Charlie", "Alice", "Bob");
            List<String> votes = Arrays.asList("Charlie", "Alice", "Bob");
            // Bob 1票, Alice 1票, Charlie 1票 → Alice 字母序最前
            assertEquals("Alice", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("单人班级")
        void singleStudent() {
            List<String> students = Collections.singletonList("OnlyOne");
            List<String> votes = Collections.singletonList("OnlyOne");
            assertEquals("OnlyOne", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("明确多数胜出（非全票）")
        void clearMajority() {
            List<String> students = Arrays.asList("Tom", "Jerry", "Spike", "Tyke");
            List<String> votes = Arrays.asList("Tom", "Jerry", "Tom");
            assertEquals("Tom", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 重名测试 ====================

    @Nested
    @DisplayName("重名（同名）处理")
    class DuplicateNames {

        @Test
        @DisplayName("两个重名，投票给第一个")
        void twoDuplicates_voteForFirst() {
            List<String> students = Arrays.asList("LiWei", "ZhangSan", "LiWei");
            // LiWei, LiWei1
            List<String> votes = Arrays.asList("LiWei", "ZhangSan");
            // LiWei 1票, ZhangSan 1票 → LiWei < ZhangSan 字母序 → LiWei 胜
            assertEquals("LiWei", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("两个重名，投票给第二个（带编号的）")
        void twoDuplicates_voteForSecond() {
            List<String> students = Arrays.asList("LiWei", "ZhangSan", "LiWei");
            // LiWei, LiWei1
            List<String> votes = Arrays.asList("LiWei1", "LiWei1");
            assertEquals("LiWei1", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("两个重名，分别投票给两人，平局字母序打破")
        void twoDuplicates_tieVotes() {
            List<String> students = Arrays.asList("LiWei", "ZhangSan", "LiWei");
            List<String> votes = Arrays.asList("LiWei", "LiWei1");
            // LiWei 1票, LiWei1 1票 → LiWei < LiWei1 → LiWei 胜
            assertEquals("LiWei", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("三个重名")
        void threeDuplicates() {
            List<String> students = Arrays.asList("A", "B", "A", "A");
            // A, A1, A2
            List<String> votes = Arrays.asList("A", "A1", "A2");
            // 各1票 → A < A1 < A2 → A胜
            assertEquals("A", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("三个重名，投票给中间编号的")
        void threeDuplicates_voteForMiddle() {
            List<String> students = Arrays.asList("A", "B", "A", "A");
            // A, A1, A2
            List<String> votes = Arrays.asList("A1", "A1", "A");
            // A1 2票, A 1票 → A1胜
            assertEquals("A1", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("重名投票：不存在的编号视为废票")
        void duplicateVote_invalidSuffix() {
            List<String> students = Arrays.asList("A", "B", "A");
            // A, A1
            List<String> votes = Arrays.asList("A", "A2", "A3");
            // A2, A3 不存在 → A得1票 → A胜
            assertEquals("A", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 废票测试 ====================

    @Nested
    @DisplayName("废票处理")
    class InvalidVotes {

        @Test
        @DisplayName("部分废票，有效票决定胜者")
        void partialInvalidVotes() {
            List<String> students = Arrays.asList("Alice", "Bob", "Charlie");
            List<String> votes = Arrays.asList("Alice", "Nobody", "Stranger", "Bob");
            // Nobody, Stranger 无效 → Alice 1票, Bob 1票 → Alice字母序胜
            assertEquals("Alice", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("全部废票 → 选举失败 ⚠️ BUG预期")
        void allInvalidVotes_shouldFail() {
            List<String> students = Arrays.asList("Alice", "Bob", "Charlie");
            List<String> votes = Arrays.asList("X", "Y", "Z");
            // 全部无效，无人当选 → 应返回 "Invalid election"
            // ⚠️ 当前代码bug: 返回 "" 而非 "Invalid election"
            assertEquals("Invalid election", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("重名场景：投给不规范编号 → 废票")
        void duplicateVote_nonStandardSuffix() {
            List<String> students = Arrays.asList("A", "B", "A");
            // A, A1
            List<String> votes = Arrays.asList("A0", "A-1", "A");
            // A0, A-1 无效 → A得1票 → A胜
            assertEquals("A", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 选举失败测试 ====================

    @Nested
    @DisplayName("选举失败场景")
    class ElectionFailure {

        @Test
        @DisplayName("票数多于全班人数")
        void votesExceedClassSize() {
            List<String> students = Arrays.asList("Alice", "Bob", "Charlie");
            // 3个学生，4张票
            List<String> votes = Arrays.asList("Alice", "Bob", "Charlie", "Alice");
            assertEquals("Invalid election", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("票数等于全班人数但全部废票")
        void votesEqualClassSize_allInvalid() {
            List<String> students = Arrays.asList("A", "B", "C");
            List<String> votes = Arrays.asList("X", "Y", "Z");
            assertEquals("Invalid election", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("空投票列表（无人投票）")
        void emptyVotes() {
            List<String> students = Arrays.asList("A", "B", "C");
            List<String> votes = Collections.emptyList();
            // 无人当选
            assertEquals("Invalid election", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 平局字母序测试 ====================

    @Nested
    @DisplayName("平局按字母序打破")
    class TieBreakAlphabetical {

        @Test
        @DisplayName("首字母不同")
        void differentFirstChar() {
            List<String> students = Arrays.asList("Bob", "Alice", "Charlie");
            List<String> votes = Arrays.asList("Bob", "Alice");
            // 各1票 → Alice < Bob → Alice胜
            assertEquals("Alice", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("首字母相同，后续字母不同 ⚠️ BUG预期")
        void sameFirstChar_differentRest() {
            List<String> students = Arrays.asList("Aa", "Ab");
            List<String> votes = Arrays.asList("Ab", "Aa");
            // 各1票 → "Aa" < "Ab" → Aa胜
            // ⚠️ 当前代码bug: 只比较首字符 'A'-'A'=0，不更新 → 结果依赖HashMap迭代顺序
            assertEquals("Aa", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("首字母相同，字符串长度不同 ⚠️ BUG预期")
        void sameFirstChar_differentLength() {
            List<String> students = Arrays.asList("Zhang", "Zhangsan");
            List<String> votes = Arrays.asList("Zhangsan", "Zhang");
            // 各1票 → "Zhang" < "Zhangsan" → Zhang胜
            // ⚠️ 当前代码bug: 只比较首字符 'Z'-'Z'=0
            assertEquals("Zhang", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("三人平局，首字母均不同")
        void threeWayTie_differentFirstChar() {
            List<String> students = Arrays.asList("Charlie", "Bob", "Alice");
            List<String> votes = Arrays.asList("Charlie", "Bob", "Alice");
            // Alice < Bob < Charlie → Alice胜
            assertEquals("Alice", solution.selectMonitor(students, votes));
        }
    }

    // ==================== 边界条件测试 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("票数刚好等于全班人数，全部有效")
        void votesEqualClassSize_allValid() {
            List<String> students = Arrays.asList("A", "B", "C");
            List<String> votes = Arrays.asList("A", "B", "C");
            // 各1票 → A字母序胜
            assertEquals("A", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("全班所有人都投给同一人")
        void entireClassVotesForOne() {
            List<String> students = Arrays.asList("Alice", "Bob", "Charlie");
            List<String> votes = Arrays.asList("Bob", "Bob", "Bob");
            // Bob 3票
            assertEquals("Bob", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("大小写混合 — 大写在小写之前（ASCII序）")
        void caseSensitivity() {
            List<String> students = Arrays.asList("abc", "ABC");
            List<String> votes = Arrays.asList("abc", "ABC");
            // ASCII: 'A'(65) < 'a'(97) → "ABC" < "abc"
            // 按照题目"字母顺序"，通常指字典序
            assertEquals("ABC", solution.selectMonitor(students, votes));
        }

        @Test
        @DisplayName("多个重名且原始名中带有数字")
        void duplicateWithNumberInName() {
            List<String> students = Arrays.asList("A1", "B", "A1");
            // A1 (原名), A11 (第二个A1)
            List<String> votes = Arrays.asList("A1", "A11");
            // 各1票 → "A1" < "A11" → A1胜
            assertEquals("A1", solution.selectMonitor(students, votes));
        }
    }
}
