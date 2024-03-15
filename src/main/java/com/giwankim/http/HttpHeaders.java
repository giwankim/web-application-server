package com.giwankim.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class HttpHeaders {
  private static final Logger logger = LoggerFactory.getLogger(HttpHeaders.class);

  private static final String KV_SEPARATOR = ":";

  private static final String CONTENT_LENGTH = "Content-Length";

  private static final String COOKIE = "Cookie";

  private final Map<String, String> headers = new HashMap<>();

  void add(String header) {
    logger.debug("header : {}", header);
    String[] parts = header.split(KV_SEPARATOR, 2);
    if (parts.length != 2) {
      throw new InvalidHeaderException("Invalid header: " + header);
    }
    headers.put(parts[0].trim(), parts[1].trim());
  }

  String getHeader(String name) {
    return headers.get(name);
  }

  int getContentLength() {
    String contentLength = getHeader(CONTENT_LENGTH);
    if (contentLength != null) {
      return Integer.parseInt(contentLength);
    }
    return 0;
  }

  HttpCookies getCookies() {
    return HttpCookies.from(getHeader(COOKIE));
  }

  @Override
  public String toString() {
    return "HttpHeaders{" +
      "headers=" + headers +
      '}';
  }
}
