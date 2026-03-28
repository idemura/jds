package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LongKeyAaTreeTest {
  private static void put(LongKeyMap<String> tree, long key) {
    tree.put(key, Long.toString(key));
    tree.verify();
  }

  private static void remove(LongKeyMap<String> tree, long key) {
    tree.remove(key);
    tree.verify();
  }

  @Test
  void testBasic() {
    var tree = new LongKeyAaTree<String>();
    LongKeyMapTestSuite.testEmpty(tree);
    LongKeyMapTestSuite.testSingleKey(tree);
  }

  @Test
  void testSkewSplit() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 50);
    assertEquals(1, tree.size());
    assertEquals("50", tree.get(50));
    // Skew
    put(tree, 40);
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
    // Skew+split
    put(tree, 30);
    assertEquals("30", tree.get(30));
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
  }

  @Test
  void testRemoveRegression() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 0);
    put(tree, 9);
    remove(tree, 15);
    remove(tree, 11);
    put(tree, 19);
    remove(tree, 17);
    put(tree, 13);
    put(tree, 15);
    remove(tree, 4);
    remove(tree, 1);
    remove(tree, 3);
    remove(tree, 4);
    put(tree, 12);
    remove(tree, 3);
    remove(tree, 12);
    put(tree, 5);
    remove(tree, 17);
    remove(tree, 2);
    remove(tree, 5);
    put(tree, 18);
    put(tree, 0);
    put(tree, 15);
    put(tree, 18);
    put(tree, 14);
    remove(tree, 12);
    remove(tree, 19);
    put(tree, 16);
    put(tree, 7);
    put(tree, 8);
    remove(tree, 3);
    remove(tree, 7);
  }

  @Test
  void testRemoveMissingKeyKeepsTreeStable() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 40);
    put(tree, 20);
    put(tree, 60);
    put(tree, 10);
    put(tree, 30);
    put(tree, 50);
    put(tree, 70);

    remove(tree, 999);

    assertEquals(7, tree.size());
    assertEquals("10", tree.get(10));
    assertEquals("20", tree.get(20));
    assertEquals("30", tree.get(30));
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("70", tree.get(70));
  }

  @Test
  void testRemoveWithPredecessorReplacement() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 40);
    put(tree, 20);
    put(tree, 60);
    put(tree, 10);
    put(tree, 30);
    put(tree, 50);
    put(tree, 70);
    put(tree, 25);
    put(tree, 35);

    remove(tree, 40);

    assertEquals(8, tree.size());
    assertNull(tree.get(40));
    assertEquals("10", tree.get(10));
    assertEquals("20", tree.get(20));
    assertEquals("25", tree.get(25));
    assertEquals("30", tree.get(30));
    assertEquals("35", tree.get(35));
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("70", tree.get(70));
  }

  @Test
  void testRemoveTriggersLeftSideRebalance() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 40);
    put(tree, 20);
    put(tree, 60);
    put(tree, 10);
    put(tree, 30);
    put(tree, 50);
    put(tree, 70);
    put(tree, 65);
    put(tree, 80);

    remove(tree, 10);
    remove(tree, 20);
    remove(tree, 30);

    assertEquals(6, tree.size());
    assertNull(tree.get(10));
    assertNull(tree.get(20));
    assertNull(tree.get(30));
    assertEquals("40", tree.get(40));
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("65", tree.get(65));
    assertEquals("70", tree.get(70));
    assertEquals("80", tree.get(80));
  }

  @Test
  void testRemoveTriggersRightSideRebalance() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 40);
    put(tree, 20);
    put(tree, 60);
    put(tree, 10);
    put(tree, 30);
    put(tree, 50);
    put(tree, 70);
    put(tree, 5);
    put(tree, 15);

    remove(tree, 70);
    remove(tree, 60);
    remove(tree, 50);

    assertEquals(6, tree.size());
    assertNull(tree.get(50));
    assertNull(tree.get(60));
    assertNull(tree.get(70));
    assertEquals("5", tree.get(5));
    assertEquals("10", tree.get(10));
    assertEquals("15", tree.get(15));
    assertEquals("20", tree.get(20));
    assertEquals("30", tree.get(30));
    assertEquals("40", tree.get(40));
  }

  @Test
  void testRemoveLeftSideRebalanceWithTallRightChild() {
    var tree = new LongKeyAaTree<String>();
    put(tree, 50);
    put(tree, 20);
    put(tree, 70);
    put(tree, 10);
    put(tree, 30);
    put(tree, 60);
    put(tree, 80);
    put(tree, 65);
    put(tree, 90);
    put(tree, 85);

    remove(tree, 10);
    remove(tree, 20);
    remove(tree, 30);

    assertEquals(7, tree.size());
    assertNull(tree.get(10));
    assertNull(tree.get(20));
    assertNull(tree.get(30));
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("65", tree.get(65));
    assertEquals("70", tree.get(70));
    assertEquals("80", tree.get(80));
    assertEquals("85", tree.get(85));
    assertEquals("90", tree.get(90));
  }

  @Test
  void testRandomizedSmallShapes() {
    LongKeyMapTestSuite.testSmallShapes(new LongKeyAaTree<>());
  }

  @Test
  void testRandomizedInsertThenRemove() {
    LongKeyMapTestSuite.testInsertThenRemove(new LongKeyAaTree<>());
  }
}
