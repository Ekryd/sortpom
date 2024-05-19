package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sortpom.exception.FailureException;

class LineSeparatorParameterTest {

  @Test
  void lineSeparatorWithSomethingElseShouldThrowException() {
    Executable testMethod =
        () ->
            PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("***", false, true, false, true)
                .setIndent(2, false, false, null);

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            equalTo(
                "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [42, 42, 42]")));
  }

  @Test
  void testCharInput() {
    assertEquals(
        "\n",
        PluginParameters.builder()
            .setFormatting("\n", true, true, true, true)
            .build()
            .lineSeparatorUtil
            .toString());
    assertEquals(
        "\r",
        PluginParameters.builder()
            .setFormatting("\r", true, true, true, true)
            .build()
            .lineSeparatorUtil
            .toString());
    assertEquals(
        "\r\n",
        PluginParameters.builder()
            .setFormatting("\r\n", true, true, true, true)
            .build()
            .lineSeparatorUtil
            .toString());
  }

  private static Stream<Arguments> provideSeparators() {
    return Stream.of(
        Arguments.of("\nn", "[10, 110]"),
        Arguments.of("\n\n", "[10, 10]"),
        Arguments.of("gurka", "[103, 117, 114, 107, 97]"),
        Arguments.of("", "[]"));
  }

  @ParameterizedTest
  @MethodSource("provideSeparators")
  void testFailedInput(String lineSeparator, String expectedChars) {
    Executable testMethod =
        () -> PluginParameters.builder().setFormatting(lineSeparator, true, true, true, true);

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            equalTo(
                "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were "
                    + expectedChars)));
  }

  @Test
  void testMixedInput() {
    assertEquals("\r\n", new LineSeparatorUtil("\\r\n").toString());
    assertEquals("\r\n", new LineSeparatorUtil("\r\\n").toString());
  }

  @Test
  void testStringInput() {
    assertEquals("\n", new LineSeparatorUtil("\\n").toString());
    assertEquals("\r", new LineSeparatorUtil("\\r").toString());
    assertEquals("\r\n", new LineSeparatorUtil("\\r\\n").toString());
  }
}
