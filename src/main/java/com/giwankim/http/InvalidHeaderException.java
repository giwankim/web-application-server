package com.giwankim.http;

public class InvalidHeaderException extends RuntimeException {
  public InvalidHeaderException() {
  }

  public InvalidHeaderException(String message) {
    super(message);
  }

  public InvalidHeaderException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidHeaderException(Throwable cause) {
    super(cause);
  }
}
