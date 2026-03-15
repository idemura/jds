package com.github.idemura;

public class GrayCode {
  public long getGrayCode(long index) {
    return index ^ (index >>> 1);
  }
}
