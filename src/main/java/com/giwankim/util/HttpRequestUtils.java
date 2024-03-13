package com.giwankim.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequestUtils {

  private static final String QUERYSTRING_SEPARATOR = "&";

  private HttpRequestUtils() {
  }

  public static Map<String, String> parseQueryString(String queryString) {
    return Collections.unmodifiableMap(parseValues(queryString, QUERYSTRING_SEPARATOR));
  }

  private static Map<String, String> parseValues(String values, String separator) {
    if (StringUtils.isNullOrBlank(values)) {
      return Collections.emptyMap();
    }
    String[] pairs = values.split(separator);
    return Arrays.stream(pairs)
      .map(token -> KvPair.parse(token, "="))
      .filter(p -> !p.getKey().isEmpty())
      .collect(Collectors.toMap(KvPair::getKey, KvPair::getValue));
  }

  public static class KvPair {
    private final String key;
    private final String value;

    public KvPair(String key, String value) {
      this.key = key;
      this.value = value;
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
}
