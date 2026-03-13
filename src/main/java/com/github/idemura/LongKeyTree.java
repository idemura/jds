package com.github.idemura;

interface LongKeyTree<T> {
  int size();

  T get(long key);

  void put(long key, T value);

  void remove(long key);

  void verify();
}
