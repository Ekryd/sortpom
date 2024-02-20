package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.XmlProcessorTestUtil;

class ExpandEmptyElementTest {
  @Test
  void trueExpandedParameterAndTrueExpandedElementShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .expandEmptyElements(true)
        .testVerifyXmlIsOrdered("src/test/resources/ExpandedElement_input.xml");
  }

  @Test
  void falseExpandedParameterAndTrueExpandedElementShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .expandEmptyElements(false)
        .testVerifyXmlIsOrdered("src/test/resources/ExpandedElement_input.xml");
  }

  @Test
  void trueExpandedParameterAndFalseExpandedElementShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .expandEmptyElements(true)
        .testVerifyXmlIsOrdered("src/test/resources/ExpandedElementNot_input.xml");
  }

  @Test
  void falseExpandedParameterAndFalseExpandedElementShouldNotAffectVerify() {
    XmlProcessorTestUtil.create()
        .expandEmptyElements(true)
        .testVerifyXmlIsOrdered("src/test/resources/ExpandedElementNot_input.xml");
  }
}
