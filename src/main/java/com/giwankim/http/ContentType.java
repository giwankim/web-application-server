package com.giwankim.http;

enum ContentType {
  APPLICATION_JAVASCRIPT("application/javascript"),
  TEXT_HTML("text/html"),
  TEXT_CSS("text/css");

  private final String type;

  ContentType(String type) {
    this.type = type;
  }

  String getType() {
    return type;
  }

  @Override
  public String toString() {
    return type;
  }
}
