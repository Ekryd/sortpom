package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;
import sortpom.util.XmlProcessorTestUtil;

class KeepBlankLinesTest {
  @Test
  void emptyLinesInXmlShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlShouldNotAffectVerify2() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBlankLines()
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlShouldNotAffectVerify3() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify2() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBLankLines()
        .testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
  }

  @Test
  void unsortedXmlAndIndentParameterShouldAffectVerify() {
    SortPomImplUtil.create()
        .indentBLankLines()
        .testVerifyXmlIsNotOrdered(
            "/EmptyRow_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void simpleLineBreaksShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/LineBreak_input2.xml");
  }

  @Test
  void unsortedXmlShouldAffectVerify() {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/LineBreak_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }
}
