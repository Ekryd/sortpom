package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static sortpom.sort.XmlFragment.createXmlProjectFragment;

import org.dom4j.tree.BaseElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;
import sortpom.util.SortPomImplUtil;

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
            .setIndent(indent, false, false, "schemaLocation")
            .build());

    var actual = xmlOutputGenerator.getSortedXml(createXmlProjectFragment());
    var indentChars = getIndentChars(indent);

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

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, -1})
  void indentSchemaLocationShouldNotIndentOtherAttributes(int indent) {
    var xmlOutputGenerator = new XmlOutputGenerator();
    var lineSeparator = "\n";
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, true, true, false, true)
            .setIndent(indent, false, false, "schemaLocation")
            .build());

    var xmlProjectFragment = createXmlProjectFragment();
    xmlProjectFragment.getRootElement().element("Gurka").addAttribute("key", "value");
    var actual = xmlOutputGenerator.getSortedXml(xmlProjectFragment);
    var indentChars = getIndentChars(indent);

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

  @ParameterizedTest
  @ValueSource(ints = {0, 1, 4, -1})
  void indentAllShouldIndentAllAttributes(int indent) {
    var xmlOutputGenerator = new XmlOutputGenerator();
    var lineSeparator = "\n";
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("UTF-8")
            .setFormatting(lineSeparator, false, true, false, true)
            .setIndent(indent, false, false, "all")
            .build());

    var xmlProjectFragment = createXmlProjectFragment();
    var gurka = xmlProjectFragment.getRootElement().element("Gurka");
    gurka.addAttribute("key", "value");
    gurka.add(new BaseElement("Banana").addAttribute("peeled", "true"));
    var actual = xmlOutputGenerator.getSortedXml(xmlProjectFragment);

    assertEquals(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + lineSeparator
            + "<project"
            + lineSeparator
            + getIndentChars(indent * 2)
            + "xmlns=\"http://maven.apache.org/POM/4.0.0\""
            + lineSeparator
            + getIndentChars(indent * 2)
            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + lineSeparator
            + getIndentChars(indent * 2)
            + "xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">"
            + lineSeparator
            + getIndentChars(indent)
            + "<Gurka"
            + lineSeparator
            + getIndentChars(indent * 3)
            + "xmlns=\"\""
            + lineSeparator
            + getIndentChars(indent * 3)
            + "key=\"value\">"
            + lineSeparator
            + getIndentChars(indent * 2)
            + "<Banana"
            + lineSeparator
            + getIndentChars(indent * 4)
            + "peeled=\"true\" />"
            + lineSeparator
            + getIndentChars(indent)
            + "</Gurka>"
            + lineSeparator
            + "</project>"
            + lineSeparator,
        actual);
  }

  @Test
  void indentSchemaLocationShouldGiveWarning() {
    var sortPomImplUtil = SortPomImplUtil.create();
    var logs =
        sortPomImplUtil
            .sortProperties()
            .sortPlugins("groupId,artifactId")
            .sortModules()
            .sortDependencies("groupId,artifactId")
            .lineSeparator("\n")
            .nrOfIndentSpace(1)
            .indentSchemaLocation()
            .testFilesAndReturnLogs(
                "/SortModules_input.xml", "/SortModules_expectedSchemaIndent1.xml");

    assertThat(
        logs.get(0),
        is(
            "[WARNING] [DEPRECATED] The parameter 'indentSchemaLocation' is no longer supported. Please use <indentAttribute>schemaLocation</indentAttribute> instead. In the next major version, using 'indentSchemaLocation' will cause an error!"));
    assertThat(logs.get(1), startsWith("[INFO] Sorting file "));
    assertThat(logs.get(2), startsWith("[INFO] Saved backup of "));
    assertThat(logs.get(logs.size() - 1), startsWith("[INFO] Saved sorted pom file to "));
  }

  private static String getIndentChars(int indent) {
    if (indent < 0) {
      return "\t".repeat(-indent);
    }
    return " ".repeat(indent);
  }
}
