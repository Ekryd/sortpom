package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class SortPropertiesTest {

  @Test
  void namedParametersInSortFileShouldNotAffectVerify() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/SortedProperties_output.xml");
  }

  @Test
  void sortPropertyParameterShouldNotAffectVerify() {
    SortPomImplUtil.create()
        .sortProperties()
        .lineSeparator("\n")
        .predefinedSortOrder("custom_1")
        .testVerifyXmlIsOrdered("/SortedProperties_output_alfa.xml");
  }

  @Test
  void testBothNamedParametersInSortFileAndSortPropertyParameterTestNotAffectVerify() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .sortProperties()
        .testVerifyXmlIsOrdered("/SortedProperties_output_alfa2.xml");
  }

  @Test
  void sortingOfFullPomFileShouldNotAffectVerify() {
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/SortProp_expected.xml");
  }

  @Test
  void namedParametersInSortFileShouldAffectVerify() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <project.build.sourceEncoding> should be placed before <other>");
  }

  @Test
  void sortPropertyParameterShouldAffectVerify() {
    SortPomImplUtil.create()
        .sortProperties()
        .lineSeparator("\n")
        .predefinedSortOrder("custom_1")
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <another> should be placed before <other>");
  }

  @Test
  void testBothNamedParametersInSortFileAndSortPropertyParameterTestAffectVerify() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .sortProperties()
        .testVerifyXmlIsNotOrdered(
            "/SortedProperties_input.xml",
            "The xml element <project.build.sourceEncoding> should be placed before <other>");
  }

  @Test
  void sortingOfFullPomFileShouldAffectVerify() {
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
