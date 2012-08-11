package sortpom.verify;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import sortpom.verify.util.VerifyOrderFilesUtil;

/**
 * @author bjorn
 * @since 2012-07-01
 */
public class VerifyOrderTest {
    @Test
    public final void sortedButUnformattedSortOrderShouldPass() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("recommended_2008_06")
                .testVerifyXmlIsOrdered("/Real2_input.xml");
    }

    @Test
    public final void wrongSortedShouldNotPass() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("custom_1")
                .testVerifyXmlIsNotOrdered("/Real2_input.xml", null);
    }

    @Test
    public void unsortedDefaultVerifyShouldPerformSort() throws Exception {
        VerifyOrderFilesUtil.create()
                .testVerifySort("/Real1_input.xml", "/Real1_expected.xml");
    }

    @Test
    public void unsortedSortVerifyShouldPerformSort() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyFail("sort")
                .testVerifySort("/Real1_input.xml", "/Real1_expected.xml");
    }

    @Test
    public void unsortedStopVerifyShouldPerformSort() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyFail("stop")
                .testVerifyFail("/Real1_input.xml", MojoFailureException.class);
    }

    @Test
    public void unsortedWarnVerifyShouldPerformSort() throws Exception {
        VerifyOrderFilesUtil.create()
                .verifyFail("warn")
                .testVerifyWarn("/Real1_input.xml", "The xml element <name> should be replaced with <version>");
    }

}
