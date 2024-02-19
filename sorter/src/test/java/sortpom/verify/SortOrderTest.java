package sortpom.verify;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class SortOrderTest {

  @Test
  void testSortDifferentClassPathShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .testVerifyXmlIsOrdered("/full_differentorder_expected.xml");
  }

  @Test
  void testSortXmlCharacterShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create().testVerifyXmlIsOrdered("/Character_expected.xml");
  }

  @Test
  void testSortXmlComplexShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("/Complex_expected.xml");
  }

  @Test
  void testSortXmlFullFromAlphabeticalOrderShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testVerifyXmlIsOrdered("/full_expected.xml");
  }

  @Test
  void testSortXmlReal1ShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create().testVerifyXmlIsOrdered("/Real1_expected.xml");
  }

  @Test
  void testSortXmlSimpleShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create().testVerifyXmlIsOrdered("/Simple_expected.xml");
  }

  @Test
  void testSortWithIndentShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .nrOfIndentSpace(42)
        .testVerifyXmlIsOrdered("/Simple_expected_indent.xml");
  }

  @Test
  void testSortWithDependencySortSimpleShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testVerifyXmlIsOrdered("/Simple_expected_sortDep.xml");
  }

  @Test
  void testSortWithDependencySortFullShouldNotAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testVerifyXmlIsOrdered("/SortDep_expected.xml");
  }

  @Test
  void testSortDifferentClassPathShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .testVerifyXmlIsNotOrdered(
            "/full_unsorted_input.xml",
            "The xml element <modelVersion> should be placed before <parent>");
  }

  @Test
  void testSortXmlCharacterShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/Character_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void testSortXmlComplexShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/Complex_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void testSortXmlFullFromAlphabeticalOrderShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/full_alfa_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void testSortXmlFullShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/full_unsorted_input.xml",
            "The xml element <modelVersion> should be placed before <parent>");
  }

  @Test
  void testSortXmlReal1ShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/Real1_input.xml", "The xml element <version> should be placed before <name>");
  }

  @Test
  void testSortXmlSimpleShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .testVerifyXmlIsNotOrdered(
            "/Simple_input.xml",
            "The xml element <modelVersion> should be placed before <artifactId>");
  }

  @Test
  void sortedDependenciesWithDifferentChildrenShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .testVerifyXmlIsNotOrdered(
            "/SortDepSimple_input.xml",
            "The xml element <groupId>cglib</groupId> should be placed before <groupId>junit</groupId>");
  }

  @Test
  void sortedDependenciesWithDifferentNamesShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .testVerifyXmlIsNotOrdered(
            "/SortDepSimple2_input.xml",
            "The xml element <groupId>cglib</groupId> should be placed before <groupId>junit</groupId>");
  }

  @Test
  void sortedDependenciesWithSameNameShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .testVerifyXmlIsNotOrdered(
            "/SortDepSimple3_input.xml",
            "The xml element <dependency> with 2 child elements should be placed before element <dependency> with 4 child elements");
  }

  @Test
  void testSortWithDependencySortFullShouldAffectVerify() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testVerifyXmlIsNotOrdered(
            "/SortDep_input.xml",
            "The xml element <groupId>cheesymock</groupId> should be placed before <groupId>junit</groupId>");
  }
}
