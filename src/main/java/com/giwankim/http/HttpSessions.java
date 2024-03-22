package com.giwankim.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessions {

  private HttpSessions() {
  }

  public static final String SESSION_ID_NAME = "JSESSIONID";

  private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

  public static HttpSession getSession(String id) {
    return sessions.get(id);
  }

  static void remove(String id) {
    sessions.remove(id);
  }
}
