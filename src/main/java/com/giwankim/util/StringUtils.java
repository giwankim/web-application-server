package com.giwankim.util;

public class StringUtils {
  private StringUtils() {
  }

  public static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }

  public static boolean isNullOrBlank(String string) {
    return string == null || string.isBlank();
  }
}
