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
}
