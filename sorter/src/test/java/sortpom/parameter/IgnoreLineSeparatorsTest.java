package sortpom.parameter;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

public class IgnoreLineSeparatorsTest {

    @Test
    public final void ignoringLineSeparatorsShouldNotSort() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .ignoreLineSeparators(true)
                .testNoSorting("/ignore_line_separators_input.xml");
    }

    @Test
    public final void doNotIgnoreLineSeparatorsShouldSort() throws Exception {
        SortPomImplUtil.create()
                .lineSeparator("\n")
                .ignoreLineSeparators(false)
                .testFiles("/ignore_line_separators_input.xml", "/ignore_line_separators_output.xml");
    }

}
