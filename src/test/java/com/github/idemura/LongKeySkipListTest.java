package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class LongKeySkipListTest {
  private static LongKeyMap<String> createSkipList() {
    return new LongKeySkipList<>();
  }

  @Test
  void testPutWithReplace() {
    var sl = createSkipList();
    sl.put(200, "1");
    assertEquals("1", sl.get(200));
    sl.put(250, "2");
    assertEquals("1", sl.get(200));
    assertEquals("2", sl.get(250));
    assertNull(sl.get(300));
    assertNull(sl.get(100));
    sl.put(250, "3");
    assertEquals("1", sl.get(200));
    assertEquals("3", sl.get(250));
    assertNull(sl.get(300));
    assertNull(sl.get(100));
  }

  @Test
  void testLongTest16() {
    var keys = List.of(18, 16, 17, 63, 89, 14, 36, 54, 78, 37, 70, 30, 37, 23, 54, 69);
    var sl = createSkipList();
    for (int key : keys) {
      sl.put(key, "dummy");
    }
    for (int key : keys) {
      assertEquals("dummy", sl.get(key));
    }
  }

  @Test
  void testDescendingInsertions() {
    var keys = List.of(90, 80, 70, 60, 50, 40, 30, 20, 10);
    var sl = createSkipList();
    for (int key : keys) {
      sl.put(key, "dummy");
    }
    for (int key : keys) {
      assertEquals("dummy", sl.get(key));
    }
    assertNull(sl.get(0));
    assertNull(sl.get(100));
  }

  @Test
  void testSupportsNegativeAndLargeLongKeys() {
    var sl = createSkipList();
    sl.put(Long.MIN_VALUE, "-9");
    sl.put(-1, "-1");
    sl.put(0, "0");
    sl.put(Long.MAX_VALUE, "9");

    assertEquals("-9", sl.get(Long.MIN_VALUE));
    assertEquals("-1", sl.get(-1));
    assertEquals("0", sl.get(0));
    assertEquals("9", sl.get(Long.MAX_VALUE));
    assertNull(sl.get(1));
  }

  @Test
  void testMixedDeterministicSequence() {
    var keys = List.of(50, 10, 70, 30, 90, 20, 80, 40, 60, 30, 70, 10);
    var sl = createSkipList();
    for (int i = 0; i < keys.size(); i++) {
      sl.put(keys.get(i), "dummy");
    }
    for (int key : keys) {
      assertEquals("dummy", sl.get(key));
    }
    assertNull(sl.get(15));
    assertNull(sl.get(100));
  }
}
