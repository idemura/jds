package com.github.idemura;

public class VerificationException extends RuntimeException {
  public VerificationException(String message, Object... args) {
    super(message.formatted(args));
  }
}
