package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GrayCodeTest {
  private final GrayCode grayCode = new GrayCode();

  @Test
  void testKnownValues() {
    assertEquals(0b000L, grayCode.getGrayCode(0b000L));
    assertEquals(0b001L, grayCode.getGrayCode(0b001L));
    assertEquals(0b011L, grayCode.getGrayCode(0b010L));
    assertEquals(0b010L, grayCode.getGrayCode(0b011L));
    assertEquals(0b110L, grayCode.getGrayCode(0b100L));
    assertEquals(0b111L, grayCode.getGrayCode(0b101L));
    assertEquals(0b101L, grayCode.getGrayCode(0b110L));
    assertEquals(0b100L, grayCode.getGrayCode(0b111L));
  }

  @Test
  void testConsecutiveValuesDifferByOneBit() {
    for (long i = 1; i < 1_000; i++) {
      long diff = grayCode.getGrayCode(i) ^ grayCode.getGrayCode(i - 1);
      assertEquals(1, Long.bitCount(diff), "index=" + i);
    }
  }
}
