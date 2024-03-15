package com.giwankim.http;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RequestParametersTest {

  @Test
  void of() {
    RequestParameters params = RequestParameters.of("id=1", "userId=testId&password=password");
    assertThat(params.getParameter("id")).isEqualTo("1");
    assertThat(params.getParameter("userId")).isEqualTo("testId");
    assertThat(params.getParameter("password")).isEqualTo("password");
  }
}