package com.giwankim.util;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;

class IOUtilsTest {

  @Test
  void shouldReadData() throws IOException {
    String data = "abc123";
    BufferedReader bufferedReader = new BufferedReader(new StringReader(data));

    String actual = IOUtils.readData(bufferedReader, data.length());

    assertThat(actual).isEqualTo(data);
  }

  @Test
  void shouldReadDataWhenContentLengthLongerThanStream() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new StringReader("abc123"));

    String actual = IOUtils.readData(bufferedReader, 10);

    assertThat(actual).isEqualTo("abc123");
  }

  @Test
  void shouldReadEmptyStringWhenStreamIsEmpty() throws IOException {
    BufferedReader bufferedReader = new BufferedReader(new StringReader(""));

    String actual = IOUtils.readData(bufferedReader, 10);

    assertThat(actual).isEmpty();
  }
}