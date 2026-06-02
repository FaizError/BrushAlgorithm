package com.hzx;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//        char[][] arr = new char[2][2];
//        LeetCode221 leetcode128 = new LeetCode221();
//        leetcode128.maximalSquare(arr);

        String a = "0o13";
        System.out.println(Integer.parseInt(a.substring(2), 8));

        int b = -5;
        b = ~b;
        System.out.println(Integer.toHexString(b));


        String c = "+";
        System.out.println(c.charAt(0) != '+');
    }


}
