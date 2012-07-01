package sortpom.sort;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.sort.util.SortOrderFilesUtil;

public class SortPropertiesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
        SortOrderFilesUtil.create()
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .lineSeparator("\n")
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output.xml");
    }

    @Test
    public final void sortPropertyParameterShouldSortAlphabetically() throws Exception {
        SortOrderFilesUtil.create()
                .sortProperties()
                .lineSeparator("\n")
                .predefinedSortOrder("custom_1")
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa.xml");
    }

    @Test
    public final void testBothNamedParametersInSortFileAndSortPropertyParameterTest() throws Exception {
        SortOrderFilesUtil.create()
                .lineSeparator("\n")
                .defaultOrderFileName("difforder/sortedPropertiesOrder.xml")
                .sortProperties()
                .testFiles("/SortedProperties_input.xml", "/SortedProperties_output_alfa2.xml");
    }

    @Test
    public final void sortingOfFullPomFileShouldWork() throws Exception {
        SortOrderFilesUtil.create()
                .sortProperties()
                .sortPlugins()
                .sortDependencies()
                .lineSeparator("\n")
                .testFiles("/SortProp_input.xml", "/SortProp_expected.xml");
    }
}
