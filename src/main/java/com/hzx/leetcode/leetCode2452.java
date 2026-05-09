package com.hzx.leetcode;

import java.util.ArrayList;
import java.util.List;

public class leetCode2452   {

    public List<String> twoEditWords(String[] queries, String[] dictionary) {
        List<String> ans = new ArrayList<>();
        for (String q : queries){
            for (String d : dictionary){
                int sum = 0;
                for (int i = 0;i < q.length();i++){
                    if(q.charAt(i) != d.charAt(i)){
                        sum++;
                    }
                    if(sum > 2){
                        break;
                    }
                }
                if(sum <= 2){
                    ans.add(q);
                    break;
                }
            }
        }
        return ans;
    }
}

class Solution {
    public List<String> twoEditWords(String[] queries, String[] dictionary) {
        List<String> list = new ArrayList<String>();
        for (String query : queries) {
            if (isTwoEdit(query, dictionary)) {
                list.add(query);
            }
        }
        return list;
    }

    public boolean isTwoEdit(String query, String[] dictionary) {
        int n = query.length();
        for (String word : dictionary) {
            int edit = 0;
            for (int i = 0; i < n && edit <= 2; i++) {
                if (query.charAt(i) != word.charAt(i)) {
                    edit++;
                }
            }
            if (edit <= 2) {
                return true;
            }
        }
        return false;
    }
}