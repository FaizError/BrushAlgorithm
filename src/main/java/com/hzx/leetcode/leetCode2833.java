package com.hzx.leetcode;

public class leetCode2833 {

    public int furthestDistanceFromOrigin(String moves) {
        int l = 0;
        int r = 0;
        int abs = 0;

        for (int i = 0; i < moves.length();i++){
            if(moves.charAt(i) == 'L'){
                l++;
            }else if(moves.charAt(i) == 'R'){
                r++;
            }else if(moves.charAt(i) == '_'){
                abs++;
            }
        }

        return l - r == 0 ? abs : Math.abs(l - r) + abs;
    }
}
