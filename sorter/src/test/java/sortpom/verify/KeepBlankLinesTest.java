package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;
import sortpom.util.XmlProcessorTestUtil;

class KeepBlankLinesTest {
  @Test
  void emptyLinesInXmlShouldNotAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlShouldNotAffectVerify2() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBlankLines()
        .testVerifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlShouldNotAffectVerify3() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
  }

  @Test
  void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify2() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .indentBLankLines()
        .testVerifyXmlIsOrdered("/EmptyRow_input2.xml");
  }

  @Test
  void unsortedXmlAndIndentParameterShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .indentBLankLines()
        .testVerifyXmlIsNotOrdered(
            "/EmptyRow_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void simpleLineBreaksShouldNotAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/LineBreak_input2.xml");
  }

  @Test
  void unsortedXmlShouldAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/LineBreak_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }
}
