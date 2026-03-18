package com.github.idemura;

public class IdExceptions {
  public static IllegalStateException verificationError(String message, Object... args) {
    return new IllegalStateException(message.formatted(args));
  }
}
