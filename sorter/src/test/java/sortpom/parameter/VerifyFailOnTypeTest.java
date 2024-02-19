package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.exception.FailureException;

/** */
class VerifyFailOnTypeTest {
  @Test
  void xmlElementsIgnoreCaseValueIsOk() {
    var pluginParameters = PluginParameters.builder().setVerifyFail("STOP", "XMLElements").build();

    assertEquals(VerifyFailOnType.XMLELEMENTS, pluginParameters.verifyFailOn);
  }

  @Test
  void strictIgnoreCaseValueIsOk() {
    var pluginParameters = PluginParameters.builder().setVerifyFail("STOP", "stRIct").build();

    assertEquals(VerifyFailOnType.STRICT, pluginParameters.verifyFailOn);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = "gurka")
  void verifyFailFaultyValues(String value) {
    Executable testMethod = () -> PluginParameters.builder().setVerifyFail("STOP", value).build();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("verifyFailOn must be either xmlElements or strict. Was: " + value)));
  }
}
