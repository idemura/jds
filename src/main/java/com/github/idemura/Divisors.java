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

  public static class ExtendedGcdResult {
    public long gcd;
    public long ka;
    public long kb;

    public void setCoefficients(long ka, long kb) {
      this.ka = ka;
      this.kb = kb;
    }
  }

  public static ExtendedGcdResult extendedGcd(long a, long b) {
    if (a < b) {
      var res = extendedGcdRecStep(b, a);
      res.setCoefficients(res.kb, res.ka);
      return res;
    } else {
      return extendedGcdRecStep(a, b);
    }
  }

  private static ExtendedGcdResult extendedGcdRecStep(long a, long b) {
    if (b == 0) {
      var res = new ExtendedGcdResult();
      res.gcd = a;
      res.setCoefficients(1, 0);
      return res;
    } else {
      long q = a / b;
      long r = a % b;
      var res = extendedGcdRecStep(b, r);
      res.setCoefficients(res.kb, res.ka - res.kb * q);
      return res;
    }
  }
}
