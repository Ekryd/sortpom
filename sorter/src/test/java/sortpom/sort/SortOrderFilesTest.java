package sortpom.sort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.util.SortPomImplUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SortOrderFilesTest {

    @Test
    final void correctCustomSortOrderShouldSortThePm() throws Exception {
        SortPomImplUtil.create()
                .customSortOrderFile("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    final void default040ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_0_4_0.xml");
    }

    @Test
    final void custom1ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("custom_1")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_custom_1.xml");
    }

    @Test
    final void recommended2008_06ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("recommended_2008_06")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_recommended_2008_06.xml");
    }

    @Test
    final void default100ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("default_1_0_0")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_1_0_0.xml");
    }

    @Test
    final void nullPredefinedSortOrderShouldReportError() {
        Executable testMethod = () ->
                SortPomImplUtil.create()
                        .predefinedSortOrder(null)
                        .lineSeparator("\n")
                        .testFiles("/full_unsorted_input.xml",
                                "/sortOrderFiles/sorted_default_1_0_0.xml");

        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, testMethod);

        assertThat(thrown.getMessage(), is("Cannot find null.xml among the predefined plugin resources"));
    }

    @Test
    final void illegalPredefinedSortOrderShouldReportError() {
        Executable testMethod = () ->
                SortPomImplUtil.create()
                        .predefinedSortOrder("special.xml")
                        .lineSeparator("\n")
                        .testFiles("/full_unsorted_input.xml",
                                "/sortOrderFiles/sorted_default_1_0_0.xml");

        final IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, testMethod);

        assertThat(thrown.getMessage(), is("Cannot find special.xml.xml among the predefined plugin resources"));
    }

    @Test
    void xmlDeviationsShouldNotHarmPlugin() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .testFiles("/Xml_deviations_input.xml", "/Xml_deviations_output.xml");
    }
}
