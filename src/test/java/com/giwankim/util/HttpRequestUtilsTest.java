package com.giwankim.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestUtilsTest {

  @Test
  void shouldParseQueryString() {
    Map<String, String> expected = Map.ofEntries(
      Map.entry("userId", "testId"),
      Map.entry("password", "password1")
    );

    Map<String, String> actual = HttpRequestUtils.parseQueryString("userId=testId&password=password1");

    assertThat(actual).isEqualTo(expected);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldReturnEmptyMapWhenQueryStringIsEmptyOrNull(String input) {
    assertThat(HttpRequestUtils.parseQueryString(input)).isEmpty();
  }

  @Test
  void shouldParseQueryStringWithInvalidKeyValuePair() {
    Map<String, String> params = HttpRequestUtils.parseQueryString("userId=testId&password");
    assertThat(params.get("password")).isNull();
  }

  @Test
  void shouldParseQueryStringWithEmptyValue() {
    Map<String, String> params = HttpRequestUtils.parseQueryString("userId=testId&password=");
    assertThat(params.get("password")).isEmpty();
  }

  @Test
  void shouldParseCookies() {
    Map<String, String> expected = Map.ofEntries(
      Map.entry("login", "true"),
      Map.entry("JSESSIONID", "1234")
    );

    Map<String, String> actual = HttpRequestUtils.parseCookies("login=true; JSESSIONID=1234");

    assertThat(actual).isEqualTo(expected);
  }
}