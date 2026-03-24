package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;

class IndentCharactersParameterTest {

  @Test
  void zeroIndentCharactersShouldResultInEmptyIndentString() {
    var pluginParameters = PluginParameters.builder().setIndent(0, true, false, null).build();

    assertThat(pluginParameters.indentCharacters, is(""));
  }

  @Test
  void oneIndentCharacterShouldResultInOneSpace() {
    var pluginParameters = PluginParameters.builder().setIndent(1, true, false, null).build();

    assertThat(pluginParameters.indentCharacters, is(" "));
  }

  @Test
  void test255IndentCharacterShouldResultIn255Space() {
    var pluginParameters = PluginParameters.builder().setIndent(255, true, false, null).build();

    // Test for only space
    assertThat(pluginParameters.indentCharacters, matchesPattern("^ *$"));
    assertThat(pluginParameters.indentCharacters.length(), is(255));
  }

  @Test
  void minusOneIndentCharacterShouldResultInOneTab() {
    var pluginParameters = PluginParameters.builder().setIndent(-1, true, false, null).build();

    assertThat(pluginParameters.indentCharacters, is("\t"));
  }

  @Test
  void minusTwoShouldFail() {
    Executable testMethod =
        () -> PluginParameters.builder().setIndent(-2, true, false, null).build();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("nrOfIndentSpace cannot be below -1 or above 255, was: -2")));
  }

  @Test
  void moreThan255ShouldFail() {
    Executable testMethod =
        () -> PluginParameters.builder().setIndent(256, true, false, null).build();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("nrOfIndentSpace cannot be below -1 or above 255, was: 256")));
  }
}
