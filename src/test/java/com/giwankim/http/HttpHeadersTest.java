package com.giwankim.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class HttpHeadersTest {

  HttpHeaders headers;

  @BeforeEach
  void setUp() {
    headers = new HttpHeaders();
  }

  @Test
  void shouldAddHeader() {
    headers.add("Connection: keep-alive");
    assertThat(headers.getHeader("Connection")).isEqualTo("keep-alive");
  }

  @Test
  void shouldThrowExceptionWhenHeaderIsInvalid() {
    assertThatExceptionOfType(InvalidHeaderException.class)
      .isThrownBy(() -> headers.add("abc123"));
    assertThatExceptionOfType(InvalidHeaderException.class)
      .isThrownBy(() -> headers.add(""));
  }

  @Test
  void shouldReturnWhetherContainsHeader() {
    HttpHeaders headers = new HttpHeaders(Map.of("Connection", "keep-alive"));
    assertThat(headers.containsHeader("Host")).isFalse();
    assertThat(headers.containsHeader("Connection")).isTrue();
  }
}