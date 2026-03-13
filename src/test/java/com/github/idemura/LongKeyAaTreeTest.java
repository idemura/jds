package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LongKeyAaTreeTest {
  @Test
  void testPut() {
    var t = new LongKeyAaTree<String>();
    assertEquals(0, t.size());
  }
}
