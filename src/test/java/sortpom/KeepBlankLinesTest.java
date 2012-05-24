package sortpom;

import org.junit.Test;

public class KeepBlankLinesTest {
    @Test
    public final void emptyRowsInSimplePomShouldBePreserved() throws Exception {
        new XmlProcessorTest().testInputAndExpected("src/test/resources/EmptyRow_input.xml", "src/test/resources/EmptyRow_expected.xml",
                false, true);
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreserved1() throws Exception {
        new XmlProcessorTest().testInputAndExpected("src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected_keepBlankLines.xml", false, true);
    }

    @Test
    public final void emptyRowsInLargePomShouldBePreserved2() throws Exception {
        SortOrderFilesUtil.testFiles("/Real1_input.xml", "/Real1_expected_keepBlankLines.xml",
                "default_0_4_0.xml", 2, false, false, "", "\r\n", false, true);
    }

}
