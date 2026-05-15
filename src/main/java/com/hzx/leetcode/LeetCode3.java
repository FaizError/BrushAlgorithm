package com.hzx.leetcode;

import java.util.HashSet;
import java.util.Set;

public class LeetCode3 {

    public int lengthOfLongestSubstring(String s) {

        int ans = 0;

        char[] charArray = s.toCharArray();

        int index = -1;

        Set<Character> set = new HashSet<>();

        for (int i = 0; i < charArray.length - 1; i++) {

            if (i != 0) {
                set.remove(charArray[i - 1]);
            }

            while (index < charArray.length && !set.contains(charArray[index])) {
                set.add(charArray[index]);
                index++;
            }

            ans = Math.max(ans, index - i + 1);
        }

        return ans;
    }
}
