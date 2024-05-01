package sortpom.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sortpom.sort.XmlFragment.createXmlProjectFragment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;

class IndentationAttributesTest {
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, -1})
  void indentSchemaLocationShouldAddNewlineAndIndentation(int indent) {
    var xmlOutputGenerator = new XmlOutputGenerator();
    var lineSeparator = "\n";
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, true, true, false, true)
            .setIndent(indent, false, "schemaLocation")
            .build());

    var actual = xmlOutputGenerator.getSortedXml(createXmlProjectFragment());
    var indentChars = "";
    switch (indent) {
      case 1:
        indentChars = " ";
        break;
      case 4:
        indentChars = "    ";
        break;
      case -1:
        indentChars = "\t";
        break;
    }
    assertEquals(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + lineSeparator
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + lineSeparator
            + indentChars
            + indentChars
            + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">"
            + lineSeparator
            + indentChars
            + "<Gurka xmlns=\"\"></Gurka>"
            + lineSeparator
            + "</project>"
            + lineSeparator,
        actual);
  }

  @Test
  void otherAttributeShouldNotBeIndented() {
    var xmlOutputGenerator = new XmlOutputGenerator();
    var lineSeparator = "\n";
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, true, true, false, true)
            .setIndent(2, false, "schemaLocation")
            .build());

    var xmlProjectFragment = createXmlProjectFragment();
    xmlProjectFragment.getRootElement().element("Gurka").addAttribute("key", "value");
    var actual = xmlOutputGenerator.getSortedXml(xmlProjectFragment);
    var indentChars = "  ";

    assertEquals(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + lineSeparator
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + lineSeparator
            + indentChars
            + indentChars
            + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">"
            + lineSeparator
            + indentChars
            + "<Gurka xmlns=\"\" key=\"value\"></Gurka>"
            + lineSeparator
            + "</project>"
            + lineSeparator,
        actual);
  }
}
