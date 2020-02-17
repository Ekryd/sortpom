package sortpom.sort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .defaultOrderFileName("src/test/resources/difforder/differentOrder.xml")
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

    @Test
    public final void corruptFileShouldThrowException() {

        final Executable testMethod = () -> SortPomImplUtil.create()
                .testFiles("/Corrupt_file.xml", "/Corrupt_file.xml");

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat("Unexpected message", thrown.getMessage(), allOf(endsWith("content: <project><artifactId>sortpom</artifactId>"), startsWith("Could not sort ")));
    }

}
