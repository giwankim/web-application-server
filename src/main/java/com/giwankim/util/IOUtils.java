package com.giwankim.util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
  private IOUtils() {
  }

  public static String readData(BufferedReader bufferedReader, int contentLength) throws IOException {
    char[] buffer = new char[contentLength];
    int charactersRead = bufferedReader.read(buffer, 0, contentLength);
    if (charactersRead != -1) {
      return String.valueOf(buffer, 0, charactersRead);
    }
    return "";
  }
}
