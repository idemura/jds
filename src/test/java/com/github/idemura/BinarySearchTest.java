package com.github.idemura;

import static com.github.idemura.BinarySearch.findLeft;
import static com.github.idemura.BinarySearch.findRight;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.tinylog.Logger;

class BinarySearchTest {
  @Test
  void testNormal() {
    var a = new int[] {1, 3, 4, 4, 4, 5, 8, 8};

    assertEquals(0, findLeft(a, 0));
    assertEquals(0, findLeft(a, 1));
    assertEquals(1, findLeft(a, 2));
    assertEquals(1, findLeft(a, 3));
    assertEquals(2, findLeft(a, 4));
    assertEquals(5, findLeft(a, 5));
    assertEquals(6, findLeft(a, 6));
    assertEquals(6, findLeft(a, 7));
    assertEquals(6, findLeft(a, 8));
    assertEquals(8, findLeft(a, 9));

    Logger.info("{}", Arrays.toString(a));

    assertEquals(0, findRight(a, 0));
    assertEquals(1, findRight(a, 1));
    assertEquals(1, findRight(a, 2));
    assertEquals(2, findRight(a, 3));
    assertEquals(5, findRight(a, 4));
    assertEquals(6, findRight(a, 5));
    assertEquals(6, findRight(a, 6));
    assertEquals(6, findRight(a, 7));
    assertEquals(8, findRight(a, 8));
    assertEquals(8, findRight(a, 9));
  }

  @Test
  void testEmpty() {
    var a = new int[] {};
    assertEquals(0, findLeft(a, 0));
    assertEquals(0, findLeft(a, 0));
    assertEquals(0, findRight(a, 0));
    assertEquals(0, findRight(a, 0));
  }
}
