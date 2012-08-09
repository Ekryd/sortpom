package sortpom.verify;

import org.junit.Test;
import sortpom.util.XmlProcessorTestUtil;

public class XmlProcessorTest {

    @Test
    public final void testSortXmlAttributesShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create().verifyXmlIsOrdered(
                "src/test/resources/Attribute_expected.xml");
    }

    @Test
    public final void testSortXmlMultilineCommentShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create().verifyXmlIsOrdered(
                "src/test/resources/MultilineComment_expected.xml");
    }


    @Test
    public final void testSortXmlAttributesShouldAffectVerify() throws Exception {
        XmlProcessorTestUtil.create().verifyXmlIsNotOrdered(
                "src/test/resources/Attribute_input.xml");
    }

    @Test
    public final void testSortXmlMultilineCommentShouldAffectVerify() throws Exception {
        XmlProcessorTestUtil.create().verifyXmlIsNotOrdered(
                "src/test/resources/MultilineComment_input.xml");
    }
}
