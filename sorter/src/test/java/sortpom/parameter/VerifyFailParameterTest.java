package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.exception.FailureException;

class VerifyFailParameterTest {

  @Test
  void stopIgnoreCaseValueIsOk() {
    var pluginParameters = PluginParameters.builder().setVerifyFail("sToP", "strict").build();

    assertThat(pluginParameters.verifyFailType, is(VerifyFailType.STOP));
  }

  @Test
  void warnIgnoreCaseValueIsOk() {
    var pluginParameters = PluginParameters.builder().setVerifyFail("wArN", "strict").build();

    assertThat(pluginParameters.verifyFailType, is(VerifyFailType.WARN));
  }

  @Test
  void sortIgnoreCaseValueIsOk() {
    var pluginParameters = PluginParameters.builder().setVerifyFail("sOrT", "strict").build();

    assertThat(pluginParameters.verifyFailType, is(VerifyFailType.SORT));
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "gurka")
  void verifyFailFaultyValues(String value) {
    Executable testMethod = () -> PluginParameters.builder().setVerifyFail(value, "strict").build();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: " + value)));
  }
}
