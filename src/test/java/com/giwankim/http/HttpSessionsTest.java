package com.giwankim.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpSessionsTest {

  @BeforeEach
  void setUp() {
    HttpSessions.removeAll();
  }

  @Test
  void getSessionIfExists() {
    String id = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
    HttpSessions.add(id);
    assertThat(HttpSessions.getOrCreateSession(id).getId()).isEqualTo(id);
  }

  @Test
  void putSessionIfNotExists() {
    String id = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
    assertThat(HttpSessions.getOrCreateSession(id).getId()).isEqualTo(id);
  }
}