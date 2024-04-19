package sortpom.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sortpom.sort.XmlFragment.createXmlProjectFragment;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;
import sortpom.util.SortPomImplUtil;

class IndentationTest {

  @ParameterizedTest()
  @ValueSource(ints = {0, 1, 4, -1})
  void differentIndentsShouldWork(int indent) {
    var expectedFile = "/SortModules_expectedIndent" + indent + ".xml";
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortModules()
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(indent)
        .testFiles("/SortModules_input.xml", expectedFile);
  }

  @ParameterizedTest()
  @ValueSource(ints = {0, 1, 4, -1})
  void indentSchemaLocationShouldBeIndentTimesTwoPlusOne(int indent) {
    var expectedFile = "/SortModules_expectedSchemaIndent" + indent + ".xml";
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortModules()
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(indent)
        .indentSchemaLocation()
        .testFiles("/SortModules_input.xml", expectedFile);
  }

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, -1})
  void indentSchemaLocationShouldAddNewlineAndIndentation(int indent) {
    var xmlOutputGenerator = new XmlOutputGenerator();
    var lineSeparator = "\n";
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, true, true, false, true)
            .setIndent(indent, false, true)
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
            .setIndent(2, false, true)
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
