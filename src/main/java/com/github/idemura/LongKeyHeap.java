package com.github.idemura;

import static com.github.idemura.IdChecks.requirePositiveElse;
import static com.github.idemura.IdExceptions.verificationError;

public class LongKeyHeap<T> {
  public record Entry<V>(long key, V value) {}

  private long[] keys;
  private Object[] values;
  private int size;

  public LongKeyHeap() {
    this(8);
  }

  public LongKeyHeap(int capacity) {
    keys = new long[requirePositiveElse(capacity, 8)];
    values = new Object[keys.length];
  }

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  @SuppressWarnings("unchecked")
  public Entry<T> peek() {
    if (size == 0) {
      return null;
    }
    return new Entry(keys[0], (T) values[0]);
  }

  @SuppressWarnings("unchecked")
  public Entry<T> poll() {
    if (size == 0) {
      return null;
    }
    var key = keys[0];
    var value = (T) values[0];
    size--;
    if (size > 0) {
      keys[0] = keys[size];
      values[0] = values[size];
      siftDown(0);
    }
    values[size] = null;
    return new Entry(key, value);
  }

  public void offer(long key, T value) {
    ensureCapacity(size + 1);
    siftUp(size, key, value);
    size++;
  }

  public void verify() {
    for (var i = 0; i < size; i++) {
      var left = leftChild(i);
      var right = rightChild(i);
      if (left < size && keys[i] > keys[left]) {
        throw verificationError("Heap order violated at index=%d", i);
      }
      if (right < size && keys[i] > keys[right]) {
        throw verificationError("Heap order violated at index=%d", i);
      }
    }
  }

  private void ensureCapacity(int minCapacity) {
    if (minCapacity <= keys.length) {
      return;
    }
    var newCapacity = keys.length;
    while (newCapacity < minCapacity) {
      newCapacity *= 2;
    }
    keys = java.util.Arrays.copyOf(keys, newCapacity);
    values = java.util.Arrays.copyOf(values, newCapacity);
  }

  private void siftUp(int index, long key, Object value) {
    while (index > 0) {
      var parent = parent(index);
      if (key >= keys[parent]) {
        break;
      }
      keys[index] = keys[parent];
      values[index] = values[parent];
      index = parent;
    }
    keys[index] = key;
    values[index] = value;
  }

  private void siftDown(int index) {
    var key = keys[index];
    var value = values[index];
    var half = size >>> 1;
    while (index < half) {
      var child = leftChild(index);
      var right = child + 1;
      if (right < size && keys[right] < keys[child]) {
        child = right;
      }
      if (key <= keys[child]) {
        break;
      }
      keys[index] = keys[child];
      values[index] = values[child];
      index = child;
    }
    keys[index] = key;
    values[index] = value;
  }

  private static int parent(int index) {
    return (index - 1) >>> 1;
  }

  private static int leftChild(int index) {
    return (index << 1) + 1;
  }

  private static int rightChild(int index) {
    return (index << 1) + 2;
  }
}
