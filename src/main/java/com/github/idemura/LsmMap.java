package com.github.idemura;


// int->int map
public class LsmMap {
//  private static final int DEFAULT_L0_CAPACITY = 16;
//
//  private static class Level {
//    int[] pairs;
//
//    Level(int capacity) {
//    }
//
//    int lookupLinear(int key, int n) {
//    }
//
//    int lookup(int key) {
//    }
//
//    // Sort level0
//    void sort() {
//    }
//  }
//
//  private final List<Level> levels = new ArrayList<>();
//  private int[] level0;
//  private int level0Size;
//  private int size;
//
//  public LsmMap() {
//    this(DEFAULT_L0_CAPACITY);
//  }
//
//  public LsmMap(int l0Capacity) {
//    this.l0 = new int[l0Capacity * 2];
//  }
//
//  public void put(int key, int val) {
////    if (lookup(key) >= 0) {
////      return;
////    }
//
//
//    if (levels.size() == 0) {
//      levels.add(new Level(L0));
//    }
//
//    size++;
//
//    if (l0Size < L0) {
//      var l = levels.get(0);
//      l.keys[l0Size] = key;
//      l.vals[l0Size] = val;
//      l0Size++;
//      return;
//    }
//
//    // Compact
//    levels.get
//    var l = le
//    for
//  }
//
//  private long lookup(int key) {
//    if (levels.size() == 0) {
//      return -1;
//    }
//
//    int j = levels.get(0).lookupLinear(key, l0Size);
//    if (j >= 0) {
//      return encode(0, j);
//    }
//    for (int i = 1; i < levels.size(); i++) {
//      j = levels.get(i).lookup(key);
//      if (j >= 0) {
//        return encode(i, j);
//      }
//    }
//    return -1;
//  }
//
//  private static long encode(int level, int j) {
//    return (((long) level) << 32) | j;
//  }
}
