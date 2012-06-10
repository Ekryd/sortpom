package sortpom;

import org.junit.Test;
import sortpom.util.XmlProcessorTestUtil;

public class KeepBlankLinesTest {
    @Test
    public final void emptyRowsInSimplePomShouldBePreserved() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .testInputAndExpected("src/test/resources/EmptyRow_input.xml", "src/test/resources/EmptyRow_expected.xml");
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreserved1() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .testInputAndExpected("src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected_keepBlankLines.xml");
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreservedAndIndented1() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .indentBlankLines()
                .testInputAndExpected("src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected_keepBlankLines_indented.xml");
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreserved2() throws Exception {
        SortOrderFilesUtil.create()
                .keepBlankLines()
                .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines.xml");
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreservedAndIndented2() throws Exception {
        SortOrderFilesUtil.create()
                .keepBlankLines()
                .indentBLankLines()
                .testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines_indented.xml");
    }

}
