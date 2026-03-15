package com.github.idemura;

public class GrayCode {
  public static long toGrayCode(long n) {
    return n ^ (n >>> 1);
  }

  public static long fromGrayCode(long g) {
    long n = 0;
    while (g != 0) {
      n ^= g;
      g >>>= 1;
    }
    return n;
  }
}
