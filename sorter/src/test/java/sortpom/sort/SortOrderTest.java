package sortpom.sort;

import org.junit.Test;
import sortpom.util.SortPomImplUtil;

public class SortOrderTest {

    @Test
    public final void testSortDifferentClassPath() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
    }

    @Test
    public final void testSortDifferentRelativePath() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
    }

    @Test
    public final void testSortXmlCharacterl() throws Exception {
        SortPomImplUtil.create().testFiles("/Character_input.xml", "/Character_expected.xml");
    }

    @Test
    public final void testSortXmlComplex() throws Exception {
        SortPomImplUtil.create().testFiles("/Complex_input.xml", "/Complex_expected.xml");
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
        SortPomImplUtil.create().testFiles("/full_alfa_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlFull() throws Exception {
        SortPomImplUtil.create()
                .testFiles("/full_unsorted_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlReal1() throws Exception {
        SortPomImplUtil.create()
                .testFiles("/Real1_input.xml", "/Real1_expected.xml");
    }

    @Test
    public final void testSortXmlSimple() throws Exception {
        SortPomImplUtil.create().testFiles("/Simple_input.xml", "/Simple_expected.xml");
    }

    @Test
    public final void testSortWithIndent() throws Exception {
        SortPomImplUtil.create()
                .nrOfIndentSpace(4)
                .testFiles("/Simple_input.xml", "/Simple_expected_indent.xml");
    }

    @Test
    public final void testSortWithDependencySortSimple() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("true")
                .sortPlugins("true")
                .testFiles("/Simple_input.xml", "/Simple_expected_sortDep.xml");
    }

    @Test
    public final void testSortWithDependencySortFull() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("true")
                .sortPlugins("true")
                .testFiles("/SortDep_input.xml", "/SortDep_expected.xml");
    }

    @Test
    public final void sortedFileShouldNotBeSorted() throws Exception {
        SortPomImplUtil.create()
                .sortDependencies("groupId,artifactId")
                .sortPlugins("groupId,artifactId")
                .testNoSorting("/SortDep_expected.xml");
    }

}
