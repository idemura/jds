package com.github.idemura;

import java.util.Random;
import java.util.random.RandomGenerator;

public class LongKeySkipList<V> implements LongKeyMap<V> {
  static class Node {
    Long key; // Boxing for fair benchmark.
    Node[] next;
    Object value;

    Node(long key, Object value, int n) {
      this.key = key;
      this.value = value;
      this.next = new Node[n];
    }
  }

  private static final int MAX_HEIGHT = 12;

  private final RandomGenerator random;
  private final Node heads;
  private int size;

  public LongKeySkipList() {
    this(null);
  }

  public LongKeySkipList(RandomGenerator random) {
    this.random = random != null ? random : new Random(1);
    this.heads = new Node(0, null, MAX_HEIGHT);
  }

  @Override
  public void put(long key, V value) {
    var prev = new Node[MAX_HEIGHT];
    var p = heads;

    // Walk from the highest level down once, collecting insertion predecessors.
    for (int h = MAX_HEIGHT - 1; h >= 0; h--) {
      while (p.next[h] != null && p.next[h].key < key) {
        p = p.next[h];
      }
      prev[h] = p;
    }

    // At level 0 we can tell whether this is an update or a true insert.
    var next0 = prev[0].next[0];
    if (next0 != null && next0.key == key) {
      next0.value = value;
      return;
    }

    int h = randomHeight();
    var newNode = new Node(key, value, h);
    for (int i = 0; i < h; i++) {
      newNode.next[i] = prev[i].next[i];
      prev[i].next[i] = newNode;
    }

    size++;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public V get(long key) {
    var node = findNode(heads, key);
    return node == null ? null : (V) node.value;
  }

  @Override
  public void remove(long key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void verify() {
    throw new UnsupportedOperationException();
  }

  private int randomHeight() {
    int h = 1;
    while (h < MAX_HEIGHT && random.nextInt(4) == 0) {
      h++;
    }
    return h;
  }

  static Node findNode(Node heads, long key) {
    int h = MAX_HEIGHT - 1;
    var p = heads;
    while (h >= 0 && p.next[h] == null) {
      h--;
    }
    while (h >= 0) {
      if (p.next[h] != null && p.next[h].key <= key) {
        // If this check happens here, then we don't require 0 to be a special key.
        if (p.next[h].key == key) {
          return p.next[h];
        }
        p = p.next[h];
      } else {
        h--;
      }
    }
    return null;
  }
}
