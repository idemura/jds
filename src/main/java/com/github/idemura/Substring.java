package com.github.idemura;

import java.util.Arrays;

public class Substring {
  public Substring() {}

  public static int findSubstring(String pattern, String input) {
    return findSubstring(pattern.toCharArray(), input.toCharArray());
  }

  public static int findSubstring(char[] pattern, char[] input) {
    if (pattern.length > input.length) {
      return -1;
    }
    if (pattern.length == 0) {
      return 0;
    }
    // var advances = computeAdvance(pattern);
    // Logger.info("advances {}", Arrays.toString(advances));
    var lastCharIndex = new int[128];
    Arrays.fill(lastCharIndex, pattern.length);
    for (int k = 0; k < pattern.length; k++) {
      lastCharIndex[pattern[k]] = k;
    }
    int j = pattern.length - 1;
    int i = j;
    while (i < input.length) {
      if (input[i] == pattern[j]) {
        if (j == 0) {
          return i;
        }
        i--;
        j--;
      } else {
        int last = lastCharIndex[input[i]];
        // if (not found) {
        //
        // }else{
        //
        // }
        j = pattern.length - 1;
        // i += Math.max(lastCharIndex[input[i]], advances[j]);
        // i += ;
      }
    }
    return -1;
  }

  static int[] computeAdvance(char[] pattern) {
    int n = pattern.length;
    var result = new int[n];
    for (int i = 0; i < n; i++) {
      result[i] = n;
    }
    result[n - 1] = 0;
    return result;
  }
}
