package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.XmlProcessorTestUtil;

class XmlProcessorTest {

  @Test
  void testSortXmlAttributesShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("src/test/resources/Attribute_expected.xml");
  }

  @Test
  void testSortXmlMultilineCommentShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsOrdered("src/test/resources/MultilineComment_expected.xml");
  }

  @Test
  void testSortXmlAttributesShouldAffectVerify() {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/Attribute_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void testSortXmlMultilineCommentShouldAffectVerify() {
    XmlProcessorTestUtil.create()
        .testVerifyXmlIsNotOrdered(
            "src/test/resources/MultilineComment_input.xml",
            "The xml element <groupId> should be placed before <artifactId>");
  }
}
