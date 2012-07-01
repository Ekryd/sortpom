package sortpom.sort;

import org.junit.Test;
import sortpom.sort.util.XmlProcessorTestUtil;

public class BlankLinesTest {
    @Test
    public final void emptyRowsInSimplePomShouldBePreserved() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .testInputAndExpected("src/test/resources/LineBreak_input.xml", "src/test/resources/Character_expected.xml");
    }

}
