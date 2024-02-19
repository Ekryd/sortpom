package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.util.XmlProcessorTestUtil;

class XmlProcessorTest {

  @Test
  void testSortXmlAttributes() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testInputAndExpected(
            "src/test/resources/Attribute_input.xml", "src/test/resources/Attribute_expected.xml");
  }

  @Test
  void testSortXmlCharacter() throws Exception {
    XmlProcessorTestUtil.create()
        .testInputAndExpected(
            "src/test/resources/Character_input.xml", "src/test/resources/Character_expected.xml");
  }

  @Test
  void testSortXmlComplex() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testInputAndExpected(
            "src/test/resources/Complex_input.xml", "src/test/resources/Complex_expected.xml");
  }

  @Test
  void testSortXmlFullFromAlphabeticalOrder() throws Exception {
    XmlProcessorTestUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testInputAndExpected(
            "src/test/resources/full_alfa_input.xml", "src/test/resources/full_expected.xml");
  }

  @Test
  void testSortXmlFullToAlphabetical() throws Exception {
    XmlProcessorTestUtil.create()
        .sortAlphabeticalOnly()
        .testInputAndExpected(
            "src/test/resources/full_unsorted_input.xml", "src/test/resources/full_alfa_input.xml");
  }

  @Test
  void testSortXmlMultilineComment() throws Exception {
    XmlProcessorTestUtil.create()
        .testInputAndExpected(
            "src/test/resources/MultilineComment_input.xml",
            "src/test/resources/MultilineComment_expected.xml");
  }

  @Test
  void testSortXmlSimple() throws Exception {
    XmlProcessorTestUtil.create()
        .testInputAndExpected(
            "src/test/resources/Simple_input.xml", "src/test/resources/Simple_expected.xml");
  }
}
