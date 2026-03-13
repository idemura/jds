package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class LongKeyAaTreeTest {
  private final LongKeyTree<String> tree = createTree();

  private static LongKeyTree<String> createTree() {
    return new LongKeyAaTree<>();
  }

  private void put(long key) {
    tree.put(key, Long.toString(key));
    tree.verify();
  }

  private void remove(long key) {
    tree.remove(key);
    tree.verify();
  }

  private void assertContains(long... keys) {
    for (long key : keys) {
      assertEquals(Long.toString(key), tree.get(key));
    }
  }

  private void assertNotFound(long... keys) {
    for (long key : keys) {
      assertNull(tree.get(key));
    }
  }

  @Test
  void testPrimitiveShapes() {
    tree.verify();
    assertEquals(0, tree.size());
    put(50);
    assertEquals(1, tree.size());
    assertEquals("50", tree.get(50));
    // Skew
    put(40);
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
    // Skew+split
    put(30);
    assertEquals("30", tree.get(30));
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
  }

  @Test
  void testRemoveRegression() {
    put(0);
    put(9);
    remove(15);
    remove(11);
    put(19);
    remove(17);
    put(13);
    put(15);
    remove(4);
    remove(1);
    remove(3);
    remove(4);
    put(12);
    remove(3);
    remove(12);
    put(5);
    remove(17);
    remove(2);
    remove(5);
    put(18);
    put(0);
    put(15);
    put(18);
    put(14);
    remove(12);
    remove(19);
    put(16);
    put(7);
    put(8);
    remove(3);
    remove(7);
  }

  @Test
  void testRemoveMissingKeyKeepsTreeStable() {
    put(40);
    put(20);
    put(60);
    put(10);
    put(30);
    put(50);
    put(70);

    remove(999);

    assertEquals(7, tree.size());
    assertContains(10, 20, 30, 40, 50, 60, 70);
  }

  @Test
  void testRemoveWithPredecessorReplacement() {
    put(40);
    put(20);
    put(60);
    put(10);
    put(30);
    put(50);
    put(70);
    put(25);
    put(35);

    remove(40);

    assertEquals(8, tree.size());
    assertNotFound(40);
    assertContains(10, 20, 25, 30, 35, 50, 60, 70);
  }

  @Test
  void testRemoveTriggersLeftSideRebalance() {
    put(40);
    put(20);
    put(60);
    put(10);
    put(30);
    put(50);
    put(70);
    put(65);
    put(80);

    remove(10);
    remove(20);
    remove(30);

    assertEquals(6, tree.size());
    assertNotFound(10, 20, 30);
    assertContains(40, 50, 60, 65, 70, 80);
  }

  @Test
  void testRemoveTriggersRightSideRebalance() {
    put(40);
    put(20);
    put(60);
    put(10);
    put(30);
    put(50);
    put(70);
    put(5);
    put(15);

    remove(70);
    remove(60);
    remove(50);

    assertEquals(6, tree.size());
    assertNotFound(50, 60, 70);
    assertContains(5, 10, 15, 20, 30, 40);
  }

  @Test
  void testRemoveLeftSideRebalanceWithTallRightChild() {
    put(50);
    put(20);
    put(70);
    put(10);
    put(30);
    put(60);
    put(80);
    put(65);
    put(90);
    put(85);

    remove(10);
    remove(20);
    remove(30);

    assertEquals(7, tree.size());
    assertNotFound(10, 20, 30);
    assertContains(50, 60, 65, 70, 80, 85, 90);
  }

  @Test
  void testRandomizedSmallShapes() {
    var random = new Random(1);
    for (int size = 0; size <= 8; size++) {
      for (int n = 0; n < 100; n++) {
        var keys = new ArrayList<Long>();
        for (int i = 0; i < size; i++) {
          keys.add((long) random.nextInt());
        }

        var tree = createTree();
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

  @Test
  void testRandomizedInsertThenRemove() {
    var random = new Random(1);
    for (int seed = 0; seed < 100; seed++) {

      int n = random.nextInt(500);
      var keys = new ArrayList<Long>(n);
      for (int i = 0; i < n; i++) {
        keys.add((long) random.nextInt());
      }

      // With this set of keys, try different orders of insert/remove.
      for (int k = 0; k < 50; k++) {
        var tree = createTree();
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
