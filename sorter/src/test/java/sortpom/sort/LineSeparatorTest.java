package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static sortpom.sort.XmlFragment.createXmlFragment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;
import sortpom.util.SortPomImplUtil;

class LineSeparatorTest {
  @ParameterizedTest
  @ValueSource(strings = {"\n", "\r", "\r\n"})
  void formattingXmlWithLineEndingsShouldResultInOneLineBreakAtEnd(String lineSeparator) {
    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, false, true, false, true)
            .setIndent(2, false, false, null)
            .build());

    var actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
    assertThat(
        actual,
        is(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + lineSeparator
                + "<Gurka />"
                + lineSeparator));
  }

  @Test
  void linesInContentShouldBePreserved() {
    SortPomImplUtil.create()
        .lineSeparator("\r\n")
        .testFiles("/MultilineContent_input.xml", "/MultilineContent_expected.xml");
  }

  @Test
  void spaceInPreservedXmlShouldNotBeTrimmed() {
    SortPomImplUtil.create()
        .lineSeparator("\r\n")
        .testFiles("/PreserveContent_input.xml", "/PreserveContent_output.xml");
  }
}
