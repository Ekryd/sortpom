package sortpom;

import org.junit.Test;

public class SortOrderTest {

    @Test
    public final void testSortDifferentClassPath() throws Exception {
        SortOrderFilesUtil.testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml",
                "difforder/differentOrder.xml", 2, false, false, "", "\r\n", false, false);
    }

    @Test
    public final void testSortDifferentRelativePath() throws Exception {
        SortOrderFilesUtil.testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml",
                "src/test/resources/difforder/differentOrder.xml", 2, false, false, "", "\r\n", false, false);
    }

    @Test
    public final void testSortXmlCharacterToAlfabetical() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/Character_input.xml", "/Character_expected.xml");
    }

    @Test
    public final void testSortXmlComplexToAlfabetical() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/Complex_input.xml", "/Complex_expected.xml");
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/full_alfa_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlFullToAlfabetical() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/full_unsorted_input.xml", "/full_expected.xml");
    }

    @Test
    public final void testSortXmlReal1() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/Real1_input.xml", "/Real1_expected.xml");
    }

    @Test
    public final void testSortXmlSimple() throws Exception {
        SortOrderFilesUtil.testFilesDefaultOrder("/Simple_input.xml", "/Simple_expected.xml");
    }

    @Test
    public final void testSortWithIndent() throws Exception {
        SortOrderFilesUtil.testFiles("/Simple_input.xml", "/Simple_expected_indent.xml", "default_0_4_0.xml", 4, false,
                false, "", "\r\n", false, false);
    }

    @Test
    public final void testSortWithDependencySortSimple() throws Exception {
        SortOrderFilesUtil.testFiles("/Simple_input.xml", "/Simple_expected_sortDep.xml", "default_0_4_0.xml", 2, true,
                true, "", "\r\n", false, false);
    }

    @Test
    public final void testSortWithDependencySortFull() throws Exception {
        SortOrderFilesUtil.testFiles("/SortDep_input.xml", "/SortDep_expected.xml", "default_0_4_0.xml", 2, true, true,
                "", "\r\n", false, false);
    }

}
