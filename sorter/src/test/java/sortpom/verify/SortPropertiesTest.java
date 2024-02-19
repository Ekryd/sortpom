package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class SortPropertiesTest {

  @Test
  void namedParametersInSortFileShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/SortedProperties_output.xml");
  }

  @Test
  void sortPropertyParameterShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortProperties()
        .lineSeparator("\n")
        .predefinedSortOrder("custom_1")
        .testVerifyXmlIsOrdered("/SortedProperties_output_alfa.xml");
  }

  @Test
  void testBothNamedParametersInSortFileAndSortPropertyParameterTestNotAffectVerify()
      throws Exception {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .sortProperties()
        .testVerifyXmlIsOrdered("/SortedProperties_output_alfa2.xml");
  }

  @Test
  void sortingOfFullPomFileShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/SortProp_expected.xml");
  }

  @Test
  void namedParametersInSortFileShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <project.build.sourceEncoding> should be placed before <other>");
  }

  @Test
  void sortPropertyParameterShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortProperties()
        .lineSeparator("\n")
        .predefinedSortOrder("custom_1")
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <another> should be placed before <other>");
  }

  @Test
  void testBothNamedParametersInSortFileAndSortPropertyParameterTestAffectVerify()
      throws Exception {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .sortProperties()
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <project.build.sourceEncoding> should be placed before <other>");
  }

  @Test
  void sortingOfFullPomFileShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortProperties()
        .predefinedSortOrder("default_0_4_0")
        .sortPlugins("groupId,artifactId")
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .testVerifyXmlIsNotOrdered(
            "/SortProp_input.xml",
            "The xml element <commons.beanutils.version> should be placed before <commons.io.version>");
  }
}
