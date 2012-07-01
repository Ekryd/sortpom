package sortpom.sort;

import org.junit.Test;
import sortpom.sort.util.SortOrderFilesUtil;

public class SortOrderTest {

    @Test
    public final void testSortDifferentClassPath() throws Exception {
        SortOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
    }

    @Test
    public final void testSortDifferentRelativePath() throws Exception {
        SortOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml");
    }

    @Test
    public final void testSortXmlCharacterToAlfabetical() throws Exception {
        SortOrderFilesUtil.create().testFiles("/Character_input.xml", "/Character_expected.xml");
    }

    @Test
    public final void testSortXmlComplexToAlfabetical() throws Exception {
        SortOrderFilesUtil.create().testFiles("/Complex_input.xml", "/Complex_expected.xml");
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
        SortOrderFilesUtil.create().testFiles("/full_alfa_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlFullToAlfabetical() throws Exception {
        SortOrderFilesUtil.create().testFiles("/full_unsorted_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlReal1() throws Exception {
        SortOrderFilesUtil.create().testFiles("/Real1_input.xml", "/Real1_expected.xml");
    }

    @Test
    public final void testSortXmlSimple() throws Exception {
        SortOrderFilesUtil.create().testFiles("/Simple_input.xml", "/Simple_expected.xml");
    }

    @Test
    public final void testSortWithIndent() throws Exception {
        SortOrderFilesUtil.create()
                .nrOfIndentSpace(4)
                .testFiles("/Simple_input.xml", "/Simple_expected_indent.xml");
    }

    @Test
    public final void testSortWithDependencySortSimple() throws Exception {
        SortOrderFilesUtil.create()
                .sortDependencies()
                .sortPlugins()
                .testFiles("/Simple_input.xml", "/Simple_expected_sortDep.xml");
    }

    @Test
    public final void testSortWithDependencySortFull() throws Exception {
        SortOrderFilesUtil.create()
                .sortDependencies()
                .sortPlugins()
                .testFiles("/SortDep_input.xml", "/SortDep_expected.xml");
    }

}
