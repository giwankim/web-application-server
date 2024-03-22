package com.giwankim.http;

import java.util.Objects;

public class HttpRequest {
  private final RequestLine requestLine;

  private final HttpHeaders headers;

  private final RequestParameters requestParameters;

  private HttpRequest(RequestLine requestLine, HttpHeaders headers, RequestParameters parameters) {
    this.requestLine = Objects.requireNonNull(requestLine);
    this.headers = Objects.requireNonNull(headers);
    this.requestParameters = Objects.requireNonNull(parameters);
  }

  static HttpRequest of(RequestLine requestLine, HttpHeaders headers, String body) {
    return new HttpRequest(requestLine, headers, RequestParameters.of(requestLine.getQueryString(), body));
  }

  public HttpMethod getMethod() {
    return requestLine.getMethod();
  }

  public String getPath() {
    return requestLine.getPath();
  }

  public String getHeader(String name) {
    return headers.getHeader(name);
  }

  public String getParameter(String name) {
    return requestParameters.getParameter(name);
  }

  public HttpCookies getCookies() {
    return headers.getCookies();
  }

  @Override
  public String toString() {
    return "HttpRequest{" +
      "requestLine=" + requestLine +
      ", headers=" + headers +
      ", requestParameters=" + requestParameters +
      '}';
  }
}
