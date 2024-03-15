package com.giwankim.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class RequestLineTest {

  @DisplayName("should parse request line from string")
  @ParameterizedTest(name = "{index}: parsing {0}")
  @MethodSource("provideRequestLineStrings")
  void shouldParseFromString(String input, HttpMethod method, String path, String queryString) {
    RequestLine requestLine = RequestLine.from(input);
    assertThat(requestLine.getMethod()).isEqualTo(method);
    assertThat(requestLine.getPath()).isEqualTo(path);
    assertThat(requestLine.getQueryString()).isEqualTo(queryString);
  }

  private static Stream<Arguments> provideRequestLineStrings() {
    return Stream.of(
      Arguments.of(
        "GET /index.html HTTP/1.1",
        HttpMethod.GET,
        "/index.html",
        ""
      ),
      Arguments.of(
        "POST /user/form.html HTTP/1.1",
        HttpMethod.POST,
        "/user/form.html",
        ""
      ),
      Arguments.of(
        "GET /user/create?userId=testId&password=password123 HTTP/1.1",
        HttpMethod.GET,
        "/user/create",
        "userId=testId&password=password123"
      )
    );
  }

  @DisplayName("should throw exception when parsing invalid request line")
  @ParameterizedTest
  @ValueSource(strings = {"GET /index.html", "INVALID /index.html HTTP/1.1"})
  void shouldThrowExceptionWhenRequestLineIsInvalid(String line) {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> RequestLine.from(line));
  }
}
