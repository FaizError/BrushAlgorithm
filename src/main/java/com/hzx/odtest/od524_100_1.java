package com.hzx.odtest;

import java.util.Arrays;

public class od524_100_1 {


    /**
     * 题目描述：在微服务网关中，为了防止某个用户短时间内发送过多请求，通常会采用最小请求间隔限流策略： 即同一个用户相邻两个请求的时间戳之差必须大于等于mininterval秒。例如，若'minlnterval=2'，则请求时间戳为'1'和'3'可以同时通过（差为2)，但'1'和'2'不能同时通过（差为1)。
     * 现有一批属于同一用户的请求，每个请求带有一个时间戳（单位：秒，整数）。网关需要从这批请求中"＊挑选一部分放行"*，使得任意两个被放行的请求时间差都2'mininterval'。请你计算＊＊一共有多少种合法的放行方案"（包括空集）。例如，请求时间戳为［1,3,4]',minlnterval=2'，则合法的放行方案有：'[]'、[1]'、[3]'、[4]、[1,3]、[1,4]，共"6＊种（注意［3,4］非法，因为差为1<2)。
     *
     * 补充说明：输入输出说明
     * ﹣输入格式
     * - int0 timestamps': 整数数组，表示每个请求的时间戳（可能乱序，无重复）。
     * -'int minlnterval'：最小允许的请求间隔（秒）,'minlnterval ≥ 1'。
     * ﹣输出格式
     * -'int'：合法放行方案的总数。
     * - 数据规模
     * -1 s timestamps.length ≤ 15'
     * ﹣时间戳取值范围：'0 s timestamp s 10^9'
     * -'minlnterval'为正整数
     *
     * 示例1
     * 输入：[1,2,4],2
     * 输出：6
     * 说明：合法方案：'、[1]'、[2]、[4]'、[1,4]'、'[2,4]'，共6种
     * 示例2
     * 输入：[10],5
     * 输出：2
     * 说明：合法方案：'0]'、[10]'，共2种
     * ————————————————
     * 版权声明：本文为CSDN博主「南山马客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/Chennai585/article/details/161424668
     */

//    public int getSum(int[] timestamps, int minInterval) {
//
//        Arrays.sort(timestamps);
//
//        int count = timestamps.length + 1;
//
//        for (int i = 0; i < timestamps.length; i++) {
//            int right = timestamps.length - 1;
//            while (i < right) {
//                if (timestamps[right] - timestamps[i] >= minInterval) {
//                    count++;
//                }
//                right--;
//            }
//        }
//
//        return count;
//    }
    public int getSum(int[] timestamps, int minInterval) {
        Arrays.sort(timestamps);
        int n = timestamps.length;
        int[] f = new int[n];
        int total = 1;
        for (int i = 0; i < n; i++) {
            f[i] = 1;
            for (int j = 0; j < i; j++) {
                if (timestamps[i] - timestamps[j] >= minInterval) {
                    f[i] += f[j];
                }
            }
            total += f[i];
        }
        return total;
    }


}
