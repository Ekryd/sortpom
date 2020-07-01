package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sortpom.sort.ExpandEmptyElementTest.createXmlFragment;

class LineSeparatorTest {
    @Test
    void formattingXmlWithNewlineShouldResultInOneLineBreakAtEnd() {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\n", false, true, false)
                .setIndent(2, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    @Test
    void formattingXmlWithCRShouldResultInOneLineBreakAtEnd() {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\r", false, true, false)
                .setIndent(2, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r<Gurka />\r", actual);
    }

    @Test
    void formattingXmlWithCRNLShouldResultInOneLineBreakAtEnd() {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\r\n", false, true, false)
                .setIndent(2, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Gurka />\r\n", actual);
    }

}
