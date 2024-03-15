package com.giwankim.http;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class HttpRequestParserTest {

  @Test
  void parseGetWithQueryString() {
    InputStream in = getFileFromResources("Http_GET.txt");

    HttpRequest request = HttpRequestParser.parse(in);

    assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Connection")).isEqualTo("keep-alive");
    assertThat(request.getParameter("userId")).isEqualTo("javajigi");
  }

  @Test
  void parsePostWithBody() {
    InputStream in = getFileFromResources("Http_POST.txt");

    HttpRequest request = HttpRequestParser.parse(in);

    assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Content-Length")).isEqualTo("46");
    assertThat(request.getParameter("userId")).isEqualTo("javajigi");
  }

  @Test
  void parsePostWithBodyAndQueryString() {
    InputStream in = getFileFromResources("Http_POST2.txt");

    HttpRequest request = HttpRequestParser.parse(in);

    assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    assertThat(request.getPath()).isEqualTo("/user/create");
    assertThat(request.getHeader("Content-Type")).isEqualTo("application/x-www-form-urlencoded");
    assertThat(request.getParameter("id")).isEqualTo("1");
    assertThat(request.getParameter("name")).isEqualTo("JaeSung");
  }

  InputStream getFileFromResources(String filename) {
    return getClass().getClassLoader().getResourceAsStream(filename);
  }
}