package com.giwankim.http;

import com.giwankim.util.HttpRequestUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class RequestParameters {

  private final Map<String, String> parameters;

  private RequestParameters() {
    this(new HashMap<>());
  }

  private RequestParameters(Map<String, String> parameters) {
    this.parameters = parameters;
  }

  static RequestParameters of(String queryString, String body) {
    RequestParameters params = new RequestParameters();
    params.addParameters(queryString);
    params.addParameters(body);
    return params;
  }

  Map<String, String> getParameters() {
    return Collections.unmodifiableMap(parameters);
  }

  String getParameter(String name) {
    return parameters.get(name);
  }

  private void addParameters(String params) {
    parameters.putAll(HttpRequestUtils.parseQueryString(params));
  }
}
