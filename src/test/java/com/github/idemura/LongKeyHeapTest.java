package com.github.idemura;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;
import org.junit.jupiter.api.Test;

class LongKeyHeapTest {
  @Test
  void testEmpty() {
    var heap = new LongKeyHeap<String>();
    assertTrue(heap.isEmpty());
    assertEquals(0, heap.size());
    assertNull(heap.peek());
    assertNull(heap.poll());
    heap.verify();
  }

  @Test
  void testSingleElement() {
    var heap = new LongKeyHeap<String>();
    heap.offer(42, "forty-two");
    heap.verify();
    assertEquals(1, heap.size());
    assertEquals(new LongKeyHeap.Entry<>(42L, "forty-two"), heap.peek());
    assertEquals(new LongKeyHeap.Entry<>(42L, "forty-two"), heap.poll());
    assertTrue(heap.isEmpty());
    heap.verify();
  }

  @Test
  void testPollsInKeyOrder() {
    var heap = new LongKeyHeap<String>();
    heap.offer(30, "30");
    heap.offer(10, "10");
    heap.offer(20, "20");
    heap.verify();

    assertEquals(new LongKeyHeap.Entry<>(10L, "10"), heap.poll());
    heap.verify();
    assertEquals(new LongKeyHeap.Entry<>(20L, "20"), heap.poll());
    heap.verify();
    assertEquals(new LongKeyHeap.Entry<>(30L, "30"), heap.poll());
    heap.verify();
    assertTrue(heap.isEmpty());
  }

  @Test
  void testPeekDoesNotRemove() {
    var heap = new LongKeyHeap<String>();
    heap.offer(5, "five");
    heap.offer(1, "one");
    assertEquals(new LongKeyHeap.Entry<>(1L, "one"), heap.peek());
    assertEquals(2, heap.size());
    assertEquals(new LongKeyHeap.Entry<>(1L, "one"), heap.peek());
    heap.verify();
  }

  @Test
  void testDuplicateKeysPreserveValues() {
    var heap = new LongKeyHeap<String>();
    heap.offer(7, "first");
    heap.offer(7, "second");
    heap.offer(7, "third");
    heap.verify();

    var seen = new ArrayList<String>();
    while (!heap.isEmpty()) {
      seen.add(heap.poll().value());
    }
    assertEquals(3, seen.size());
    assertTrue(seen.contains("first"));
    assertTrue(seen.contains("second"));
    assertTrue(seen.contains("third"));
  }

  @Test
  void testRandomizedOfferPoll() {
    var random = new Random(0);
    var heap = new LongKeyHeap<Integer>();
    var keys = new ArrayList<Long>();
    for (var i = 0; i < 200; i++) {
      var key = (long) random.nextInt(1_000);
      keys.add(key);
      heap.offer(key, (int) key);
    }
    heap.verify();

    keys.sort(Long::compareTo);
    for (var key : keys) {
      assertEquals(key, heap.poll().key());
      heap.verify();
    }
    assertTrue(heap.isEmpty());
  }

  @Test
  void testInterleavedOfferPoll() {
    var random = new Random(1);
    var heap = new LongKeyHeap<Long>();
    var expected = new ArrayList<Long>();

    for (var step = 0; step < 500; step++) {
      if (heap.isEmpty() || random.nextBoolean()) {
        var key = (long) random.nextInt(100);
        heap.offer(key, key);
        expected.add(key);
        expected.sort(Long::compareTo);
      } else {
        assertEquals(expected.removeFirst(), heap.poll().key());
      }
      heap.verify();
    }

    expected.sort(Long::compareTo);
    for (var key : expected) {
      assertEquals(key, heap.poll().key());
    }
    heap.verify();
  }
}
