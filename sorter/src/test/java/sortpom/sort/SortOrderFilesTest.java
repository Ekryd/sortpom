package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class SortOrderFilesTest {

  @Test
  void correctCustomSortOrderShouldSortThePm() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .lineSeparator("\n")
        .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
  }

  @Test
  void default040ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .lineSeparator("\n")
        .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_default_0_4_0.xml");
  }

  @Test
  void custom1ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("custom_1")
        .lineSeparator("\n")
        .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_custom_1.xml");
  }

  @Test
  void recommended2008_06ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("recommended_2008_06")
        .lineSeparator("\n")
        .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_recommended_2008_06.xml");
  }

  @Test
  void default100ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_1_0_0")
        .lineSeparator("\n")
        .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_default_1_0_0.xml");
  }

  @Test
  void nullPredefinedSortOrderShouldReportError() {
    var sortPomImplUtil = SortPomImplUtil.create().predefinedSortOrder(null).lineSeparator("\n");

    var thrown =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                sortPomImplUtil.testFiles(
                    "/full_unsorted_input.xml", "/sortOrderFiles/sorted_default_1_0_0.xml"));

    assertThat(
        thrown.getMessage(), is("Cannot find null.xml among the predefined plugin resources"));
  }

  @Test
  void illegalPredefinedSortOrderShouldReportError() {
    var sortPomImplUtil =
        SortPomImplUtil.create().predefinedSortOrder("special.xml").lineSeparator("\n");

    var thrown =
        assertThrows(
            RuntimeException.class,
            () ->
                sortPomImplUtil.testFiles(
                    "/full_unsorted_input.xml", "/sortOrderFiles/sorted_default_1_0_0.xml"));

    assertThat(
        thrown.getMessage(),
        is("Cannot find special.xml.xml among the predefined plugin resources"));
  }

  @Test
  void xmlDeviationsShouldNotHarmPlugin() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .testFiles("/Xml_deviations_input.xml", "/Xml_deviations_output.xml");
  }

  @Test
  void evilDocTypeShouldNotHarmPlugin() {
    var sortPomImplUtil = SortPomImplUtil.create().lineSeparator("\n");
    var thrown =
        assertThrows(
            FailureException.class, () -> sortPomImplUtil.testNoSorting("/Xml_attack_input2.xml"));

    assertThat(thrown.getCause().getMessage(), containsString("DOCTYPE is disallowed"));
  }
}
