package com.github.idemura;

public class DisjointSets {
  public static class Node {
    private Node parent;
  }

  public static Node newSet() {
    return new Node();
  }

  public static Node getRoot(Node node) {
    // Find root and compress path on the next step.
    var root = node;
    while (root.parent != null) {
      root = root.parent;
    }
    while (node.parent != null) {
      var p = node.parent;
      node.parent = root;
      node = p;
    }
    return root;
  }

  public static boolean isSameSet(Node a, Node b) {
    return getRoot(a) == getRoot(b);
  }

  public static void joinSets(Node a, Node largerSet) {
    a.parent = largerSet;
  }
}
