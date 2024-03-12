package com.giwankim.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

  @ParameterizedTest
  @ValueSource(strings = {"abc", "훈민정음", "!@#", "✅"})
  void isNullOrEmptyReturnsFalseForEmptyString(String string) {
    assertThat(StringUtils.isNullOrEmpty(string)).isFalse();
  }


  @ParameterizedTest
  @ValueSource(strings = {"  ", "\t", "\n"})
  void isNullOrEmptyReturnsFalseForWhitespace(String string) {
    assertThat(StringUtils.isNullOrEmpty(string)).isFalse();
  }

  @Test
  void isNullOrEmptyReturnsTrueForNull() {
    assertThat(StringUtils.isNullOrEmpty(null)).isTrue();
  }

  @Test
  void isNullOrEmptyReturnsTrueForEmptyString() {
    assertThat(StringUtils.isNullOrEmpty("")).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"abc", "훈민정음", "!@#", "✅"})
  void isNullOrBlankReturnsFalseForNonEmptyString(String string) {
    assertThat(StringUtils.isNullOrBlank(string)).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"  ", "\t", "\n"})
  void isNullOrBlankReturnsTrueForWhitespace(String string) {
    assertThat(StringUtils.isNullOrBlank(string)).isTrue();
  }

  @Test
  void isNullOrBlankReturnsTrueForNull() {
    assertThat(StringUtils.isNullOrBlank(null)).isTrue();
  }

  @Test
  void isNullOrBlankReturnsTrueForEmptyString() {
    assertThat(StringUtils.isNullOrBlank("")).isTrue();
  }
}