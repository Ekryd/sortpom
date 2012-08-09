package sortpom.verify;

import org.junit.Test;
import sortpom.verify.util.VerifyOrderFilesUtil;

public class SortOrderTest {

    @Test
    public final void testSortDifferentClassPathShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .verifyXmlIsOrdered("/full_differentorder_expected.xml");
    }

    @Test
    public final void testSortXmlCharacterToAlfabeticalShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsOrdered("/Character_expected.xml");
    }

    @Test
    public final void testSortXmlComplexToAlfabeticalShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsOrdered("/Complex_expected.xml");
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrderShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsOrdered("/full_expected.xml");
    }

    @Test
    public final void testSortXmlReal1ShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsOrdered("/Real1_expected.xml");
    }

    @Test
    public final void testSortXmlSimpleShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsOrdered("/Simple_expected.xml");
    }

    @Test
    public final void testSortWithIndentShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .nrOfIndentSpace(4)
                .verifyXmlIsOrdered("/Simple_expected_indent.xml");
    }

    @Test
    public final void testSortWithDependencySortSimpleShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortDependencies()
                .sortPlugins()
                .verifyXmlIsOrdered("/Simple_expected_sortDep.xml");
    }

    @Test
    public final void testSortWithDependencySortFullShouldNotAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortDependencies()
                .sortPlugins()
                .verifyXmlIsOrdered("/SortDep_expected.xml");
    }


    
    @Test
    public final void testSortDifferentClassPathShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .defaultOrderFileName("difforder/differentOrder.xml")
                .verifyXmlIsNotOrdered("/full_unsorted_input.xml");
    }

    @Test
    public final void testSortXmlCharacterToAlfabeticalShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/Character_input.xml");
    }

    @Test
    public final void testSortXmlComplexToAlfabeticalShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/Complex_input.xml");
    }

    @Test
    public final void testSortXmlFullFromAlfabeticalOrderShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/full_alfa_input.xml");
    }

    @Test
    public final void testSortXmlFullToAlfabeticalShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/full_unsorted_input.xml");
    }

    @Test
    public final void testSortXmlReal1ShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/Real1_input.xml");
    }

    @Test
    public final void testSortXmlSimpleShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyXmlIsNotOrdered("/Simple_input.xml");
    }

    @Test
    public final void testSortWithDependencySortFullShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .sortDependencies()
                .sortPlugins()
                .verifyXmlIsNotOrdered("/SortDep_input.xml");
    }
}
