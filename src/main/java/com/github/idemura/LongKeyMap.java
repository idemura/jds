package com.github.idemura;

public interface LongKeyMap<T> {
  int size();

  T get(long key);

  void put(long key, T value);

  void remove(long key);

  void verify();
}
