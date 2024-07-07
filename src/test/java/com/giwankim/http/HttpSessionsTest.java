package com.giwankim.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HttpSessionsTest {

  private String sessionId;

  @BeforeEach
  void setUp() {
    sessionId = UUID.randomUUID().toString();
    HttpSessions.removeAll();
  }

  @Test
  void getSessionIfExists() {
    HttpSessions.add(sessionId);
    assertThat(HttpSessions.getOrCreateSession(sessionId).getId()).isEqualTo(sessionId);
  }

  @Test
  void putSessionIfNotExists() {
    assertThat(HttpSessions.getOrCreateSession(sessionId).getId()).isEqualTo(sessionId);
  }
}
