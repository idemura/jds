package com.github.idemura;

import static com.github.idemura.Divisors.*;
import static com.github.idemura.IdAssertions.*;

import org.junit.jupiter.api.Test;

class DivisorsTest {
  @Test
  void testGcd() {
    assertEquals(1, gcd(17, 13));
    assertEquals(6, gcd(54, 24));
    assertEquals(6, gcd(24, 54));
    assertEquals(1, gcd(1, 5));
    assertEquals(1, gcd(5, 1));
    assertEquals(4, gcd(12, 4));
    assertEquals(27, gcd(81, 27));
  }

  @Test
  void testExtendedGcd() {
    assertWith(
        extendedGcd(17, 13),
        res -> {
          assertEquals(1, res.gcd);
          assertEquals(res.gcd, 17 * res.ka + 13 * res.kb);
        });
    assertWith(
        extendedGcd(54, 24),
        res -> {
          assertEquals(6, res.gcd);
          assertEquals(res.gcd, 54 * res.ka + 24 * res.kb);
        });
    assertWith(
        extendedGcd(24, 54),
        res -> {
          assertEquals(6, res.gcd);
          assertEquals(res.gcd, 24 * res.ka + 54 * res.kb);
        });
    assertWith(
        extendedGcd(1, 5),
        res -> {
          assertEquals(1, res.gcd);
          assertEquals(res.gcd, 1 * res.ka + 5 * res.kb);
        });
    assertWith(
        extendedGcd(5, 1),
        res -> {
          assertEquals(1, res.gcd);
          assertEquals(res.gcd, 5 * res.ka + 1 * res.kb);
        });
    assertWith(
        extendedGcd(12, 4),
        res -> {
          assertEquals(4, res.gcd);
          assertEquals(res.gcd, 12 * res.ka + 4 * res.kb);
        });
    assertWith(
        extendedGcd(81, 27),
        res -> {
          assertEquals(27, res.gcd);
          assertEquals(res.gcd, 81 * res.ka + 27 * res.kb);
        });
  }
}
