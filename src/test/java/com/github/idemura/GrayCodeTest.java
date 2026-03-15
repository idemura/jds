package com.github.idemura;

import static com.github.idemura.GrayCode.fromGrayCode;
import static com.github.idemura.GrayCode.toGrayCode;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GrayCodeTest {
  @Test
  void testKnownValues() {
    assertEquals(0b000L, toGrayCode(0b000L));
    assertEquals(0b001L, toGrayCode(0b001L));
    assertEquals(0b011L, toGrayCode(0b010L));
    assertEquals(0b010L, toGrayCode(0b011L));
    assertEquals(0b110L, toGrayCode(0b100L));
    assertEquals(0b111L, toGrayCode(0b101L));
    assertEquals(0b101L, toGrayCode(0b110L));
    assertEquals(0b100L, toGrayCode(0b111L));

    assertEquals(0b000L, fromGrayCode(0b000L));
    assertEquals(0b001L, fromGrayCode(0b001L));
    assertEquals(0b010L, fromGrayCode(0b011L));
    assertEquals(0b011L, fromGrayCode(0b010L));
    assertEquals(0b100L, fromGrayCode(0b110L));
    assertEquals(0b101L, fromGrayCode(0b111L));
    assertEquals(0b110L, fromGrayCode(0b101L));
    assertEquals(0b111L, fromGrayCode(0b100L));
  }

  @Test
  void testConsecutiveValuesDifferByOneBit() {
    for (long i = 1; i < 1_000; i++) {
      long diff = toGrayCode(i) ^ toGrayCode(i - 1);
      assertEquals(1, Long.bitCount(diff), "index=" + i);
    }
  }

  @Test
  void testFromGrayCode() {
    for (long i = 0; i < 1_000; i++) {
      assertEquals(i, fromGrayCode(toGrayCode(i)), "index=" + i);
    }
  }
}
