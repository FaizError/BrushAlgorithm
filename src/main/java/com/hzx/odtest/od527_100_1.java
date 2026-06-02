package com.hzx.odtest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class od527_100_1 {


    /**
     * 题目描述：9月份开学，小学某班级要举行班长选举，全班1人1票把心目中的班长人选姓名写在投票纸上。最终得票最多的当选班长，如果票数相等，按姓名字母顺序靠前的人当选班长。
     * 投票约定：
     * 1、若学生间存在重名，首个同学按原名，后面的携带＋n编号，例如李伟，李伟1，李伟2。
     * 2、若选票中书写的名字不存在，视为废票（也适用于约定1)。
     * 3、若出现票数多于全班人数，本次票选无效，即选举失败。
     * 4、若有人当选则认为选举成功，没人当选，则认为选举失败。
     * 输入：
     * 1、全班学生姓名字符串集合。
     * 2、全部投票数据字符串集合。
     * 输出：
     * 1、若选举成功，给出班长的姓名（重名的情况需要按照上面约定1输出）。
     * 2、若选举失败，返回"Invalid election..
     *
     * 补充说明
     * 示例1
     * 输入：
     * ["Zhangsan"."Lisi","Wangwu"].["Zhangsan"."Lisi"，"Zhangsan"]
     * 输出："Zhangsan"
     * 说明：3个同学中Zhangsan得了2票，Lisi得1票，Zhangsan票数最高，因此当选班长。
     * 示例2
     * 输入：["Zhangsan", "Lisi","Wangwu'].["Zhangsan", "Zhaoliu","Zhaoliu"]
     * 输出："Zhangsan"
     * 说明：Zhangsan得1票，Zhaoliu得了2票，但是并不是班集成员，因而Zhaoliu的选票无效，Zhangsan当选班长。
     * 示例3
     * 输入：
     * ['Zhangsan","Lisi","Wangwu","Zhangsan"].["Zhangsan"."ZhangsanO", "Zhangsanl', "Zhangsano"]
     * 输出："Zhangsan"
     * 说明：存在重名的Zhangsan，按规则投票时，前后2个张三分别需要写Zhangsan和Zhangsan1。而实际投票有3票是Zhangsan0,1票Zhangsan1，由于代码报告
     * Zhangsan0是不规范的选票，因此Zhangan1当选班长。
     * ————————————————
     * 版权声明：本文为CSDN博主「南山马客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/Chennai585/article/details/161492476
     *
     * @return
     */
    public String selectMonitor(List<String> students, List<String> votes) {
        //Invalid election
        Map<String, Integer> map = new HashMap<>();
//        Map<String, String> nameMap = new HashMap<>();
        for (String student : students) {

            if (map.containsKey(student)) {
                int index = 1;
                while (map.containsKey(student + index)) {
                    index++;
                }
                map.put(student + index, 1);
//                nameMap.put(student + index, student);
            } else {
                map.put(student, 1);
//                nameMap.put(student, student);
            }

        }

        // 投票计数
        Map<String, Integer> votesMap = new HashMap<>();
        for (String vote : votes) {
            if (!map.containsKey(vote)) {
                continue;
            }

            Integer count = votesMap.getOrDefault(vote, 0);
            votesMap.put(vote, count + 1);
        }

        int sum = 0;
        String name = "";
        int max = 0;
        for (String student : votesMap.keySet()) {
            if (votesMap.get(student) > max) {
                name = student;
                max = votesMap.get(student);
            } else if (votesMap.get(student) == max && (student.compareTo(name) < 0)) {
                name = student;
            }
            sum += votesMap.get(student);
        }

        if (sum > students.size()) {
            return "Invalid election";
        }


//        return nameMap.containsKey(name) ? nameMap.get(name) : "Invalid election";
        return name.isEmpty() ? "Invalid election" : name;
    }

}
