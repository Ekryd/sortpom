package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

public class IgnoreSectionsTest {
        @Test
    final void forceDependencyToTopTrickShouldWork() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("scope,groupId,artifactId")
                .lineSeparator("\n")
                .testFiles("/DependencyToTop_input.xml", "/DependencyToTop_expected.xml");
    }


}
