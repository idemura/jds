package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Random;
import java.util.random.RandomGenerator;
import org.junit.jupiter.api.Test;

class LongKeyTreapTest {
  @Test
  void testBasic() {
    LongKeyMapTestSuite.testEmpty(new LongKeyTreap<>(new Random(0)));
    LongKeyMapTestSuite.testSingleKey(new LongKeyTreap<>(new Random(0)));
  }

  @Test
  void test2Nodes1() {
    var random = mock(RandomGenerator.class);
    doReturn(10, 5).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(50, "50");
    tree.put(70, "70");
    tree.verify();
    assertEquals(2, tree.size());
    assertEquals("50", tree.get(50));
    assertEquals("70", tree.get(70));
    assertNull(tree.get(40));
  }

  @Test
  void test2Nodes2() {
    var random = mock(RandomGenerator.class);
    doReturn(5, 10).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(50, "50");
    tree.put(70, "70");
    tree.verify();
    assertEquals(2, tree.size());
    assertEquals("50", tree.get(50));
    assertEquals("70", tree.get(70));
    assertNull(tree.get(40));
  }

  @Test
  void test3Nodes1() {
    var random = mock(RandomGenerator.class);
    doReturn(10, 5, 7).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(50, "50");
    tree.put(70, "70");
    tree.put(60, "60");
    tree.verify();
    assertEquals(3, tree.size());
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("70", tree.get(70));
  }

  @Test
  void test3Nodes2() {
    var random = mock(RandomGenerator.class);
    doReturn(10, 5, 12).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(50, "50");
    tree.put(70, "70");
    tree.put(60, "60");
    tree.verify();
    assertEquals(3, tree.size());
    assertEquals("50", tree.get(50));
    assertEquals("60", tree.get(60));
    assertEquals("70", tree.get(70));
  }

  @Test
  void test3Nodes2UpdateValue() {
    var random = mock(RandomGenerator.class);
    doReturn(10, 5, 12, 100).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(50, "50");
    tree.put(70, "70");
    tree.put(60, "60");
    tree.put(60, "sixty");
    tree.verify();
    assertEquals(3, tree.size());
    assertEquals("50", tree.get(50));
    assertEquals("sixty", tree.get(60));
    assertEquals("70", tree.get(70));
  }

  @Test
  void testLeftLeaning() {
    var random = mock(RandomGenerator.class);
    doReturn(3, 2, 1).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(3, "3");
    tree.put(2, "2");
    tree.put(1, "1");
    tree.verify();
    assertEquals(3, tree.size());
    assertEquals("1", tree.get(1));
    assertEquals("2", tree.get(2));
    assertEquals("3", tree.get(3));
  }

  @Test
  void testRightLeaning() {
    var random = mock(RandomGenerator.class);
    doReturn(1, 2, 3).when(random).nextInt();

    var tree = new LongKeyTreap<String>(random);
    tree.put(1, "1");
    tree.put(2, "2");
    tree.put(3, "3");
    tree.verify();
    assertEquals(3, tree.size());
    assertEquals("1", tree.get(1));
    assertEquals("2", tree.get(2));
    assertEquals("3", tree.get(3));
  }

  @Test
  void testRandomizedSmallShapes() {
    LongKeyMapTestSuite.testSmallShapes(new LongKeyTreap<>(new Random(0)));
  }

  @Test
  void testRandomizedInsertThenRemove() {
    LongKeyMapTestSuite.testInsertThenRemove(new LongKeyTreap<>(new Random(0)));
  }
}
