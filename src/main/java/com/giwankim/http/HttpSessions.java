package com.giwankim.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSessions {

  public static final String SESSION_ID_NAME = "JSESSIONID";

  private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

  private HttpSessions() {
  }

  static HttpSession getOrCreateSession(String id) {
    if (sessions.containsKey(id)) {
      return sessions.get(id);
    }
    HttpSession session = new HttpSession(id);
    sessions.put(id, session);
    return session;
  }

  static void add(String id) {
    sessions.put(id, new HttpSession(id));
  }

  static void remove(String id) {
    sessions.remove(id);
  }

  static void removeAll() {
    sessions.clear();
  }
}
