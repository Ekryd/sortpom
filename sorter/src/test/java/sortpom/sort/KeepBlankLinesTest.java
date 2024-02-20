package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;
import sortpom.util.XmlProcessorTestUtil;

class KeepBlankLinesTest {
  @Test
  void emptyRowsInSimplePomShouldBePreserved() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testInputAndExpected(
            "src/test/resources/EmptyRow_input.xml", "src/test/resources/EmptyRow_expected.xml");
  }

  @Test
  void emptyRowsInLargePomShouldBePreserved1() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testInputAndExpected(
            "src/test/resources/Real1_input.xml",
            "src/test/resources/Real1_expected_keepBlankLines.xml");
  }

  @Test
  void emptyRowsInLargePomShouldBePreservedAndIndented1() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBlankLines()
        .testInputAndExpected(
            "src/test/resources/Real1_input.xml",
            "src/test/resources/Real1_expected_keepBlankLines_indented.xml");
  }

  @Test
  void emptyRowsInLargePomShouldBePreserved2() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines.xml");
  }

  @Test
  void emptyRowsInLargePomShouldBePreservedAndIndented2() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBLankLines()
        .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines_indented.xml");
  }

  @Test
  void simpleLineBreaksShouldNotBePreserved() {
    XmlProcessorTestUtil.create()
        .testInputAndExpected(
            "src/test/resources/LineBreak_input.xml", "src/test/resources/Character_expected.xml");
  }

  @Test
  void allLineBreaksInXmlShouldBeNewlines() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\n")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\n  <artifactId>whitespace-test</artifactId>\n  <version>"));
    assertThat(actual, containsString(",\nembedded,\nand"));
  }

  @Test
  void allLineBreaksInXmlShouldBeCarriageReturnNewlines() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\r\n")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\r\n  <artifactId>whitespace-test</artifactId>\r\n  <version>"));
    assertThat(actual, containsString(",\r\nembedded,\r\nand"));
  }

  @Test
  void allLineBreaksInXmlShouldBeCarriageReturn() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\r")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_N_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\r  <artifactId>whitespace-test</artifactId>\r  <version>"));
    assertThat(actual, containsString(",\rembedded,\rand"));
  }

  @Test
  void allLineBreaksInXmlShouldBeNewlines2() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\n")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\n  <artifactId>whitespace-test</artifactId>\n  <version>"));
    assertThat(actual, containsString(",\nembedded,\nand"));
  }

  @Test
  void allLineBreaksInXmlShouldBeCarriageReturnNewlines2() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\r\n")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\r\n  <artifactId>whitespace-test</artifactId>\r\n  <version>"));
    assertThat(actual, containsString(",\r\nembedded,\r\nand"));
  }

  @Test
  void allLineBreaksInXmlShouldBeCarriageReturn2() {
    var actual =
        XmlProcessorTestUtil.create()
            .lineSeparator("\r")
            .sortXmlAndReturnResult("src/test/resources/LineBreak_RN_input.xml");

    assertThat(
        actual,
        containsString("</groupId>\r  <artifactId>whitespace-test</artifactId>\r  <version>"));
    assertThat(actual, containsString(",\rembedded,\rand"));
  }

  @Test
  void artifactIdsWithLineBreaksShouldBeTrimmedBeforeSorting() {
    XmlProcessorTestUtil.create()
        .lineSeparator("\n")
        .expandEmptyElements(false)
        .sortModules()
        .sortDependencies("scope,groupId,artifactId")
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .noSpaceBeforeCloseEmptyElement()
        .testInputAndExpected(
            "src/test/resources/LineBreak_input3.xml",
            "src/test/resources/LineBreak_expected3.xml");
  }

  @Test
  void emptyRowsInLargePomShouldNotBePreserved() {
    XmlProcessorTestUtil.create()
        .keepBlankLinesFalse()
        .testInputAndExpected(
            "src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected.xml");
  }
}
