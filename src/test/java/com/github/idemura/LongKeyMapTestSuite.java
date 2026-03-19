package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

final class LongKeyMapTestSuite {
  static void testEmpty(LongKeyMap<String> tree) {
    assertEquals(0, tree.size());
    assertNull(tree.get(10));
    tree.verify();
  }

  static void testSingleKey(LongKeyMap<String> tree) {
    tree.put(50, "50");
    assertEquals(1, tree.size());
    assertEquals("50", tree.get(50));
    assertNull(tree.get(40));
    tree.verify();
  }

  static void testSmallShapes(LongKeyMap<String> tree) {
    var random = new Random(1);
    for (int size = 2; size <= 8; size++) {
      for (int n = 0; n < 100; n++) {
        var keys = new ArrayList<Long>();
        for (int i = 0; i < size; i++) {
          keys.add((long) random.nextInt());
        }

        for (long key : keys) {
          tree.put(key, Long.toString(key));
        }
        tree.verify();

        Collections.shuffle(keys, random);
        for (long key : keys) {
          tree.remove(key);
          tree.verify();
        }
        assertEquals(0, tree.size());
      }
    }
  }

  static void testInsertThenRemove(LongKeyMap<String> tree) {
    var random = new Random(1);
    for (int seed = 0; seed < 100; seed++) {
      int n = random.nextInt(500);
      var keys = new ArrayList<Long>(n);
      for (int i = 0; i < n; i++) {
        keys.add((long) random.nextInt());
      }

      for (int k = 0; k < 50; k++) {
        for (long key : keys) {
          tree.put(key, Long.toString(key));
        }
        tree.verify();

        Collections.shuffle(keys, random);
        for (long key : keys) {
          tree.remove(key);
          tree.verify();
        }

        assertEquals(0, tree.size());
      }
    }
  }
}
