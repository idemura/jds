package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DivisorsTest {
  @Test
  void testGcd() {
    assertEquals(1, Divisors.gcd(17, 13));
    assertEquals(6, Divisors.gcd(54, 24));
    assertEquals(6, Divisors.gcd(24, 54));
    assertEquals(1, Divisors.gcd(1, 5));
    assertEquals(1, Divisors.gcd(5, 1));
    assertEquals(4, Divisors.gcd(12, 4));
    assertEquals(27, Divisors.gcd(81, 27));
  }
}
