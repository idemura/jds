package com.github.idemura;

import java.util.function.Consumer;
import org.junit.jupiter.api.Assertions;

public class IdAssertions extends Assertions {
  public static <T> void assertWith(T x, Consumer<T> consumer) {
    consumer.accept(x);
  }
}
