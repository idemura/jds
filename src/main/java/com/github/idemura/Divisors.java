package com.github.idemura;

public class Divisors {
  public static long gcd(long a, long b) {
    if (a < b) {
      return gcd(b, a);
    }
    while (b > 0) {
      long r = a % b;
      a = b;
      b = r;
    }
    return a;
  }
}
