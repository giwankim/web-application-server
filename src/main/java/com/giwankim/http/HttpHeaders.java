package com.giwankim.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class HttpHeaders {
  private static final Logger logger = LoggerFactory.getLogger(HttpHeaders.class);

  private static final String KV_SEPARATOR = ":";

  private static final String HEADER_CONTENT_LENGTH = "Content-Length";

  private static final String HEADER_COOKIE = "Cookie";

  private final Map<String, String> headers;

  public HttpHeaders() {
    this(new HashMap<>());
  }

  public HttpHeaders(Map<String, String> headers) {
    this.headers = Objects.requireNonNull(headers);
  }

  public void add(String header) {
    logger.debug("header : {}", header);
    String[] parts = header.split(KV_SEPARATOR, 2);
    if (parts.length != 2) {
      throw new InvalidHeaderException("Invalid header: " + header);
    }
    headers.put(parts[0].trim(), parts[1].trim());
  }

  public String getHeader(String name) {
    return headers.get(name);
  }

  public int getContentLength() {
    return getIntHeader(HEADER_CONTENT_LENGTH);
  }

  public int getIntHeader(String name) {
    String contentLength = getHeader(name);
    if (contentLength != null) {
      return Integer.parseInt(contentLength);
    }
    return 0;
  }

  public boolean containsHeader(String name) {
    return headers.containsKey(name);
  }

  public HttpCookies getCookies() {
    return HttpCookies.from(getHeader(HEADER_COOKIE));
  }

  public HttpSession getSession() {
    String sessionId = getCookies().getCookie(HttpSessions.SESSION_ID_NAME);
    return HttpSessions.getOrCreateSession(sessionId);
  }

  @Override
  public String toString() {
    return "HttpHeaders{" +
      "headers=" + headers +
      '}';
  }
}
