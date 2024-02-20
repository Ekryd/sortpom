package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.util.XmlProcessorTestUtil;

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
}
