package com.giwankim.http;

import com.giwankim.util.IOUtils;
import com.giwankim.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpRequestParser {

  private HttpRequestParser() {
  }

  public static HttpRequest parse(InputStream in) {
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, UTF_8));
      RequestLine requestLine = parseRequestLine(bufferedReader);
      HttpHeaders headers = parseHeaders(bufferedReader);
      String body = IOUtils.readData(bufferedReader, headers.getContentLength());
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
