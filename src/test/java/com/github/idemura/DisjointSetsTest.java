package com.github.idemura;

import static com.github.idemura.DisjointSets.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DisjointSetsTest {
  @Test
  void testNewSetCreatesDistinctSingletons() {
    var a = newSet();
    var b = newSet();
    assertSame(a, getRoot(a));
    assertSame(b, getRoot(b));
    assertFalse(isSameSet(a, b));
  }

  @Test
  void testJoinSetsMakesSetsEquivalent() {
    var a = newSet();
    var b = newSet();
    joinSets(a, b);
    var root = getRoot(a);
    assertTrue(isSameSet(a, b));
    assertSame(root, getRoot(b));
    assertTrue(root == a || root == b);
  }

  @Test
  void testJoinSetsChainsPreserveCommonRoot() {
    var a = newSet();
    var b = newSet();
    var c = newSet();
    joinSets(a, b);
    joinSets(b, c);
    var root = getRoot(a);
    assertTrue(isSameSet(a, c));
    assertTrue(isSameSet(b, c));
    assertSame(root, getRoot(b));
    assertSame(root, getRoot(c));
    assertTrue(root == a || root == b || root == c);
  }
}
