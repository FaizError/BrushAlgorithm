package com.hzx.simulationexercise;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例
 * 2. 边界条件（s短于p、s等于p、p=1等）
 * 3. 基本功能（单匹配、无匹配、多匹配）
 * 4. 重叠异位词
 * 5. 综合场景
 */
@DisplayName("统计异位词子串数量算法测试")
class test4Test {

    private test4 solution;

    @BeforeEach
    void setUp() {
        solution = new test4();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: s=cbaebabacd, p=abc → 2")
        void example1() {
            // 匹配子串: "cba"(0-2), "bac"(6-8)
            assertEquals(2, solution.countAnagrams("cbaebabacd", "abc"));
        }

        @Test
        @DisplayName("示例2: s=abab, p=ab → 3")
        void example2() {
            // 匹配子串: "ab"(0-1), "ba"(1-2), "ab"(2-3)
            assertEquals(3, solution.countAnagrams("abab", "ab"));
        }
    }

    // ==================== 边界条件 ====================

    @Nested
    @DisplayName("边界条件")
    class EdgeCases {

        @Test
        @DisplayName("s 长度 < p 长度 → 0")
        void sShorterThanP() {
            assertEquals(0, solution.countAnagrams("ab", "abc"));
        }

        @Test
        @DisplayName("s == p（恰好是异位词）→ 1")
        void sEqualsP_anagram() {
            assertEquals(1, solution.countAnagrams("cba", "abc"));
        }

        @Test
        @DisplayName("s == p 但不是异位词 → 0")
        void sEqualsP_notAnagram() {
            assertEquals(0, solution.countAnagrams("abc", "abd"));
        }

        @Test
        @DisplayName("p 长度为 1 → 统计 s 中等于 p[0] 的字符数")
        void pLength1() {
            // s="abca", p="a" → 位置0和3，共2个
            assertEquals(2, solution.countAnagrams("abca", "a"));
        }

        @Test
        @DisplayName("p 长度为 1，s 中无匹配 → 0")
        void pLength1_noMatch() {
            assertEquals(0, solution.countAnagrams("hello", "z"));
        }

        @Test
        @DisplayName("s 和 p 长度相同且只有一个字符")
        void singleCharBoth() {
            assertEquals(1, solution.countAnagrams("a", "a"));
        }
    }

    // ==================== 基本功能 ====================

    @Nested
    @DisplayName("基本功能")
    class BasicFunctionality {

        @Test
        @DisplayName("无任何匹配")
        void noMatch() {
            // s="abcdef", p="xyz" → 完全不重叠
            assertEquals(0, solution.countAnagrams("abcdef", "xyz"));
        }

        @Test
        @DisplayName("窗口恰好位于字符串开头")
        void matchAtBeginning() {
            // s="cbaxyz", p="abc" → "cba"(0-2)匹配，"bax"(1-3)✗，"axy"(2-4)✗，"xyz"(3-5)✗
            assertEquals(1, solution.countAnagrams("cbaxyz", "abc"));
        }

        @Test
        @DisplayName("窗口恰好位于字符串末尾")
        void matchAtEnd() {
            // s="xybac", p="abc" → "xyb"(0-2)✗，"yba"(1-3)✗，"bac"(2-4)在末尾匹配
            assertEquals(1, solution.countAnagrams("xybac", "abc"));
        }

        @Test
        @DisplayName("p 含重复字母")
        void pHasDuplicateLetters() {
            // s="aabb", p="aab" → 窗口:"aab"(0-2)✓, "abb"(1-3)✗ (a数量不同)
            assertEquals(1, solution.countAnagrams("aabb", "aab"));
        }

        @Test
        @DisplayName("p 含重复字母、多个匹配")
        void pHasDuplicates_multipleMatches() {
            // s="aabbaa", p="aab" → "aab"(0-2)✓, "abb"(1-3)✗, "bba"(2-4)✗, "baa"(3-5)✓
            assertEquals(2, solution.countAnagrams("aabbaa", "aab"));
        }

        @Test
        @DisplayName("s 中全部窗口都是异位词（所有字母相同）")
        void allWindowsMatch() {
            // s="aaaa", p="aa" → "aa"(0-1)✓, "aa"(1-2)✓, "aa"(2-3)✓
            assertEquals(3, solution.countAnagrams("aaaa", "aa"));
        }
    }

    // ==================== 重叠异位词 ====================

    @Nested
    @DisplayName("重叠异位词")
    class OverlappingAnagrams {

        @Test
        @DisplayName("连续重叠匹配")
        void consecutiveOverlapping() {
            // s="abcba", p="abc"
            // 窗口: "abc"(0-2)✓, "bcb"(1-3)✗, "cba"(2-4)✓
            assertEquals(2, solution.countAnagrams("abcba", "abc"));
        }

        @Test
        @DisplayName("交替模式重叠")
        void alternatingOverlap() {
            // s="ababa", p="ab"
            // "ab"(0-1)✓, "ba"(1-2)✓, "ab"(2-3)✓, "ba"(3-4)✓
            assertEquals(4, solution.countAnagrams("ababa", "ab"));
        }

        @Test
        @DisplayName("相同字母重叠且p较长")
        void allSameChar_longP() {
            // s="aaaaa", p="aaa" → 窗口: [0-2][1-3][2-4] 共3个
            assertEquals(3, solution.countAnagrams("aaaaa", "aaa"));
        }
    }

    // ==================== 综合场景 ====================

    @Nested
    @DisplayName("综合场景")
    class ComprehensiveScenarios {

        @Test
        @DisplayName("较长字符串，p覆盖全部26个字母")
        void fullAlphabet_p() {
            // p包含a-z各1个，在s中找匹配
            String p = "abcdefghijklmnopqrstuvwxyz";
            // s = p + "zzz"  → 只有开头1个匹配（zzz无法构成a-z各1个）
            assertEquals(1, solution.countAnagrams(p + "zzz", p));
        }

        @Test
        @DisplayName("s 远大于 p，验证滑动效率")
        void longString() {
            // "abc" 出现在 s 的多个位置
            // s = "abcabcabcabc", p = "abc"
            // 10个窗口都是abc的排列: abc✓, bca✓, cab✓, abc✓, bca✓, cab✓, abc✓, bca✓, cab✓, abc✓
            assertEquals(10, solution.countAnagrams("abcabcabcabc", "abc"));
        }

        @Test
        @DisplayName("只有部分字母重叠的窗口")
        void partialOverlap() {
            // s="baaebabacd", p="abc"
            // 窗口: "baa"(0-2)✗, "aae"(1-3)✗, "aeb"(2-4)✗, "eba"(3-5)✗, "bab"(4-6)✗, "aba"(5-7)✗, "bac"(6-8)✓, "acd"(7-9)✗
            assertEquals(1, solution.countAnagrams("baaebabacd", "abc"));
        }

        @Test
        @DisplayName("p 长度等于 s 长度，恰好匹配")
        void pLengthEqualsS() {
            assertEquals(1, solution.countAnagrams("dcba", "abcd"));
        }

        @Test
        @DisplayName("p 长度等于 s 长度，但不匹配")
        void pLengthEqualsS_noMatch() {
            assertEquals(0, solution.countAnagrams("dcba", "abce"));
        }
    }
}
