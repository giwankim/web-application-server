package com.giwankim.http;

import com.giwankim.util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

class HttpCookies {
  private final Map<String, String> cookies;

  private HttpCookies() {
    this(new HashMap<>());
  }

  private HttpCookies(Map<String, String> cookies) {
    this.cookies = cookies;
  }

  static HttpCookies from(String values) {
    return new HttpCookies(HttpRequestUtils.parseCookies(values));
  }

  Map<String, String> getCookies() {
    return cookies;
  }

  String getCookie(String name) {
    return cookies.get(name);
  }
}
