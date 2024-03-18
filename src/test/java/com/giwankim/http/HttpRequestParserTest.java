package com.giwankim.http;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

  @Test
  void parseGetWithQueryString() {
    HttpRequest request = parseFromFile("Http_GET.txt");

    assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
    assertThat(request.getParameter("userId")).isEqualTo("javajigi");
  }

  @Test
  void parsePostWithBody() {
    HttpRequest request = parseFromFile("Http_POST.txt");

    assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Content-Length")).isEqualTo("46");
    assertThat(request.getParameter("userId")).isEqualTo("javajigi");
  }

  @Test
  void parsePostWithBodyAndQueryString() {
    HttpRequest request = parseFromFile("Http_POST2.txt");

    assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
    assertThat(request.getParameter("id")).isEqualTo("1");
    assertThat(request.getParameter("name")).isEqualTo("JaeSung");
  }

  private HttpRequest parseFromFile(String filename) {
    InputStream in = getClass().getClassLoader().getResourceAsStream(filename);
    if (in == null) {
      throw new IllegalArgumentException("file not found: " + filename);
    }
    BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
    return HttpRequestParser.parse(reader);
  }
}