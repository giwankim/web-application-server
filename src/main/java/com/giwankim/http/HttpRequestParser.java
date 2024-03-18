package com.giwankim.http;

import com.giwankim.util.IOUtils;
import com.giwankim.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestParser {

  private HttpRequestParser() {
  }

  public static HttpRequest parse(BufferedReader reader) {
    try {
      RequestLine requestLine = parseRequestLine(reader);
      HttpHeaders headers = parseHeaders(reader);
      String body = IOUtils.readData(reader, headers.getContentLength());
      return HttpRequest.of(requestLine, headers, body);
    } catch (IOException e) {
      throw new HttpRequestParseException("Failed to parse", e);
    }
  }

  private static HttpHeaders parseHeaders(BufferedReader bufferedReader) throws IOException {
    HttpHeaders headers = new HttpHeaders();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      if (line.isBlank()) {
        break;
      }
      headers.add(line);
    }
    return headers;
  }

  private static RequestLine parseRequestLine(BufferedReader bufferedReader) throws IOException {
    String line = bufferedReader.readLine();
    if (StringUtils.isNullOrBlank(line)) {
      throw new HttpRequestParseException("Missing request line");
    }
    return RequestLine.from(line);
  }
}
