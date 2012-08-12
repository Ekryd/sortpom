package sortpom.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.verify.util.VerifyOrderFilesUtil;

import static org.hamcrest.Matchers.endsWith;

public class SortOrderFilesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void sortedCustomSortOrderShouldNotTriggerVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void unsortedCustomSortOrderShouldTriggerVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testVerifyXmlIsNotOrdered("/full_unsorted_input.xml", 
                        "The xml element <modelVersion> should be placed before <parent>");
    }

    @Test
    public final void incorrectCustomSortOrderShouldThrowException() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage(endsWith("VERYdifferentOrder.xml in classpath"));
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/VERYdifferentOrder.xml")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void incorrectPredefinedSortOrderShouldThrowException() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Cannot find abbie_normal_brain.xml among the predefined plugin resources");
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("abbie_normal_brain")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_0_4_0.xml");
    }

    @Test
    public final void default040ShouldWorkAsPredefinedSortOrder() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("default_0_4_0")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_0_4_0.xml");
    }

    @Test
    public final void custom1ShouldWorkAsPredefinedSortOrder() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("custom_1")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_custom_1.xml");
    }

    @Test
    public final void recommended2008_06ShouldWorkAsPredefinedSortOrder() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("recommended_2008_06")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_recommended_2008_06.xml");
    }

    @Test
    public final void default100ShouldWorkAsPredefinedSortOrder() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("default_1_0_0")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_1_0_0.xml");
    }

    @Test
    public final void defaultPredefinedSortOrderShouldWork() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder(null)
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_1_0_0.xml");
    }
    
    @Test
    public void xmlDeviationsShouldNotHarmPlugin() throws Exception {
        VerifyOrderFilesUtil.create()
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/Xml_deviations_output.xml");
    }

}
