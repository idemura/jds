package com.github.idemura;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import org.tinylog.Logger;

public class ThreadExceptions {
  static Executor executor =
      Executors.newFixedThreadPool(
          2,
          (runnable) ->
              new Thread(
                  () -> {
                    Logger.info("start");
                    try {
                      runnable.run();
                    } finally {
                      Logger.info("exit thread");
                    }
                  }));

  static void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {
      Logger.info(ex, "InterruptedException");
    }
  }

  static void worker() {
    // for (int i = 0; i < 200; i++) {
    //   final int n = i;
    //   executor.execute(
    //       () -> {
    //         Logger.info("for i={}", n);
    //         throw new RuntimeException("blah");
    //       });
    // }

    var f1 = new CompletableFuture<Long>();
    f1.thenAccept((x) -> Logger.info("f1 x={}", x));
    var f2 = new CompletableFuture<Long>();
    f2.exceptionallyCompose(
        ex -> {
          Logger.info("ex in f2");
          return CompletableFuture.completedFuture(2000L);
        });
    var f3 = new CompletableFuture<Long>();
    f3.thenAccept((x) -> Logger.info("f2 value {}", x));

    // var all = CompletableFuture.allOf(f1, f2, f3);
    // all.thenAccept((x) -> Logger.info("got number {}", x));
    // all.exceptionally(ex -> {
    //   // Logger.info(ex, "exceptionally");
    //   Logger.info("all exceptionally");
    //   return null;
    // });

    var counter = new AtomicLong(0);
    var all = new CompletableFuture<Long>();
    for (var f : List.of(f1, f2, f3)) {
      f.whenCompleteAsync(
          (r, e) -> {
            if (e != null) {
              Logger.info("got ex {}", e.getMessage());
              all.completeExceptionally(new RuntimeException("haha"));
            } else {
              Logger.info("got {}", r);
              if (counter.incrementAndGet() == 3) {
                all.complete(100L);
              }
            }
          },
          executor);
    }

    Logger.info("starting");

    f1.completeAsync(() -> 10L, executor);
    sleep(100);
    // f2.completeExceptionally(new RuntimeException("f2 ex"));
    f2.completeAsync(
        () -> {
          throw new RuntimeException("rtex");
        },
        executor);
    sleep(100);
    f3.completeAsync(
        () -> {
          sleep(5000);
          return 20L;
        },
        executor);
    sleep(200);

    Logger.info("all join {}", all.join());
  }

  public static void main(String[] args) {
    worker();
  }
}
