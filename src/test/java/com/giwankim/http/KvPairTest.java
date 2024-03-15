package com.giwankim.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class KvPairTest {

  @ParameterizedTest(name = "{index}: {3}")
  @DisplayName("should parse from string")
  @MethodSource("provideStringsForParse")
  void shouldParseKvPair(String input, String key, String value, String message) {
    assertThat(KvPair.parse(input, "=")).isEqualTo(new KvPair(key, value));
  }

  private static Stream<Arguments> provideStringsForParse() {
    return Stream.of(
      Arguments.of("a=b", "a", "b", "정상적인 인풋"),
      Arguments.of("a= b", "a", "b", "value 앞에 공백"),
      Arguments.of("a=\tb", "a", "b", "value 앞에 탭"),
      Arguments.of("a =b", "a", "b", "key 뒤에 공백"),
      Arguments.of("a = b", "a", "b", "구분자 = 앞뒤로 공백"),
      Arguments.of("a=", "a", "", "value 값이 없음")
    );
  }

  @Test
  void shouldParseEmptyStringForKey() {
    assertThat(KvPair.parse("=value", "=")).isEqualTo(new KvPair("", "value"));
    assertThat(KvPair.parse("value", "=")).isEqualTo(new KvPair("", "value"));
  }

  @Test
  void testEquals() {
    KvPair a = new KvPair("a", "b");
    KvPair b = new KvPair("a", "b");
    KvPair c = new KvPair("a", "c");

    assertThat(a)
      .isEqualTo(b)
      .isNotEqualTo(c)
      .isNotEqualTo(null);
  }

  @Test
  void testHashCode() {
    KvPair a = new KvPair("a", "b");
    KvPair b = new KvPair("a", "b");

    assertThat(a).hasSameHashCodeAs(b);
  }

  @Test
  void testToStringContainsImportantThings() {
    KvPair a = new KvPair("a", "b");

    assertThat(a.toString()).contains("a");
    assertThat(a.toString()).contains("b");
  }

  @Test
  void testGetters() {
    KvPair a = new KvPair("a", "b");

    assertThat(a.getKey()).isEqualTo("a");
    assertThat(a.getValue()).isEqualTo("b");
  }
}