package com.giwankim.db;

import com.giwankim.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Database {

  private static final Map<String, User> users = new ConcurrentHashMap<>();

  private Database() {
  }

  public static void addUser(User user) {
    users.put(user.getUserId(), user);
  }

  public static Optional<User> findUserById(String userId) {
    return Optional.ofNullable(users.get(userId));
  }

  public static Collection<User> findAll() {
    return Collections.unmodifiableCollection(users.values());
  }
}
