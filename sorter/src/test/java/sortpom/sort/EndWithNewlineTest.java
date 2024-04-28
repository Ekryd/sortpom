package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.parameter.PluginParameters;
import sortpom.util.XmlProcessorTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EndWithNewlineTest {
  @Test
  void endWithNewlineFalseShouldNotOutputFinalNewline() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .endWithNewlineFalse()
        .testInputAndExpected(
            "src/test/resources/Real1_input.xml",
            "src/test/resources/Real1_expected_noFinalEndline.xml");
  }

  @Test
  @SuppressWarnings("removal")
  void endWithNewlineShouldDefaultToTrue() {
    PluginParameters pluginParameters = PluginParameters.builder()
        .setFormatting(System.lineSeparator(), true, false, true)
        .build();
    assertThat(pluginParameters.endWithNewline, is(true));
  }
}
