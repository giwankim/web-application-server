package com.giwankim.http;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookiesTest {
  @Test
  void shouldReturnWhetherContainsCookie() {
    HttpCookies cookies = new HttpCookies(Map.of("JSESSIONID", UUID.randomUUID().toString()));
    assertThat(cookies.containsCookie("JSESSIONID")).isTrue();
    assertThat(cookies.containsCookie("login")).isFalse();
  }
}
