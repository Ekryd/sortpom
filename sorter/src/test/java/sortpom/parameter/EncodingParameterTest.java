package sortpom.parameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.util.SortPomImplUtil;

public class EncodingParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void illegalEncodingShouldNotWork() throws Exception {
        thrown.expectMessage("gurka-2000");

        SortPomImplUtil.create()
                .encoding("gurka-2000")
                .testFiles("/Xml_deviations_input.xml", "/Xml_deviations_output.xml");
    }


}
