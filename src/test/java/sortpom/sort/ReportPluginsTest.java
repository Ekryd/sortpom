package sortpom.sort;

import org.junit.Test;
import sortpom.util.SortPomImplUtil;

public class ReportPluginsTest {

    @Test
    public final void custom1ShouldWorkAsPredefinedSortOrder() throws Exception {
        SortPomImplUtil.create()
                .defaultOrderFileName("sortOrderFiles/custom_report_plugins.xml")
                .lineSeparator("\r\n")
                .sortPlugins("true")
                .testFiles("/ReportPlugins_input.xml",
                        "/ReportPlugins_expected.xml");
    }

}
