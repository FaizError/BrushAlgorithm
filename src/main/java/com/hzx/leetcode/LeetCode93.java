package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.List;

public class LeetCode93 {

    public List<String> restoreIpAddresses(String s) {
        // 定义返回结果
        List<String> res = new ArrayList<>();

        // 前置无效范围拦截
        if(s == null || s.length() < 4){
            return res;
        }

        // 中间状态存储
        List<String> path = new ArrayList<>();

        // 回溯结果
        dfs(s,res,path);
        return res;
    }

    private void  dfs(String s,List<String> res,List<String> path){

        // 长度为0时 中断
        if(s.length() == 0){

            // 长度为4 同时元素没有了 证明符合预期 保存结果
            if(path.size() == 4){
                res.add(String.join(".", path));
            }
            return;
        }

        // 长度为4但是还有元素 证明不符合结果 直接中断
        if(path.size() == 4){
            return;
        }

        for (int i = 1; i <= s.length(); i++) {

            // ip地址每一个节点长度都不会超过3
            if(i > 3){
                return;
            }
            String substr= s.substring(0, i);

            if(substr.length() == 1 ||
                    (substr.length() == 2 && substr.charAt(0) != '0') ||
                    (substr.length() == 3 && substr.charAt(0) != '0' && substr.compareTo("255") <= 0)){
                path.add(substr);
                dfs(s.substring(i),res,path);
                path.remove(path.size()-1);
            }
        }

    }


}
