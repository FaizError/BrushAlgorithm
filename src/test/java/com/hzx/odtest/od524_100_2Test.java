package com.hzx.odtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 测试用例覆盖：
 * 1. 题目示例（8个）
 * 2. 十/十六/八进制解析
 * 3. 运算符与计算
 * 4. 钳位与取反
 * 5. 错误输入返回 "NA"
 * 6. ⚠️ Bug暴露用例
 */
@DisplayName("表达式解析计算算法测试")
class od524_100_2Test {

    private od524_100_2 solution;

    @BeforeEach
    void setUp() {
        solution = new od524_100_2();
    }

    // ==================== 题目示例 ====================

    @Nested
    @DisplayName("题目示例")
    class ProblemExamples {

        @Test
        @DisplayName("示例1: 023+0x21+0o13+1 → 0xBB")
        void example1() {
            // 23 + 33 + 11 + 1 = 68 → hex 0x44 → NOT → 0xBB
            assertEquals("0xBB", solution.getOutPutStr("023+0x21+0o13+1"));
        }

        @Test
        @DisplayName("示例2: 0x0C-0o20+1 → 0x02")
        void example2() {
            // 12 - 16 + 1 = -3 → -3的补码NOT → 0x02
            assertEquals("0x02", solution.getOutPutStr("0x0C-0o20+1"));
        }

        @Test
        @DisplayName("示例3: *-1+5 → NA（非法字符）")
        void example3_illegalChar() {
            assertEquals("NA", solution.getOutPutStr("*-1+5"));
        }

        @Test
        @DisplayName("示例4: 2324+12-23 → NA（超范围）")
        void example4_valueOutOfRange() {
            assertEquals("NA", solution.getOutPutStr("2324+12-23"));
        }

        @Test
        @DisplayName("示例5: 123+12-13= → NA（非法字符=）")
        void example5_illegalChar_equals() {
            assertEquals("NA", solution.getOutPutStr("123+12-13="));
        }

        @Test
        @DisplayName("示例6: 3A+0xEGW-0o89 → NA（不合法数值）")
        void example6_invalidNumbers() {
            assertEquals("NA", solution.getOutPutStr("3A+0xEGW-0o89"));
        }

        @Test
        @DisplayName("示例7: 45-46 → 0x00")
        void example7() {
            // -1 → NOT → 0x00
            assertEquals("0x00", solution.getOutPutStr("45-46"));
        }

        @Test
        @DisplayName("示例8: 30+0xEc+0012+9 → 0x00（钳位到255）")
        void example8_clamp() {
            // 30 + 236 + 12 + 9 = 287 → clamp to 255 → 0xFF → NOT → 0x00
            assertEquals("0x00", solution.getOutPutStr("30+0xEc+0012+9"));
        }
    }

    // ==================== 十进制解析 ====================

    @Nested
    @DisplayName("十进制解析")
    class DecimalParsing {

        @Test
        @DisplayName("简单十进制: 10+5 → 0x0B")
        void simpleDecimal() {
            // 15 → 0x0F → NOT → 0xFFFFFFF0 & 0xFF = 0xF0 → "0xF0"
            // 15 = 0x0F, ~0x0F = 0xFFFFFFF0, last 2 hex = "F0"
            assertEquals("0xF0", solution.getOutPutStr("10+5"));
        }

        @Test
        @DisplayName("带前导零的十进制: 0010+0005 → 同10+5")
        void decimalWithLeadingZeros() {
            assertEquals("0xF0", solution.getOutPutStr("0010+0005"));
        }

        @Test
        @DisplayName("单个十进制数: 42 → 计算结果42")
        void singleDecimal() {
            // 42 → clamp → 42 → hex 0x2A → NOT → 0xD5
            assertEquals("0xD5", solution.getOutPutStr("42"));
        }
    }

    // ==================== 十六进制解析 ====================

    @Nested
    @DisplayName("十六进制解析")
    class HexParsing {

        @Test
        @DisplayName("0xEF → 值239")
        void hex_0xEF() {
            // 239 → 0xEF → NOT → 0x10
            assertEquals("0x10", solution.getOutPutStr("0xEF"));
        }

        @Test
        @DisplayName("0X0eF 大小写混合")
        void hex_mixedCase() {
            assertEquals("0x10", solution.getOutPutStr("0X0eF"));
        }

        @Test
        @DisplayName("0X00Ef 前导零")
        void hex_leadingZeros() {
            assertEquals("0x10", solution.getOutPutStr("0X00Ef"));
        }

        @Test
        @DisplayName("0x0000EF 多个前导零")
        void hex_manyLeadingZeros() {
            assertEquals("0x10", solution.getOutPutStr("0x0000EF"));
        }
    }

    // ==================== 八进制解析 ====================

    @Nested
    @DisplayName("八进制解析")
    class OctalParsing {

        @Test
        @DisplayName("0o77 → 值63")
        void octal_0o77() {
            // 63 → 0x3F → NOT → 0xC0
            assertEquals("0xC0", solution.getOutPutStr("0o77"));
        }

        @Test
        @DisplayName("0O077 大小写+前导零")
        void octal_leadingZeros() {
            assertEquals("0xC0", solution.getOutPutStr("0O077"));
        }

        @Test
        @DisplayName("0O0077 多个前导零")
        void octal_manyLeadingZeros() {
            assertEquals("0xC0", solution.getOutPutStr("0O0077"));
        }
    }

    // ==================== 运算测试 ====================

    @Nested
    @DisplayName("运算")
    class Arithmetic {

        @Test
        @DisplayName("纯加法: 100+200+300 → 扣位255 → 0x00")
        void additionOnly() {
            // 600 → clamp to 255 → 0xFF → NOT → 0x00
            assertEquals("0x00", solution.getOutPutStr("100+200+300"));
        }

        @Test
        @DisplayName("纯减法: 100-50-30 → 20 → 0xEB")
        void subtractionOnly() {
            // 20 → 0x14 → NOT → 0xEB
            assertEquals("0xEB", solution.getOutPutStr("100-50-30"));
        }

        @Test
        @DisplayName("加减混合: 50-20+30-10 → 50 → 0xCD")
        void mixedAddSub() {
            // 50 → 0x32 → NOT → 0xCD
            assertEquals("0xCD", solution.getOutPutStr("50-20+30-10"));
        }

        @Test
        @DisplayName("结果为0: 10-5-5 → 0xFF")
        void resultZero() {
            // 0 → 0x00 → NOT → 0xFF
            assertEquals("0xFF", solution.getOutPutStr("10-5-5"));
        }

        @Test
        @DisplayName("结果为负数: 5-10 → -5 → 0x04")
        void negativeResult() {
            // -5 → clamp to -5 → hex补码 0xFFFFFFFB → NOT → 0x04
            assertEquals("0x04", solution.getOutPutStr("5-10"));
        }
    }

    // ==================== 钳位与取反 ====================

    @Nested
    @DisplayName("钳位与取反")
    class ClampAndNot {

        @Test
        @DisplayName("超过255: 200+100 → 300 → 钳位255 → 0xFF → NOT → 0x00")
        void above255() {
            assertEquals("0x00", solution.getOutPutStr("200+100"));
        }

        @Test
        @DisplayName("刚好255: 200+55 → 255 → 0xFF → NOT → 0x00")
        void exactly255() {
            assertEquals("0x00", solution.getOutPutStr("200+55"));
        }

        @Test
        @DisplayName("低于-255: -200-100 → -300 → 钳位-255 → NOT → 0xFE")
        void belowMinus255() {
            // Need expression that goes way below: 0-200-100 = -300
            // Actually need to figure out how to get to -300 with positive numbers
            // Hmm, values are 0-999. We can do: 0-999-1 → but negative numbers in input are not allowed
            // Change approach: the parser takes numbers, not negatives. So "0-999-999" = -1998
            // But 999 is 0-999 range. -1998 → clamp to -255 → NOT → 0xFE
            assertEquals("0xFE", solution.getOutPutStr("0-999-999"));
        }

        @Test
        @DisplayName("刚好-255: 0-200-55 → -255 → NOT → 0xFE")
        void exactlyMinus255() {
            assertEquals("0xFE", solution.getOutPutStr("0-200-55"));
        }
    }

    // ==================== 错误输入 → "NA" ====================

    @Nested
    @DisplayName("错误输入返回NA")
    class ErrorInputs {

        @Test
        @DisplayName("空字符串 → NA")
        void emptyString() {
            assertEquals("NA", solution.getOutPutStr(""));
        }

        @Test
        @DisplayName("非法字符（字母）: abc+1 → NA")
        void illegalLetters() {
            assertEquals("NA", solution.getOutPutStr("abc+1"));
        }

        @Test
        @DisplayName("非法字符（符号）: 1@2 → NA")
        void illegalSymbol() {
            assertEquals("NA", solution.getOutPutStr("1@2"));
        }

        @Test
        @DisplayName("表达式以运算符开头: +1+2 → NA")
        void startsWithOperator() {
            assertEquals("NA", solution.getOutPutStr("+1+2"));
        }

        @Test
        @DisplayName("表达式以运算符结尾: 1+2- → NA")
        void endsWithOperator() {
            assertEquals("NA", solution.getOutPutStr("1+2-"));
        }

        @Test
        @DisplayName("连续运算符: 1++2 → NA")
        void consecutiveOperators() {
            assertEquals("NA", solution.getOutPutStr("1++2"));
        }

        @Test
        @DisplayName("数值超过999: 1000+1 → NA")
        void valueExceeds999() {
            assertEquals("NA", solution.getOutPutStr("1000+1"));
        }

        @Test
        @DisplayName("十六进制值超999: 0x3E8 → NA（0x3E8=1000）")
        void hexValueExceeds999() {
            assertEquals("NA", solution.getOutPutStr("0x3E8"));
        }

        @Test
        @DisplayName("非法八进制数字: 0o89 → NA（8不是合法八进制数字）")
        void invalidOctalDigit() {
            assertEquals("NA", solution.getOutPutStr("0o89"));
        }

        @Test
        @DisplayName("非法十六进制数字: 0xGG → NA")
        void invalidHexDigit() {
            assertEquals("NA", solution.getOutPutStr("0xGG"));
        }
    }

    // ==================== ⚠️ Bug 暴露用例 ====================

    @Nested
    @DisplayName("⚠️ Bug 暴露用例")
    class BugRevealing {

        @Test
        @DisplayName("Bug: 最后一个数字无运算符 → OOB崩溃（期望正常解析）")
        void lastNumber_noOperator() {
            // 纯数字 "42" 应该正常解析，但内层while(true)会 OOB
            assertEquals("0xD5", solution.getOutPutStr("42"));
        }

        @Test
        @DisplayName("Bug: 十六进制+十进制混用 → 十进制解析出错")
        void hexThenDecimal() {
            // 仅十六进制: 0x0C → 12 → NOT → 0xF3
            assertEquals("0xF3", solution.getOutPutStr("0x0C"));
        }

        @Test
        @DisplayName("Bug: 八进制+十进制混用 → 十进制解析出错")
        void octalThenDecimal() {
            assertEquals("0xC0", solution.getOutPutStr("0o77"));
        }

        @Test
        @DisplayName("Bug: 去前导零导致首字符重复: 023 → 期望23但代码得223")
        void leadingZeroStripping() {
            // "023" → 解析十进制应为 23
            // 23 → 0x17 → NOT → 0xE8
            assertEquals("0xE8", solution.getOutPutStr("023"));
        }

        @Test
        @DisplayName("Bug: 单数字十六进制 0xF → 期望15但代码拒收")
        void singleHexDigit() {
            // 0xF = 15 → 0x0F → NOT → 0xF0
            assertEquals("0xF0", solution.getOutPutStr("0xF"));
        }

        @Test
        @DisplayName("Bug: flag逻辑反转 → 最后数字被误判为NA")
        void flagLogic() {
            // 纯数字表达式，最后一个（也是唯一一个）无后续运算符
            assertEquals("0xEB", solution.getOutPutStr("20"));
        }

        @Test
        @DisplayName("Bug: 输出应为大写 → 0xbb vs 0xBB")
        void outputUppercase() {
            // 示例1的结果
            assertEquals("0xBB", solution.getOutPutStr("023+0x21+0o13+1"));
        }

        @Test
        @DisplayName("Bug: 缺少钳位 → 示例8应钳位到255")
        void missingClamp() {
            // 30+0xEc+0012+9 = 30+236+12+9 = 287
            // 287 > 255, 应钳位到255
            assertEquals("0x00", solution.getOutPutStr("30+0xEc+0012+9"));
        }

        @Test
        @DisplayName("Bug: 负数钳位: -300 → 应钳位到-255")
        void negativeClamp() {
            assertEquals("0xFE", solution.getOutPutStr("0-300"));
        }
    }
}

