package com.giwankim.http;

import com.giwankim.util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class HttpCookies {
  private final Map<String, String> cookies;

  HttpCookies(Map<String, String> cookies) {
    this.cookies = Objects.requireNonNull(cookies);
  }

  public static HttpCookies from(String values) {
    return new HttpCookies(HttpRequestUtils.parseCookies(values));
  }

  public Map<String, String> getCookies() {
    return Collections.unmodifiableMap(cookies);
  }

  public String getCookie(String name) {
    return cookies.get(name);
  }

  public boolean containsCookie(String name) {
    return cookies.containsKey(name);
  }

  @Override
  public String toString() {
    return "HttpCookies{" +
      "cookies=" + cookies +
      '}';
  }
}
