package com.github.idemura;

import org.openjdk.jmh.annotations.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
public class LongKeySkipListBenchmark {
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

  @Benchmark
  public Object skipListInsert(InsertState state) {
    var skipList = new LongKeySkipList<String>();
    for (long key : state.keys) {
      skipList.put(key, "dummy");
    }
    return skipList;
  }

  @Benchmark
  public Object skipList2Insert(InsertState state) {
    var skipList = new LongKeySkipList2<String>();
    for (long key : state.keys) {
      skipList.put(key, "dummy");
    }
    return skipList;
  }

  private static ArrayList<Long> makeKeys(Random random, int size) {
    var keys = new ArrayList<Long>(size);
    for (int i = 0; i < size; i++) {
      keys.add(random.nextLong());
    }
    return keys;
  }
}
