package com.hzx.odtest;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class od524_100_2 {

    /**
     * 题目描述：给出一个由字母、数字和加减运算符组成的简单表达式，做如下处理
     * 1、对字符串进行解析，获取8进制（0o或0O开头）、10进制、16进制（0x或0X开头）整数和＋、﹣运算符；
     * 2、对解析结果，按照表达式的顺序从左到右进行运算，得到10进制整数结果；
     * 3、把运算的整数结果调整到﹣255~255范围，如果值大于255，则取值255，如果值小于﹣255，则取﹣255;
     * 4、把调整后的结果转换成十六进制并进行取反运算；
     * 5、输出最终运算结果对应的字符串。
     * 输入：
     * string inputStr /／表达式
     * 输出：
     * string outputStr /／返回 "NA"或者 运算结果对应的字符串
     * 注：
     * 1、给定的inputStr是由字母、数字、+、﹣组成的字符串，长度为0~10000;
     * 2、关于inputStr中解析的整数部分，表示进制的x、o以及数值部分的字母a~f支持大小写，支持前缀为0整数串，解析的整数值范围为0~999，例如：
     * (1) "OxEF"、"OX0eF"、"OX00Ef"、"OX000EF"、0x0000EF"、等解析的整数16进制值为0xEF,10进制值为239;
     * (2)、"89"、"089"、"0089"、"00089"、"000089";等解析的整数10进制值为89;
     * (3)、"0o77"、"0O077"、"0O0077"、"0O000077 "、"0O0000077""等解析的整数8进制值为0o77,10进制值为63;
     * 3、如果输入表达式合法，则输出最终十六进制运算结果对应的字符串；否则输出 NA，例如：包含非法字符、长度超出范围、表达式不合法、值超出范围、输入有负数等；
     * 4、输出的十六进制字符串以"0x"为前缀，数值字符用大写，补齐两位，例如："0x00"、"0x0B"、"0xC9"、"0x2E"、"0xDF"、"0x3A"。
     * 补充说明：1、程序运行内存要小于256MB;
     * 2、程序运行耗时不能超过1秒。
     *
     * 示例1
     * 输入："023+0x21+0o13+1"
     * 输出： "OxBB"
     * 说明：计算结果为68，转换16进制为44，取反后16进制结果为BB
     * 示例2
     * 输入："0x0C-0o20+1"
     * 输出："0x02"
     * 说明：计算结果为﹣3，该负数的16进制补码值为FFFFFFFD，取反后16进制值为0x2
     * 示例3
     * 输入：*-1+5"
     * 输出："NA"
     * 说明：非法表达式，不支持负数
     * 示例4
     * 输入："2324+12-23"
     * 输出："NA"
     * 说明：表达式中整数超过限定范围
     * 示例5
     * 输入："123+12-13="
     * 输出："NA"
     * 说明：表达式中存在非法的空白字符
     * 示例6
     * 输入："3A+0xEGW-0o89"
     * 输出："NA"
     * 说明：表达式不合法，10进制、16进制、8进制的数都不正确
     * 示例7
     * 输入："45-46
     * 输出："0x00"
     * 说明：计算结果为﹣1，该负数的补码为FFFFFFFF，取反后为0
     * 示例8
     * 输入："30+0xEc+0012+9"
     * 输出："0x00"
     * 说明：输出结果为285，大于255，取值255,16进制为0xFF，取反后的16进制结果为0x00
     * ————————————————
     * 版权声明：本文为CSDN博主「南山马客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/Chennai585/article/details/161423972
     */

    public String getOutPutStr(String inPutStr) {

        int left = 0;
        List<Integer> list = new ArrayList<>();
        Queue<Character> queue = new ArrayDeque();
        if (inPutStr.charAt(left) < '0' || inPutStr.charAt(left) > '9') {
            return "NA";
        }
        while (left < inPutStr.length()) {
            String temp = "";
            boolean flag = true;
            while (true) {
                if (left > inPutStr.length() - 1) {
                    flag = false;
                    break;
                }
                if (inPutStr.charAt(left) == '+' || inPutStr.charAt(left) == '-') {
                    flag = false;
                    break;
                }
                temp = temp + inPutStr.charAt(left);
                left++;
            }

            boolean sixTeenFlag = true;
            boolean eightFlag = true;

            if (temp.charAt(0) == '0' && (temp.charAt(1) == 'x' || temp.charAt(1) == 'X')) {
                // 16进制
                if (!((temp.charAt(temp.length() - 2) <= 'e' && (temp.charAt(temp.length() - 2) >= 'a')) ||
                        (temp.charAt(temp.length() - 2) <= 'E' && (temp.charAt(temp.length() - 2) >= 'A')) ||
                        (temp.charAt(temp.length() - 2) <= '9' && (temp.charAt(temp.length() - 2) >= '0')))) {
                    return "NA";
                }

                if (!((temp.charAt(temp.length() - 1) <= 'f' && (temp.charAt(temp.length() - 1) >= 'a')) ||
                        (temp.charAt(temp.length() - 1) <= 'F' && (temp.charAt(temp.length() - 1) >= 'A')) ||
                        (temp.charAt(temp.length() - 1) <= '9' && (temp.charAt(temp.length() - 1) >= '0')))) {
                    return "NA";
                }
                list.add(Integer.parseInt(temp.substring(temp.length() - 2, temp.length()), 16));
                sixTeenFlag = false;
            }

            if (temp.charAt(0) == '0' && (temp.charAt(1) == 'o' || temp.charAt(1) == 'O')) {
                // 8进制
                if (!(temp.charAt(temp.length() - 2) <= '9' && (temp.charAt(temp.length() - 2) >= '0'))) {
                    return "NA";
                }

                if (!(temp.charAt(temp.length() - 1) <= '9' && (temp.charAt(temp.length() - 1) >= '0'))) {
                    return "NA";
                }

                list.add(Integer.parseInt(temp.substring(temp.length() - 2, temp.length()), 8));
                eightFlag = false;
            }

            boolean numFlag = true;
            int i = 0;
            String num = "";
            while (i < temp.length() && sixTeenFlag && eightFlag) {
                if (!numFlag) {
                    num = num + temp.charAt(i);
                }
                if (numFlag) {
                    if (temp.charAt(i) != '0') {
                        num = num + temp.charAt(i);
                        numFlag = false;
                    }
                }
                i++;
            }

            if (!num.isEmpty()) {
                int parsed = Integer.parseInt(num);
                if (parsed > 999) {
                    return "NA";
                }
                list.add(parsed);
            }

            if (left > inPutStr.length() - 1) {
                flag = false;
                break;
            }

            if (flag) {
                return "NA";
            }

            if (inPutStr.charAt(left) != '+' && inPutStr.charAt(left) != '-') {
                System.out.println(inPutStr.charAt(left));
                return "NA";
            }

            queue.add(inPutStr.charAt(left));
            left++;

        }

        if (list.size() <= 0) {
            return "NA";
        }

        int calculate = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            Character poll = queue.poll();
            if (poll == '+') {
                calculate = calculate + list.get(i);
            } else {
                calculate = calculate - list.get(i);
            }
        }

        if (calculate < -255) {
            calculate = -255;
        }
        if (calculate > 255) {
            calculate = 255;
        }
        calculate = ~calculate;
        String hexString = Integer.toHexString(calculate);

        return "0x" + Character.toUpperCase(hexString.charAt(hexString.length() - 2)) + Character.toUpperCase(hexString.charAt(hexString.length() - 1));
    }
}
