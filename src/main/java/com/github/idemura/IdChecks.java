package com.github.idemura;

public class IdChecks {
  public static int requirePositiveElse(int v, int defaultValue) {
    return v > 0 ? v : defaultValue;
  }
}
