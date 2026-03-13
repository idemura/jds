package com.github.idemura;

public class LongKeyAaTree<T> {
  private static class Node<T> {
    long key;
    int depth;
    T value;
    Node<T> left;
    Node<T> right;

    Node(long key, T value) {
      this.key = key;
      this.value = value;
      this.depth = 1;
    }
  }

  private Node<T> root;
  private int size;

  public LongKeyAaTree() {}

  public int size() {
    return size;
  }

  public T get(long key) {
    var node = root;
    while (node != null) {
      if (key < node.key) {
        node = node.left;
      } else if (key > node.key) {
        node = node.right;
      } else {
        return node.value;
      }
    }
    return null;
  }

  public void put(long key, T value) {
    // Size is updated exactly at node creation in putRecStep.
    root = putRecStep(root, key, value);
  }

  public void remove(long key) {
    // Size is updated exactly at node creation in putRecStep.
    root = removeRecStep(root, key);
  }

  void verify() {
    int actualSize = verifySearchTreeRec(root, null, null);
    if (actualSize != size) {
      throw newVerificationException("Size mismatch: expected=%d, actual=%d", size, actualSize);
    }
    verifyAaRec(root);
  }

  private Node<T> putRecStep(Node<T> node, long key, T value) {
    if (node == null) {
      size++;
      return new Node<>(key, value);
    }

    if (key < node.key) {
      node.left = putRecStep(node.left, key, value);
    } else if (key > node.key) {
      node.right = putRecStep(node.right, key, value);
    } else {
      node.value = value;
      return node;
    }

    // AA-tree rebalancing after recursive insert:
    // 1) skew: fix left child rule violation,
    // 2) split: fix right-right grand child rule violation.
    //
    // Note on transient states:
    // After inserting into the left subtree, its root may be promoted to this node's depth (d),
    // so we can briefly have node.left.depth == node.depth. In that promotion shape,
    // node.left.right is expected to be depth (d-1), so skew fixes the violation without
    // introducing a new left horizontal link at the rotated-down node.
    return split(skew(node));
  }

  private Node<T> removeRecStep(Node<T> node, long key) {
    if (node == null) {
      // Key not found.
      return null;
    }

    if (key < node.key) {
      node.left = removeRecStep(node.left, key);
      if (getDepth(node.left) < node.depth - 1) {
        // node.right != null here.
        int d = node.depth;
        node.depth--;
        if (getDepth(node.right) == d) {
          return promoteRight(node);
        }
        if (node.right != null && getDepth(node.right.right) == d - 1) {
          node = promoteRight(node);
          node.depth++;
        }
      }
    } else if (key > node.key) {
      node.right = removeRecStep(node.right, key);
      if (getDepth(node.right) < node.depth - 1) {
        node.depth--;
        return promoteLeft(node);
      }
    } else {
      // If node doesn't have left child, delete it. Otherwise, find the rightmost node (rm) in
      // the left subtree and copy key and value in this node. Remove rm.key instead.
      if (node.left == null) {
        size--;
        return node.right;
      } else {
        var t = node.left;
        while (t.right != null) {
          t = t.right;
        }
        node.key = t.key;
        node.value = t.value;
        node.left = removeRecStep(node.left, t.key);
      }
    }
    return node;
  }

  private static <T> int verifySearchTreeRec(
      Node<T> node, Long minKeyExclusive, Long maxKeyExclusive) {
    if (node == null) {
      return 0;
    }
    if (minKeyExclusive != null && node.key <= minKeyExclusive) {
      throw newVerificationException("BST violation at key=%d", node.key);
    }
    if (maxKeyExclusive != null && node.key >= maxKeyExclusive) {
      throw newVerificationException("BST violation at key=%d", node.key);
    }
    int leftCount = verifySearchTreeRec(node.left, minKeyExclusive, node.key);
    int rightCount = verifySearchTreeRec(node.right, node.key, maxKeyExclusive);
    return leftCount + rightCount + 1;
  }

  private static <T> void verifyAaRec(Node<T> node) {
    if (node == null) {
      return;
    }
    if (node.depth < 1) {
      throw newVerificationException("Invalid depth at key=%d", node.key);
    }

    // Left child must be exactly one level lower.
    if (getDepth(node.left) != node.depth - 1) {
      throw newVerificationException("Invalid left depth at key=%d", node.key);
    }

    // Right child must be at same level or one lower.
    int rightDepth = getDepth(node.right);
    if (rightDepth != node.depth && rightDepth != node.depth - 1) {
      throw newVerificationException("Invalid right depth at key=%d", node.key);
    }

    // Right-right child cannot be on the same level as node.
    if (getDepth(node.right == null ? null : node.right.right) >= node.depth) {
      throw newVerificationException("Invalid right-right depth at key=%d", node.key);
    }

    verifyAaRec(node.left);
    verifyAaRec(node.right);
  }

  private static <T> int getDepth(Node<T> node) {
    return node == null ? 0 : node.depth;
  }

  private static IllegalArgumentException newVerificationException(String message, Object... args) {
    return new IllegalArgumentException(message.formatted(args));
  }

  private static <T> Node<T> skew(Node<T> node) {
    if (getDepth(node.left) == node.depth) {
      return promoteLeft(node);
    }
    return node;
  }

  private static <T> Node<T> split(Node<T> node) {
    if (node.right != null && getDepth(node.right.right) == node.depth) {
      node = promoteRight(node);
      node.depth++;
      return node;
    }
    return node;
  }

  private static <T> Node<T> promoteLeft(Node<T> node) {
    var left = node.left;
    node.left = left.right;
    left.right = node;
    return left;
  }

  private static <T> Node<T> promoteRight(Node<T> node) {
    var right = node.right;
    node.right = right.left;
    right.left = node;
    return right;
  }
}
