package com.github.idemura;

public class SkipList<V> {
  private static class Node {
    final int key;
    final Node[] next;
    Object value;

    Node(int key, Object value, int n) {
      this.key = key;
      this.value = value;
      this.next = new Node[n];
    }
  }

  private static final int MAX_HEIGHT = 12;

  private int rngState;
  private final Node root;

  SkipList() {
    this(13);
  }

  SkipList(int seed) {
    this.rngState = seed;
    this.root = new Node(0, null, MAX_HEIGHT);
  }

  private int random() {
    return (rngState = (rngState * 1103515245 + 12345) & 0x7fffffff);
  }

  private int randomHeight() {
    int h = 1;
    while (h < MAX_HEIGHT && random() % 4 == 0) {
      h++;
    }
    return h;
  }

  public void put(int key, V value) {
    // Check if we replace the value
    var keyNode = findNode(key);
    if (keyNode != null) {
      keyNode.value = value;
      return;
    }

    // Generate a new node
    int h = randomHeight();
    var newNode = new Node(key, value, h);

    h--; // 0-based index
    var p = root;
    for (;;) {
      boolean levelDown = false;
      if (p.next[h] == null) {
        // We are at the end of the list. Insert and move to the level below.
        p.next[h] = newNode;
        levelDown = true;
      } else {
        if (p.next[h].key < key) {
          p = p.next[h];
        } else {
          if (p.next[h].key == key) {
            // Update value
            p.next[h].value = value;
          } else {
            // Insert here and move to the level below.
            newNode.next[h] = p.next[h];
            p.next[h] = newNode;
          }
          levelDown = true;
        }
      }
      if (levelDown) {
        if (h == 0) {
          break;
        } else {
          h--;
        }
      }
    }
  }

  public V get(int key) {
    var keyNode = findNode(key);
    return keyNode == null ? null : (V) keyNode.value;
  }

  String toDebugString() {
    var sb = new StringBuilder();
    for (int i = 0; i < MAX_HEIGHT; i++) {
      if (root.next[i] == null) {
        break;
      }
      sb.append("L").append(i).append(": ");
      for (var p = root.next[i]; p != null; p = p.next[i]) {
        sb.append(p.key).append("(").append(p.value).append(") ");
      }
      sb.append("\n");
    }
    return sb.toString();
  }

  private Node findNode(int key) {
    int h = MAX_HEIGHT - 1;
    var p = root;
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
