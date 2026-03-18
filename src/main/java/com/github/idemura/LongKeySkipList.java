package com.github.idemura;

import static com.github.idemura.IdChecks.requirePositiveElse;
import static java.util.Objects.requireNonNullElseGet;

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

  private static final int DEFAULT_MAX_HEIGHT = 12;

  private final Node[] heads;
  private final int maxHeight;
  private final RandomGenerator random;
  private int size;

  public LongKeySkipList() {
    this(0, null);
  }

  public LongKeySkipList(int maxHeight, RandomGenerator random) {
    this.maxHeight = requirePositiveElse(maxHeight, DEFAULT_MAX_HEIGHT);
    this.random = requireNonNullElseGet(random, () -> new Random(1));
    this.heads = new Node[this.maxHeight];
  }

  @Override
  public void put(long key, V value) {
    var prev = new Node[maxHeight][];
    var p = heads;
    // Walk from the highest level down once, collecting insertion predecessors.
    for (int h = maxHeight - 1; h >= 0; h--) {
      while (p[h] != null && p[h].key < key) {
        p = p[h].next;
      }
      prev[h] = p;
    }
    // At level 0 we can tell whether this is an update or a true insert.
    var next0 = prev[0][0];
    if (next0 != null && next0.key == key) {
      next0.value = value;
      return;
    }
    int h = randomHeight();
    var newNode = new Node(key, value, h);
    for (int i = 0; i < h; i++) {
      newNode.next[i] = prev[i][i];
      prev[i][i] = newNode;
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
  public void verify() {}

  private int randomHeight() {
    int h = 1;
    while (h < maxHeight && random.nextInt(4) == 0) {
      h++;
    }
    return h;
  }

  static Node findNode(Node[] heads, long key) {
    int h = heads.length - 1;
    var p = heads;
    while (h >= 0 && p[h] == null) {
      h--;
    }
    while (h >= 0) {
      if (p[h] != null && p[h].key <= key) {
        // If this check happens here, then we don't require 0 to be a special key.
        if (p[h].key == key) {
          return p[h];
        }
        p = p[h].next;
      } else {
        h--;
      }
    }
    return null;
  }
}
