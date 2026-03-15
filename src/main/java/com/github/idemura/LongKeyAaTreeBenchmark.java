package com.github.idemura;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
public class LongKeyAaTreeBenchmark {
  private static final int SIZE = 100_000;
  private static final int SEED = 1;

  @State(Scope.Thread)
  public static class InsertState {
    public ArrayList<Long> keys;

    @Setup(Level.Trial)
    public void setup() {
      var random = new Random(SEED);
      keys = makeKeys(random, SIZE);
    }
  }

  @State(Scope.Thread)
  public static class RemoveState {
    public ArrayList<Long> shuffledKeys;
    public LongKeyMap<String> aaTree;
    public TreeMap<Long, String> treeMap;

    @Setup(Level.Invocation)
    public void setup() {
      var random = new Random(SEED);
      var keys = makeKeys(random, SIZE);
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

  // @Benchmark
  // public Object aaTreeRemove(RemoveState state) {
  //   for (long key : state.shuffledKeys) {
  //     state.aaTree.remove(key);
  //   }
  //   return state.aaTree;
  // }
  //
  // @Benchmark
  // public Object treeMapRemove(RemoveState state) {
  //   for (long key : state.shuffledKeys) {
  //     state.treeMap.remove(key);
  //   }
  //   return state.treeMap;
  // }

  private static ArrayList<Long> makeKeys(Random random, int size) {
    var keys = new ArrayList<Long>(size);
    for (int i = 0; i < size; i++) {
      keys.add(random.nextLong());
    }
    return keys;
  }
}
