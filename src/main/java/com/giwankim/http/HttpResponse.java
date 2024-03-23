package com.giwankim.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpResponse {
  private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

  private static final String WEBAPP_PATH = "src/main/webapp";

  private static final String HEADER_CONTENT_TYPE = "Content-Type";

  private static final String HEADER_CONTENT_LENGTH = "Content-Length";

  private static final String HEADER_SET_COOKIE = "Set-Cookie";

  private static final String CRLF = "\r\n";

  private final DataOutputStream writer;

  private final Map<String, String> headers;

  public HttpResponse(DataOutputStream writer) {
    this.writer = Objects.requireNonNull(writer);
    this.headers = new HashMap<>();
  }

  public static HttpResponse from(DataOutputStream dos) {
    return new HttpResponse(dos);
  }

  public Map<String, String> getHeaders() {
    return Collections.unmodifiableMap(headers);
  }

  public String getHeader(String name) {
    return headers.get(name);
  }

  public void setHeader(String name, String value) {
    headers.put(name, value);
  }

  public void setCookie(String name, String value) {
    setHeader(HEADER_SET_COOKIE, name + "=" + value + "; Path=/");
  }

  public void forward(String url) {
    try {
      if (url.endsWith(".css")) {
        headers.put(HEADER_CONTENT_TYPE, ContentType.TEXT_CSS.getType());
      } else if (url.endsWith(".js")) {
        headers.put(HEADER_CONTENT_TYPE, ContentType.APPLICATION_JAVASCRIPT.getType());
      } else {
        headers.put(HEADER_CONTENT_TYPE, ContentType.TEXT_HTML.getType());
      }
      byte[] body = Files.readAllBytes(Paths.get(WEBAPP_PATH, url));
      headers.put(HEADER_CONTENT_LENGTH, String.valueOf(body.length));
      sendBody(body);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public void forwardBody(String body) {
    byte[] contents = body.getBytes();
    headers.put(HEADER_CONTENT_TYPE, ContentType.TEXT_HTML.getType());
    headers.put(HEADER_CONTENT_LENGTH, String.valueOf(contents.length));
    sendBody(contents);
  }

  private void sendBody(byte[] body) {
    try {
      processStatusLine(HttpStatus.OK);
      processHeaders();
      writer.write(body, 0, body.length);
      writer.writeBytes(CRLF);
      writer.flush();
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  public void sendRedirect(String location) {
    headers.put("Location", location);
    processStatusLine(HttpStatus.FOUND);
    processHeaders();
  }

  private void processStatusLine(HttpStatus httpStatus) {
    try {
      String statusLine = String.format("HTTP/1.1 %d %s%s", httpStatus.getStatusCode(), httpStatus.getReason(), CRLF);
      writer.writeBytes(statusLine);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }

  private void processHeaders() {
    try {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        String key = entry.getKey();
        String value = entry.getValue();
        writer.writeBytes(key + ": " + value + CRLF);
      }
      writer.writeBytes(CRLF);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
  }
}
