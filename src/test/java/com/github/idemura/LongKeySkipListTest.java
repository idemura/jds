package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class LongKeySkipListTest {
  @Test
  void testInsertAndPut1() {
    var sl = new LongKeySkipList<Integer>();
    sl.put(200, 1);
    assertEquals(1, sl.get(200));
    sl.put(250, 2);
    assertEquals(1, sl.get(200));
    assertEquals(2, sl.get(250));
    assertNull(sl.get(300));
    assertNull(sl.get(100));
    sl.put(250, 3);
    assertEquals(1, sl.get(200));
    assertEquals(3, sl.get(250));
    assertNull(sl.get(300));
    assertNull(sl.get(100));
  }

  @Test
  void testLongTest16() {
    var keys = List.of(18, 16, 17, 63, 89, 14, 36, 54, 78, 37, 70, 30, 37, 23, 54, 69);
    var sl = new LongKeySkipList<Integer>();
    Map<Integer, Integer> expected = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      expected.put(keys.get(i), i);
      sl.put(keys.get(i), i);
      verifyMaps(expected, sl);
    }
  }

  @Test
  void testDescendingInsertions() {
    var keys = List.of(90, 80, 70, 60, 50, 40, 30, 20, 10);
    var sl = new LongKeySkipList<Integer>();
    Map<Integer, Integer> expected = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      expected.put(keys.get(i), i);
      sl.put(keys.get(i), i);
      verifyMaps(expected, sl);
    }
    assertNull(sl.get(0));
    assertNull(sl.get(100));
  }

  @Test
  void testSupportsNegativeAndLargeLongKeys() {
    var sl = new LongKeySkipList<String>();
    sl.put(Long.MIN_VALUE, "min");
    sl.put(-1L, "minus-one");
    sl.put(0L, "zero");
    sl.put(Long.MAX_VALUE, "max");

    assertEquals("min", sl.get(Long.MIN_VALUE));
    assertEquals("minus-one", sl.get(-1L));
    assertEquals("zero", sl.get(0L));
    assertEquals("max", sl.get(Long.MAX_VALUE));
    assertNull(sl.get(1L));
  }

  @Test
  void testRepeatedUpdatesKeepLatestValue() {
    var sl = new LongKeySkipList<Integer>();
    sl.put(42, 1);
    sl.put(42, 2);
    sl.put(42, 3);
    sl.put(42, 4);

    assertEquals(4, sl.get(42));
    assertNull(sl.get(41));
    assertNull(sl.get(43));
  }

  @Test
  void testMixedDeterministicSequence() {
    var keys = List.of(50, 10, 70, 30, 90, 20, 80, 40, 60, 30, 70, 10);
    var sl = new LongKeySkipList<Integer>();
    Map<Integer, Integer> expected = new HashMap<>();
    for (int i = 0; i < keys.size(); i++) {
      expected.put(keys.get(i), i);
      sl.put(keys.get(i), i);
      verifyMaps(expected, sl);
    }
    assertNull(sl.get(15));
    assertNull(sl.get(100));
  }

  private static void verifyMaps(Map<Integer, Integer> expected, LongKeySkipList<Integer> sl) {
    for (var k : expected.keySet()) {
      assertEquals(expected.get(k), sl.get(k), "key=" + k);
    }
  }
}
