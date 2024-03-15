package com.giwankim.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RequestLine {
  private static final Logger logger = LoggerFactory.getLogger(RequestLine.class);

  private final HttpMethod method;

  private final String path;

  private final String queryString;

  private RequestLine(HttpMethod method, String path, String queryString) {
    this.method = method;
    this.path = path;
    this.queryString = queryString;
  }

  public static RequestLine from(String requestLine) {
    logger.debug("request line : {}", requestLine);
    String[] parts = requestLine.split("\\s+", 3);
    if (parts.length != 3) {
      throw new IllegalArgumentException("Invalid request line: " + requestLine);
    }
    HttpMethod method = HttpMethod.valueOf(parts[0]);
    String url = parts[1];
    String[] pathAndQuery = url.split("\\?", 2);
    if (pathAndQuery.length == 2) {
      return new RequestLine(method, pathAndQuery[0], pathAndQuery[1]);
    }
    return new RequestLine(method, pathAndQuery[0], "");
  }

  public HttpMethod getMethod() {
    return method;
  }

  public String getPath() {
    return path;
  }

  public String getQueryString() {
    return queryString;
  }

  @Override
  public String toString() {
    return String.format("RequestLine{method=%s, path='%s', queryString='%s'}", method, path, queryString);
  }
}
