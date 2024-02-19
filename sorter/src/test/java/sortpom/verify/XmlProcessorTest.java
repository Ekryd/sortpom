package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.XmlProcessorTestUtil;

class XmlProcessorTest {

  @Test
  void testSortXmlAttributesShouldNotAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/Attribute_expected.xml");
  }

  @Test
  void testSortXmlMultilineCommentShouldNotAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsOrdered("src/test/resources/MultilineComment_expected.xml");
  }

  @Test
  void testSortXmlAttributesShouldAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/Attribute_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void testSortXmlMultilineCommentShouldAffectVerify() throws Exception {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/MultilineComment_input.xml",
            "The xml element <groupId> should be placed before <artifactId>");
  }
}
