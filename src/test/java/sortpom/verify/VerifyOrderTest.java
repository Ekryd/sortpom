package sortpom.verify;

import org.junit.Test;
import sortpom.verify.util.VerifyOrderFilesUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author bjorn
 * @since 2012-07-01
 */
public class VerifyOrderTest {
    @Test
    public final void sortedButUnformattedSortOrderShouldPass() throws Exception {
        assertTrue(VerifyOrderFilesUtil.create()
                .predefinedSortOrder("recommended_2008_06")
                .isPomSorted("/Real2_input.xml"));
    }

    @Test
    public final void wrongSortedShouldNotPass() throws Exception {
        assertFalse(VerifyOrderFilesUtil.create()
                .predefinedSortOrder("custom_1")
                .isPomSorted("/Real2_input.xml"));
    }

}
