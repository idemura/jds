package com.github.idemura;

import static com.github.idemura.IdExceptions.verificationError;

import java.util.random.RandomGenerator;

public class LongKeyTreap<V> implements LongKeyMap<V> {
  static class Node {
    Long key; // Boxing for fair benchmark.
    int rank; // Max heap by rank.
    Object value;
    Node parent;
    Node left;
    Node right;

    Node(long key, int rank, Object value, Node parent) {
      this.key = key;
      this.rank = rank;
      this.value = value;
      this.parent = parent;
    }

    @Override
    public String toString() {
      return "Node(key=%s rank=%d)".formatted(key, rank);
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

  @SuppressWarnings("unchecked")
  @Override
  public V get(long key) {
    var node = findNode(root, key);
    return node == null ? null : (V) node.value;
  }

  @Override
  public void put(long key, V value) {
    if (root == null) {
      root = new Node(key, random.nextInt(), value, root);
      size = 1;
      return;
    }
    boolean left = true;
    var node = root;
    var last = node.parent; // Null
    while (node != null) {
      last = node;
      if (key < node.key) {
        node = node.left;
        left = true;
      } else if (key > node.key) {
        node = node.right;
        left = false;
      } else {
        node.value = value;
        return;
      }
    }
    size++;
    if (left) {
      node = last.left = new Node(key, random.nextInt(), value, last);
    } else {
      node = last.right = new Node(key, random.nextInt(), value, last);
    }
    // Fix heap order using rotations.
    while (node.parent != null) {
      if (node.rank > node.parent.rank) {
        promoteUp(node);
      } else {
        // We recovered heap property and didn't reach the root.
        return;
      }
    }
    // Update the root.
    root = node;
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
    if (root != null) {
      root.parent = null;
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
    verifyParent(root, null);
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

  static void verifyParent(Node node, Node parent) {
    if (node == null) {
      return;
    }
    if (node.parent != parent) {
      throw verificationError("Parent violated");
    }
    verifyParent(node.left, node);
    verifyParent(node.right, node);
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
      if (s.right != null) {
        s.right.parent = node;
      }
      s.right = node;
      node.parent = null;
      return s;
    } else {
      var s = split(node.right, key);
      node.right = s.left;
      if (s.left != null) {
        s.left.parent = node;
      }
      s.left = node;
      node.parent = null;
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
      if (left.right != null) {
        left.right.parent = left;
      }
      return left;
    } else {
      right.left = merge(left, right.left);
      if (right.left != null) {
        right.left.parent = right;
      }
      return right;
    }
  }

  static void promoteUp(Node node) {
    var parent = node.parent;
    if (node == parent.left) {
      parent.left = node.right;
      if (node.right != null) {
        node.right.parent = parent;
      }
      node.right = parent;
    } else {
      parent.right = node.left;
      if (node.left != null) {
        node.left.parent = parent;
      }
      node.left = parent;
    }
    var pp = parent.parent;
    parent.parent = node;
    if (pp != null) {
      if (parent == pp.left) {
        pp.left = node;
      } else {
        pp.right = node;
      }
    }
    node.parent = pp;
  }
}
