package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class SortOrderTest {

  @Test
  void testSortDifferentClassPath() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
  }

  @Test
  void testSortDifferentRelativePath() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("src/test/resources/difforder/differentOrder.xml")
        .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
  }

  @Test
  void testSortXmlCharacter() throws Exception {
    SortPomImplUtil.create().testFiles("/Character_input.xml", "/Character_expected.xml");
  }

  @Test
  void testSortXmlComplex() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testFiles("/Complex_input.xml", "/Complex_expected.xml");
  }

  @Test
  void testSortXmlFullFromAlphabeticalOrder() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testFiles("/full_alfa_input.xml", "/full_expected.xml");
  }

  @Test
  void testSortXmlFull() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .testFiles("/full_unsorted_input.xml", "/full_expected.xml");
  }

  @Test
  void testSortXmlReal1() throws Exception {
    SortPomImplUtil.create()
        .noKeepBlankLines()
        .testFiles("/Real1_input.xml", "/Real1_expected.xml");
  }

  @Test
  void testSortXmlSimple() throws Exception {
    SortPomImplUtil.create().testFiles("/Simple_input.xml", "/Simple_expected.xml");
  }

  @Test
  void testSortWithIndent() throws Exception {
    SortPomImplUtil.create()
        .nrOfIndentSpace(4)
        .testFiles("/Simple_input.xml", "/Simple_expected_indent.xml");
  }

  @Test
  void testSortWithDependencySortSimple() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testFiles("/Simple_input.xml", "/Simple_expected_sortDep.xml");
  }

  @Test
  void testSortWithDependencySortFull() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testFiles("/SortDep_input.xml", "/SortDep_expected.xml");
  }

  @Test
  void sortedFileShouldNotBeSorted() throws Exception {
    SortPomImplUtil.create()
        .sortDependencies("groupId,artifactId")
        .sortPlugins("groupId,artifactId")
        .testNoSorting("/SortDep_expected.xml");
  }

  @Test
  void corruptFileShouldThrowException() {
    Executable testMethod =
        () -> SortPomImplUtil.create().testFiles("/Corrupt_file.xml", "/Corrupt_file.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        allOf(
            endsWith("content: <project><artifactId>sortpom</artifactId>"),
            startsWith("Could not sort ")));
  }
}
