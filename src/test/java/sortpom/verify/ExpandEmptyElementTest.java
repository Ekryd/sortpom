package sortpom.verify;

import org.junit.Test;
import sortpom.util.XmlProcessorTestUtil;

public class ExpandEmptyElementTest {
    @Test
    public void trueExpandedParameterAndtrueExpandedElementShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .expandEmptyElements(true)
                .verifyXmlIsOrdered("src/test/resources/ExpandedElement_input.xml");
    }

    @Test
    public void falseExpandedParameterAndtrueExpandedElementShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .expandEmptyElements(false)
                .verifyXmlIsOrdered("src/test/resources/ExpandedElement_input.xml");
    }

    @Test
    public void trueExpandedParameterAndfalseExpandedElementShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .expandEmptyElements(true)
                .verifyXmlIsOrdered("src/test/resources/ExpandedElementNot_input.xml");
    }

    @Test
    public void falseExpandedParameterAndfalseExpandedElementShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .expandEmptyElements(true)
                .verifyXmlIsOrdered("src/test/resources/ExpandedElementNot_input.xml");
    }

}
