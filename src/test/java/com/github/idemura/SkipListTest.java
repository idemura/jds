package com.github.idemura;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SkipListTest {
  @Test
  void insertAndPut1() {
    var sl = new SkipList<Integer>();
    sl.put(200, 1);
    Assertions.assertEquals(1, sl.get(200));
    sl.put(250, 2);
    Assertions.assertEquals(1, sl.get(200));
    Assertions.assertEquals(2, sl.get(250));
    Assertions.assertNull(sl.get(300));
    Assertions.assertNull(sl.get(100));
    sl.put(250, 3);
    Assertions.assertEquals(1, sl.get(200));
    Assertions.assertEquals(3, sl.get(250));
    Assertions.assertNull(sl.get(300));
    Assertions.assertNull(sl.get(100));
  }

  @Test
  void longTest16() {
    var keys = List.of(18, 16, 17, 63, 89, 14, 36, 54, 78, 37, 70, 30, 37, 23, 54, 69);
    var sl = new SkipList<Integer>();
    Map<Integer, Integer> expected = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      expected.put(keys.get(i), i);
      sl.put(keys.get(i), i);
      verifyMaps(expected, sl);
    }
  }

  @Test
  void stressTest() {
    final int n = 120;
    var rg = new Random();
    var sl = new SkipList<Integer>();
    Map<Integer, Integer> expected = new HashMap<>();
    for (int i = 0; i < n; i++) {
      int k = rg.nextInt(120);
      expected.put(k, i);
      sl.put(k, i);
    }
    verifyMaps(expected, sl);
  }

  private static void verifyMaps(Map<Integer, Integer> expected, SkipList<Integer> sl) {
    for (var k : expected.keySet()) {
      Assertions.assertEquals(expected.get(k), sl.get(k), "key=" + k);
    }
  }
}
