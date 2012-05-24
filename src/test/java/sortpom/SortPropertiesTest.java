package sortpom;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class SortPropertiesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
        SortOrderFilesUtil.testFilesWithCustomSortOrder("/SortedProperties_input.xml", "/SortedProperties_output.xml",
                "difforder/sortedPropertiesOrder.xml");
    }

    @Test
    public final void sortPropertyParameterShouldSortAlphabetically() throws Exception {
        SortOrderFilesUtil.testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa.xml", null, 2,
                false, false, "custom_1", "\n", true, false);
    }

    @Test
    public final void testBothNamedParametersInSortFileAndSortPropertyParameterTest() throws Exception {
        SortOrderFilesUtil.testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa2.xml",
                "difforder/sortedPropertiesOrder.xml", 2, false, false, "custom_1", "\n", true, false);
    }

    @Test
    public final void sortingOfFullPomFileShouldWork() throws Exception {
        SortOrderFilesUtil.testFiles("/SortProp_input.xml", "/SortProp_expected.xml", "default_0_4_0.xml", 2, true,
                true, "", "\n", true, false);
    }
}
