package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class SortPropertiesTest {

  @Test
  void namedParametersInSortFileShouldSortThemFirst() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .lineSeparator("\n")
        .testFiles("/SortedProperties_input.xml", "/SortedProperties_output.xml");
  }

  @Test
  void sortPropertyParameterShouldSortAlphabetically() {
    SortPomImplUtil.create()
        .sortProperties()
        .lineSeparator("\n")
        .predefinedSortOrder("custom_1")
        .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa.xml");
  }

  @Test
  void testBothNamedParametersInSortFileAndSortPropertyParameterTest() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .customSortOrderFile("difforder/sortedPropertiesOrder.xml")
        .sortProperties()
        .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa2.xml");
  }

  @Test
  void sortingOfFullPomFileShouldWork() {
    SortPomImplUtil.create()
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .testFiles("/SortProp_input.xml", "/SortProp_expected.xml");
  }

  @Test
  void duplicatePropertiesShouldNotTriggerSorting() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .sortProperties()
        .sortPlugins("groupId,artifactId")
        .sortDependencies("groupId,artifactId")
        .lineSeparator("\n")
        .testNoSorting("/SortProp_input_duplicate.xml");
  }
}
