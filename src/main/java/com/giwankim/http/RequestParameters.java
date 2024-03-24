package com.giwankim.http;

import com.giwankim.util.HttpRequestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class RequestParameters {
  private final Map<String, String> parameters;

  private RequestParameters() {
    this(new HashMap<>());
  }

  private RequestParameters(Map<String, String> parameters) {
    this.parameters = Objects.requireNonNull(parameters);
  }

  static RequestParameters of(String queryString, String body) {
    RequestParameters params = new RequestParameters();
    params.parseAddParameters(queryString);
    params.parseAddParameters(body);
    return params;
  }

  Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  String getParameter(String name) {
    return parameters.get(name);
  }

  private void parseAddParameters(String params) {
    parameters.putAll(HttpRequestUtils.parseQueryString(params));
  }
}
