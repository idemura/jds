package com.github.idemura;

import static com.github.idemura.LongKeyMapRandomizedTests.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LongKeyAaTreeTest {
  private final LongKeyMap<String> tree = createTree();

  private static LongKeyMap<String> createTree() {
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
    testSmallShapes(createTree());
  }

  @Test
  void testRandomizedInsertThenRemove() {
    testInsertThenRemove(createTree());
  }
}
