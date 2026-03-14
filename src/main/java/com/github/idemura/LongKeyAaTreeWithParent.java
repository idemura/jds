package com.github.idemura;

public class LongKeyAaTreeWithParent<V> implements LongKeyTree<V> {
  static class Node {
    Long key; // Boxing for fair benchmark.
    int depth;
    Object value;
    Node parent;
    Node left;
    Node right;

    Node(long key, Object value, Node parent) {
      this.key = key;
      this.depth = 1;
      this.value = value;
      this.parent = parent;
    }
  }

  private Node root;
  private int size;

  public LongKeyAaTreeWithParent() {}

  public int size() {
    return size;
  }

  public V get(long key) {
    var node = findNode(key);
    return node == null ? null : (V) node.value;
  }

  public void put(long key, V value) {
    if (root == null) {
      root = new Node(key, value, null);
      size++;
      return;
    }

    var node = root;
    while (true) {
      if (key < node.key) {
        if (node.left == null) {
          node.left = new Node(key, value, node);
          break;
        }
        node = node.left;
      } else if (key > node.key) {
        if (node.right == null) {
          node.right = new Node(key, value, node);
          break;
        }
        node = node.right;
      } else {
        node.value = value;
        return;
      }
    }
    size++;
    rebalanceAfterInsert(node);
  }

  public void remove(long key) {
    // Size is updated exactly at node creation in putRecStep.
    root = removeRecStep(root, key);
    if (root != null) {
      root.parent = null;
    }
  }

  public void verify() {
    int actualSize = countRec(root);
    if (actualSize != size) {
      throw newVerificationException("Size mismatch: expected=%d, actual=%d", size, actualSize);
    }
    verifyParentRec(root, null);
    verifySearchTreeRec(root, null, null);
    verifyAaRec(root);
  }

  private Node findNode(long key) {
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

  private void rebalanceAfterInsert(Node node) {
    int sameCount = 0;
    while (node != null) {
      var parent = node.parent;

      // AA-tree rebalancing after insert:
      // 1) skew: fix left child rule violation,
      // 2) split: fix right-right grand child rule violation.
      int d = node.depth;
      var top = split(skew(node));
      replaceChild(parent, node, top);
      if (parent == null) {
        root = top;
        root.parent = null;
      }
      if (top == node && top.depth == d) {
        if (sameCount++ == 2) {
          return;
        }
      } else {
        sameCount = 0;
      }

      node = top.parent;
    }
  }

  private Node removeRecStep(Node node, long key) {
    if (node == null) {
      // Key not found.
      return null;
    }

    if (key <= node.key) {
      if (key == node.key) {
        // If node doesn't have left child, delete it. Otherwise, find the predecessor and copy
        // the key and the value in this node. Remove predecessor's key instead.
        if (node.left == null) {
          size--;
          return node.right;
        }
        var pred = findPredecessor(node);
        node.key = pred.key;
        node.value = pred.value;
        node.left = removeRecStep(node.left, pred.key);
        if (node.left != null) {
          node.left.parent = node;
        }
      } else {
        node.left = removeRecStep(node.left, key);
        if (node.left != null) {
          node.left.parent = node;
        }
      }
      return rebalanceAfterDeleteLeft(node);
    } else {
      node.right = removeRecStep(node.right, key);
      if (node.right != null) {
        node.right.parent = node;
      }
      return rebalanceAfterDeleteRight(node);
    }
  }

  static Node rebalanceAfterDeleteLeft(Node node) {
    if (getDepth(node.left) < node.depth - 1) {
      // Consider two cases: right and right-right.
      int d = node.depth--;
      if (getDepth(node.right) < d) {
        // We only need to split.
        return split(node);
      } else {
        // Right-right case. Promote node with greater depth up.
        var top = promoteRight(node);
        // Split left child, skew after.
        top.left = split(top.left);
        return skew(top);
      }
    }
    return node;
  }

  static Node rebalanceAfterDeleteRight(Node node) {
    if (getDepth(node.right) < node.depth - 1) {
      node.depth--;
      var top = skew(node);
      if (top.right != null) {
        top.right = skew(top.right);
      }
      return split(top);
    }
    return node;
  }

  static void verifySearchTreeRec(Node node, Long minKeyExclusive, Long maxKeyExclusive) {
    if (node == null) {
      return;
    }
    if (minKeyExclusive != null && node.key <= minKeyExclusive) {
      throw newVerificationException("BST violation at key=%d", node.key);
    }
    if (maxKeyExclusive != null && node.key >= maxKeyExclusive) {
      throw newVerificationException("BST violation at key=%d", node.key);
    }
    verifySearchTreeRec(node.left, minKeyExclusive, node.key);
    verifySearchTreeRec(node.right, node.key, maxKeyExclusive);
  }

  static void verifyAaRec(Node node) {
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

  static int countRec(Node node) {
    if (node == null) {
      return 0;
    }
    return countRec(node.left) + countRec(node.right) + 1;
  }

  static void verifyParentRec(Node node, Node expectedParent) {
    if (node == null) {
      return;
    }
    if (node.parent != expectedParent) {
      throw newVerificationException("Invalid parent at key=%d", node.key);
    }
    verifyParentRec(node.left, node);
    verifyParentRec(node.right, node);
  }

  static int getDepth(Node node) {
    return node == null ? 0 : node.depth;
  }

  static Node findPredecessor(Node node) {
    if (node.left == null) {
      return null;
    }
    node = node.left;
    while (node.right != null) {
      node = node.right;
    }
    return node;
  }

  static IllegalArgumentException newVerificationException(String message, Object... args) {
    return new IllegalArgumentException(message.formatted(args));
  }

  private static void replaceChild(Node parent, Node oldChild, Node newChild) {
    if (parent == null) {
      return;
    }
    if (parent.left == oldChild) {
      parent.left = newChild;
    } else if (parent.right == oldChild) {
      parent.right = newChild;
    } else {
      throw newVerificationException("Parent does not reference child at key=%d", oldChild.key);
    }
    newChild.parent = parent;
  }

  static Node skew(Node node) {
    if (getDepth(node.left) == node.depth) {
      return promoteLeft(node);
    }
    return node;
  }

  static Node split(Node node) {
    if (node.right != null && getDepth(node.right.right) == node.depth) {
      node = promoteRight(node);
      node.depth++;
      return node;
    }
    return node;
  }

  static Node promoteLeft(Node node) {
    var left = node.left;
    left.parent = node.parent;
    node.left = left.right;
    if (node.left != null) {
      node.left.parent = node;
    }
    left.right = node;
    node.parent = left;
    return left;
  }

  static Node promoteRight(Node node) {
    var right = node.right;
    right.parent = node.parent;
    node.right = right.left;
    if (node.right != null) {
      node.right.parent = node;
    }
    right.left = node;
    node.parent = right;
    return right;
  }
}
