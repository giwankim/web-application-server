package com.giwankim.http;

import java.util.Objects;

public class KvPair {
  private final String key;
  private final String value;

  public KvPair(String key, String value) {
    this.key = Objects.requireNonNull(key);
    this.value = Objects.requireNonNull(value);
  }

  public static KvPair parse(String input, String regex) {
    String[] pairs = input.split(regex, 2);
    if (pairs.length >= 2) {
      return new KvPair(pairs[0].trim(), pairs[1].trim());
    }
    return new KvPair("", input);
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof KvPair kvPair)) return false;
    return Objects.equals(key, kvPair.key) && Objects.equals(value, kvPair.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, value);
  }

  @Override
  public String toString() {
    return String.format("KvPair{key='%s', value='%s'}", key, value);
  }
}
