package com.github.idemura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
public class LongKeyMapBenchmark {
  private static final int SIZE = 100_000;

  static LongKeyMap<String> makeCustomTreeMap() {
    // return new LongKeyAaTree<>();
    // return new LongKeySkipList<>();
    return new LongKeyTreap<>(new Random());
  }

  private static List<Long> makeKeys(RandomGenerator random, int size) {
    var keys = new ArrayList<Long>(size);
    for (int i = 0; i < size; i++) {
      keys.add(random.nextLong());
    }
    return keys;
  }

  @State(Scope.Thread)
  public static class InsertState {
    public List<Long> keys;

    @Setup(Level.Trial)
    public void setup() {
      var random = new Random(0);
      keys = makeKeys(random, SIZE);
    }
  }

  @State(Scope.Thread)
  public static class RemoveState {
    public List<Long> keys;
    public LongKeyMap<String> customTreeMap;
    public TreeMap<Long, String> treeMap;

    @Setup(Level.Invocation)
    public void setup() {
      var random = new Random(0);
      keys = makeKeys(random, SIZE);

      customTreeMap = makeCustomTreeMap();
      treeMap = new TreeMap<>();
      for (long key : keys) {
        customTreeMap.put(key, "dummy");
        treeMap.put(key, "dummy");
      }

      // Shuffle keys for remove order.
      Collections.shuffle(keys, random);
    }
  }

  @Benchmark
  public Object customTreeMapInsert(InsertState state) {
    var tree = makeCustomTreeMap();
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

  // @Benchmark
  // public Object customTreeMapRemove(RemoveState state) {
  //   for (long key : state.keys) {
  //     state.customTreeMap.remove(key);
  //   }
  //   return state.customTreeMap;
  // }
  //
  // @Benchmark
  // public Object treeMapRemove(RemoveState state) {
  //   for (long key : state.keys) {
  //     state.treeMap.remove(key);
  //   }
  //   return state.treeMap;
  // }
}
