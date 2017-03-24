package sortpom.sort;

import org.junit.Test;
import sortpom.util.SortPomImplUtil;

public class SortModulesTest {
    
    @Test
    public final void sortingOfPomFileWithSubmodulesShouldWork() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("true")
                .sortModules()
                .sortDependencies("true")
                .lineSeparator("\n")
                .testFiles("/SortModules_input.xml", "/SortModules_expected.xml");
    }
    @Test
    public final void sortingOfPomFileWithSubmodulesNotEnabled() throws Exception {
        SortPomImplUtil.create()
                .sortProperties()
                .sortPlugins("true")
                .sortDependencies("true")
                .lineSeparator("\n")
                .testFiles("/SortModules_input.xml", "/SortModules_expected_notsorted.xml");
    }
}
