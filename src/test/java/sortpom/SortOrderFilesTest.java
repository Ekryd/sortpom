package sortpom;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.endsWith;

public class SortOrderFilesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void correctCustomSortOrderShouldSortThePm() throws Exception {
        SortOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void incorrectCustomSortOrderShouldThrowException() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(endsWith("VERYdifferentOrder.xml in classpath"));
        SortOrderFilesUtil.create()
                .defaultOrderFileName("difforder/VERYdifferentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void incorrectPredefinedSortOrderShouldThrowException() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot find abbie_normal_brain.xml among the predefined plugin resources");
        SortOrderFilesUtil.create()
                .predefinedSortOrder("abbie_normal_brain")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_0_4_0.xml");
    }

    @Test
    public final void default040ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortOrderFilesUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_0_4_0.xml");
    }

    @Test
    public final void custom1ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortOrderFilesUtil.create()
                .predefinedSortOrder("custom_1")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_custom_1.xml");
    }

    @Test
    public final void recommended2008_06ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortOrderFilesUtil.create()
                .predefinedSortOrder("recommended_2008_06")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_recommended_2008_06.xml");
    }

    @Test
    public final void default100ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortOrderFilesUtil.create()
                .predefinedSortOrder("default_1_0_0")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_1_0_0.xml");
    }

    @Test
    public final void defaultPredefinedSortOrderShouldWork() throws Exception {
        SortOrderFilesUtil.create()
                .predefinedSortOrder(null)
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml",
                        "/sortOrderFiles/sorted_default_1_0_0.xml");
    }

}
