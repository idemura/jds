package com.github.idemura;

public class BinarySearch {
  // l - left index (inclusive), r - right index (exclusive).
  public static int findLeft(int l, int r, CompareAnchorWith anchorLessOrEqual) {
    while (l < r) {
      int m = l + (r - l) / 2;
      if (anchorLessOrEqual.compare(m)) {
        r = m;
      } else {
        l = m + 1;
      }
    }
    return l;
  }

  public static int findLeft(int[] array, int x, int l, int r) {
    return findLeft(l, r, (i) -> x <= array[i]);
  }

  public static int findLeft(int[] array, int x) {
    return findLeft(array, x, 0, array.length);
  }

  // l - left index (inclusive), r - right index (exclusive).
  public static int findRight(int l, int r, CompareAnchorWith anchorLess) {
    while (l < r) {
      int m = l + (r - l) / 2;
      if (anchorLess.compare(m)) {
        r = m;
      } else {
        l = m + 1;
      }
    }
    return l;
  }

  public static int findRight(int[] array, int x, int l, int r) {
    return findRight(l, r, (i) -> x < array[i]);
  }

  public static int findRight(int[] array, int x) {
    return findRight(array, x, 0, array.length);
  }
}
