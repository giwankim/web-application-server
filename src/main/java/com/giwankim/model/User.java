package com.giwankim.model;

public class User {
  private final String userId;
  private final String password;
  private final String name;
  private final String email;

  public User(String userId, String password, String name, String email) {
    this.userId = userId;
    this.password = password;
    this.name = name;
    this.email = email;
  }

  public String getUserId() {
    return userId;
  }

  public String getPassword() {
    return password;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public boolean comparePasswords(String password) {
    return this.password.equals(password);
  }

  @Override
  public String toString() {
    return String.format("User{userId='%s', password='%s', name='%s', email='%s'}", userId, password, name, password);
  }
}
