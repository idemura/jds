package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LongKeyAaTreeTest {
  private static void putAndVerify(LongKeyAaTree<String> t, long key) {
    t.put(key, Long.toString(key));
    t.verify();
  }

  @Test
  void testPrimitiveShapes() {
    var t = new LongKeyAaTree<String>();
    t.verify();
    assertEquals(0, t.size());
    putAndVerify(t, 50);
    assertEquals(1, t.size());
    assertEquals("50", t.get(50));
    // Skew
    putAndVerify(t, 40);
    assertEquals("40", t.get(40));
    assertEquals("50", t.get(50));
    // Skew+split
    putAndVerify(t, 30);
    assertEquals("30", t.get(30));
    assertEquals("40", t.get(40));
    assertEquals("50", t.get(50));
  }
}
