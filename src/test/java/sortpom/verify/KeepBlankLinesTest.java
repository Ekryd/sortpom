package sortpom.verify;

import org.junit.Test;
import sortpom.util.XmlProcessorTestUtil;
import sortpom.verify.util.VerifyOrderFilesUtil;

public class KeepBlankLinesTest {
    @Test
    public final void emptyLinesInXmlShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .verifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    public final void emptyLinesInXmlShouldNotAffectVerify2() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .verifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    public final void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .indentBlankLines()
                .verifyXmlIsOrdered("src/test/resources/EmptyRow_input2.xml");
    }

    @Test
    public final void emptyLinesInXmlShouldNotAffectVerify3() throws Exception {
        VerifyOrderFilesUtil.create()
                .keepBlankLines()
                .verifyXmlIsOrdered("/EmptyRow_input2.xml");
    }

    @Test
    public final void emptyLinesInXmlAndIndentParameterShouldNotAffectVerify2() throws Exception {
        VerifyOrderFilesUtil.create()
                .keepBlankLines()
                .indentBLankLines()
                .verifyXmlIsOrdered("/EmptyRow_input2.xml");
    }

    @Test
    public final void unsortedXmlAndIndentParameterShouldAffectVerify() throws Exception {
        VerifyOrderFilesUtil.create()
                .keepBlankLines()
                .indentBLankLines()
                .verifyXmlIsNotOrdered("/EmptyRow_input.xml");
    }

    @Test
    public final void simpleLineBreaksShouldNotAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .verifyXmlIsOrdered("src/test/resources/LineBreak_input2.xml");
    }
    
    @Test
    public final void unsortedXmlShouldAffectVerify() throws Exception {
        XmlProcessorTestUtil.create()
                .keepBlankLines()
                .verifyXmlIsNotOrdered("src/test/resources/LineBreak_input.xml");
    }
}
