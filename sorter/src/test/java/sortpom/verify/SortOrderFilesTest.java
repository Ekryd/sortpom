package sortpom.verify;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.util.SortPomImplUtil;

class SortOrderFilesTest {
  @Test
  void sortedCustomSortOrderShouldNotTriggerVerify() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_differentOrder.xml");
  }

  @Test
  void unsortedCustomSortOrderShouldTriggerVerify() {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .lineSeparator("\n")
        .testVerifyXmlIsNotOrdered(
            "/full_unsorted_input.xml",
            "The xml element <modelVersion> should be placed before <parent>");
  }

  @Test
  void default040ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_0_4_0.xml");
  }

  @Test
  void custom1ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("custom_1")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_custom_1.xml");
  }

  @Test
  void recommended2008_06ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("recommended_2008_06")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_recommended_2008_06.xml");
  }

  @Test
  void default100ShouldWorkAsPredefinedSortOrder() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_1_0_0")
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_1_0_0.xml");
  }

  @Test
  void illegalPredefinedSortOrderShouldReportError() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .predefinedSortOrder("special.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_1_0_0.xml");

    var thrown = assertThrows(RuntimeException.class, testMethod).getCause().getCause();

    assertThat(
        thrown.getMessage(),
        is("Cannot find special.xml.xml among the predefined plugin resources"));
  }

  @Test
  void xmlDeviationsShouldNotHarmPlugin() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .testVerifyXmlIsOrdered("/Xml_deviations_output.xml");
  }
}
