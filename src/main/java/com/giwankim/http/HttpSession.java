package com.giwankim.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpSession {

  private final String id;

  private final Map<String, Object> attributes;

  public HttpSession(String id, Map<String, Object> attributes) {
    this.id = Objects.requireNonNull(id);
    this.attributes = Objects.requireNonNull(attributes);
  }

  public HttpSession(String id) {
    this(id, new HashMap<>());
  }

  public String getId() {
    return id;
  }

  public void setAttribute(String name, Object value) {
    attributes.put(name, value);
  }

  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }
}
