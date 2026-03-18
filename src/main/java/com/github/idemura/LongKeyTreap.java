package com.github.idemura;

import static com.github.idemura.IdExceptions.verificationError;

import java.util.random.RandomGenerator;

public class LongKeyTreap<V> implements LongKeyMap<V> {
  static class Node {
    Long key; // Boxing for fair benchmark.
    int rank;
    Object value;
    Node left;
    Node right;

    Node(long key, int rank, Object value) {
      this.key = key;
      this.rank = rank;
      this.value = value;
    }

    @Override
    public String toString() {
      return "Node(key=%s rank=%d %s)".formatted(key, rank, value);
    }
  }

  static class SplitResult {
    Node left;
    Node right;
  }

  private final RandomGenerator random;
  private Node root;
  private int size;

  public LongKeyTreap(RandomGenerator random) {
    this.random = random;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public V get(long key) {
    var node = findNode(root, key);
    return node == null ? null : (V) node.value;
  }

  @Override
  public void put(long key, V value) {
    var keyNode = findNode(root, key);
    if (keyNode != null) {
      keyNode.value = value;
      return;
    }
    var s = split(root, key);
    var n = new Node(key, random.nextInt(), value);
    root = merge(merge(s.left, n), s.right);
    size++;
  }

  @Override
  public void remove(long key) {
    var keyNode = findNode(root, key);
    if (keyNode == null) {
      return;
    }
    var s = split(root, key);
    if (key == Long.MIN_VALUE) {
      // Removing minimal possible key. When we split by the minimal key int tree, we get
      // left side of the split containing just one that node.
      root = s.right;
    } else {
      root = merge(split(s.left, key - 1).left, s.right);
    }
    size--;
  }

  @Override
  public void verify() {
    int actualSize = countRec(root);
    if (actualSize != size) {
      throw verificationError("Size mismatch: expected=%d, actual=%d", size, actualSize);
    }
    verifySearchTreeRec(root, null, null);
    verifyMaxHeapRec(root);
  }

  static int countRec(Node node) {
    if (node == null) {
      return 0;
    }
    return countRec(node.left) + countRec(node.right) + 1;
  }

  static void verifySearchTreeRec(Node node, Long minKeyExclusive, Long maxKeyExclusive) {
    if (node == null) {
      return;
    }
    if (minKeyExclusive != null && node.key <= minKeyExclusive) {
      throw verificationError("BST violation");
    }
    if (maxKeyExclusive != null && node.key >= maxKeyExclusive) {
      throw verificationError("BST violation");
    }
    verifySearchTreeRec(node.left, minKeyExclusive, node.key);
    verifySearchTreeRec(node.right, node.key, maxKeyExclusive);
  }

  static void verifyMaxHeapRec(Node node) {
    if (node == null) {
      return;
    }
    if (node.left != null) {
      if (node.left.rank > node.rank) {
        throw verificationError("Heap order violated");
      }
      verifyMaxHeapRec(node.left);
    }
    if (node.right != null) {
      if (node.right.rank > node.rank) {
        throw verificationError("Heap order violated");
      }
      verifyMaxHeapRec(node.right);
    }
  }

  static Node findNode(Node root, long key) {
    var node = root;
    while (node != null) {
      if (key < node.key) {
        node = node.left;
      } else if (key > node.key) {
        node = node.right;
      } else {
        return node;
      }
    }
    return null;
  }

  static SplitResult split(Node node, long key) {
    if (node == null) {
      return new SplitResult();
    }
    if (key < node.key) {
      var s = split(node.left, key);
      node.left = s.right;
      s.right = node;
      return s;
    } else {
      var s = split(node.right, key);
      node.right = s.left;
      s.left = node;
      return s;
    }
  }

  static Node merge(Node left, Node right) {
    if (left == null) {
      return right;
    }
    if (right == null) {
      return left;
    }
    if (left.rank >= right.rank) {
      left.right = merge(left.right, right);
      return left;
    } else {
      right.left = merge(right.left, left);
      return right;
    }
  }
}
