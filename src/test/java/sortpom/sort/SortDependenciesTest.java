package sortpom.sort;

import org.junit.Test;
import sortpom.util.SortPomImplUtil;

public class SortDependenciesTest {

    @Test
    public final void namedParametersInSortFileShouldSortThemFirst() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("difforder/dependenciesWithScopeOrder.xml")
                .sortDependencies("true")
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input_simpleWithScope.xml", "/SortDep_expected_simpleWithScope.xml");
    }

    @Test
    public final void namedParametersInSortFileShouldSortThemFirst2() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("difforder/dependenciesWithScopeOrder.xml")
                .sortDependencies("groupId,artifactId")
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input_simpleWithScope.xml", "/SortDep_expected_simpleWithScope.xml");
    }

    @Test
    public final void scopeInSortDependenciesShouldSortByScope() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("custom_1.xml")
                .sortDependencies("scope,groupId,artifactId")
                .lineSeparator("\r\n")
                .testFiles("/SortDep_input_simpleWithScope.xml", "/SortDep_expected_simpleWithScope2.xml");
    }

}
