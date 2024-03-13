package com.giwankim.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
  private static final Logger logger = LoggerFactory.getLogger(HttpHeaders.class);

  private static final String KEY_VALUE_SEPARATOR = ":";

  private final Map<String, String> headers = new HashMap<>();

  public void add(String header) {
    logger.debug("Header: {}", header);
    String[] parts = header.split(KEY_VALUE_SEPARATOR);
    if (parts.length >= 2) {
      headers.put(parts[0].trim(), parts[1].trim());
    }
  }

  public String getHeader(String name) {
    return headers.get(name);
  }

  public int getContentLength() {
    String contentLength = headers.get("Content-Length");
    if (contentLength != null) {
      return Integer.parseInt(contentLength);
    }
    return 0;
  }
}
