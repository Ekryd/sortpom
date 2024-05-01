package sortpom.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    assertEquals(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + lineSeparator + "<Gurka />" + lineSeparator,
        actual);
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
