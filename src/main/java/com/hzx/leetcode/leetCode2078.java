package com.hzx.leetcode;

public class leetCode2078 {
//    public int maxDistance(int[] colors) {
//        int abs = 0;
//        for (int i = 0; i < colors.length - 1;i++){
//            for (int j = i + 1; j < colors.length; j++){
//                if(colors[i] != colors[j]){
//                    abs = Math.max(abs,j - i);
//                }
//            }
//        }
//        return abs;
//
//    }
    public int maxDistance(int[] colors) {
        int n = colors.length;
        for (int i = 0; i < n; i++) {
            if (colors[0] != colors[n - 1 - i] || colors[i] != colors[n - 1]) {
                return n - 1 - i;
            }
            }

        return 0;
    }

}
