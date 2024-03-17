package com.giwankim.http;

public enum ContentType {
  APPLICATION_JAVASCRIPT("application/javascript"),
  TEXT_HTML("text/html"),
  TEXT_CSS("text/css");

  private final String type;

  ContentType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return type;
  }
}
