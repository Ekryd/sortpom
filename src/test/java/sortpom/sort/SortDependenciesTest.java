package sortpom.sort;

import org.junit.Test;
import sortpom.util.SortPomImplUtil;

public class SortDependenciesTest {

    @Test
    public final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("difforder/dependenciesWithScopeOrder.xml")
                .sortDependencies()
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input_simpleWithScope.xml", "/SortDep_expected_simpleWithScope.xml");
    }

    @Test
    public final void custom2ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .predefinedSortOrder("custom_2")
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input.xml",
                        "/SortDep_expected_withScope.xml");
    }

}
