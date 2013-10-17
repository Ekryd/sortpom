package sortpom.sort;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;
import refutils.ReflectionHelper;
import sortpom.XmlProcessor;
import sortpom.parameter.PluginParametersBuilder;

import static org.junit.Assert.assertEquals;

public class LineSeparatorTest {
    @Test
    public void formattingXmlWithNewlineShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", false, false)
                .setIndent(2, false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    @Test
    public void formattingXmlWithCRShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\r", false, false)
                .setIndent(2, false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r<Gurka />\r", actual);
    }

    @Test
    public void formattingXmlWithCRNLShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\r\n", false, false)
                .setIndent(2, false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Gurka />\r\n", actual);
    }

    private Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }

}
