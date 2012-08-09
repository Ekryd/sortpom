package sortpom.verify;

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
                .verifyXmlIsOrdered("/Real2_input.xml");
    }

    @Test
    public final void wrongSortedShouldNotPass() throws Exception {
        VerifyOrderFilesUtil.create()
                .predefinedSortOrder("custom_1")
                .verifyXmlIsNotOrdered("/Real2_input.xml");
    }

}
