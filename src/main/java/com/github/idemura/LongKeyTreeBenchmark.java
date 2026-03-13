package com.github.idemura;

import org.openjdk.jmh.annotations.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
public class LongKeyTreeBenchmark {
  private static final long seed = 1;

  @State(Scope.Thread)
  public static class InsertState {
    @Param({"100000"})
    public int size;

    public ArrayList<Long> keys;

    @Setup(Level.Trial)
    public void setup() {
      var random = new Random(seed);
      keys = makeKeys(random, size);
    }
  }

  @State(Scope.Thread)
  public static class RemoveState {
    @Param({"100000"})
    public int size;

    public ArrayList<Long> shuffledKeys;
    public LongKeyTree<String> aaTree;
    public TreeMap<Long, String> treeMap;

    @Setup(Level.Invocation)
    public void setup() {
      var random = new Random(seed);
      var keys = makeKeys(random, size);
      shuffledKeys = new ArrayList<>(keys);
      Collections.shuffle(shuffledKeys, random);

      aaTree = new LongKeyAaTree<>();
      treeMap = new TreeMap<>();
      for (long key : keys) {
        aaTree.put(key, "dummy");
        treeMap.put(key, "dummy");
      }
    }
  }

  @Benchmark
  public Object aaTreeInsert(InsertState state) {
    var tree = new LongKeyAaTree<String>();
    for (long key : state.keys) {
      tree.put(key, "dummy");
    }
    return tree;
  }

  @Benchmark
  public Object treeMapInsert(InsertState state) {
    var tree = new TreeMap<Long, String>();
    for (long key : state.keys) {
      tree.put(key, "dummy");
    }
    return tree;
  }

  @Benchmark
  public Object aaTreeRemove(RemoveState state) {
    for (long key : state.shuffledKeys) {
      state.aaTree.remove(key);
    }
    return state.aaTree;
  }

  @Benchmark
  public Object treeMapRemove(RemoveState state) {
    for (long key : state.shuffledKeys) {
      state.treeMap.remove(key);
    }
    return state.treeMap;
  }

  private static ArrayList<Long> makeKeys(Random random, int size) {
    var keys = new ArrayList<Long>(size);
    for (int i = 0; i < size; i++) {
      keys.add(random.nextLong());
    }
    return keys;
  }
}
