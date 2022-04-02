package com.example;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 返回 s 字典序最小的子序列，该子序列包含 s 的所有不同字符，且只包含一次
 */
public class SmallestSubsequence {
    public String smallestSubsequence(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        boolean[] inStack = new boolean[256];

        int[] leftCount = new int[256];
        for (char c: s.toCharArray()) {
            leftCount[c]++;
        }

        for (char c: s.toCharArray()) {
            leftCount[c]--;

            if (inStack[c]) {
                continue;
            }

            while (!stack.isEmpty() && leftCount[stack.peek()] > 0 && c < stack.peek()) {
                inStack[stack.pop()] = false;
            }

            stack.push(c);
            inStack[c] = true;
        }

        StringBuffer sb = new StringBuffer();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }

        return sb.reverse().toString();
    }
}
