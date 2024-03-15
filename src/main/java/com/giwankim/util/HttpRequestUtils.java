package com.giwankim.util;

import com.giwankim.http.KvPair;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

  private static final String QUERYSTRING_SEPARATOR = "&";
  private static final String COOKIE_SEPARATOR = ";";

  private HttpRequestUtils() {
  }

  public static Map<String, String> parseQueryString(String queryString) {
    return parseValues(queryString, QUERYSTRING_SEPARATOR);
  }

  public static Map<String, String> parseCookies(String cookies) {
    return parseValues(cookies, COOKIE_SEPARATOR);
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
}
