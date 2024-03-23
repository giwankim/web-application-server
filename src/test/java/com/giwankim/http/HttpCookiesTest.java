package com.giwankim.http;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class HttpCookiesTest {
  @Test
  void shouldReturnWhetherCookie() {
    HttpCookies cookies = new HttpCookies(Map.of("JSESSIONID", "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"));
    assertThat(cookies.containsCookie("JSESSIONID")).isTrue();
    assertThat(cookies.containsCookie("login")).isFalse();
  }
}
