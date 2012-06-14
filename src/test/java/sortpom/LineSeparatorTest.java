package sortpom;

import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.util.ReflectionHelper;

import static org.junit.Assert.assertEquals;

public class LineSeparatorTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void formattingXmlWithNewlineShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\n", false, false)
                .setIndent("  ", false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    @Test
    public void formattingXmlWithCRShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\r", false, false)
                .setIndent("  ", false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r<Gurka />\r", actual);
    }

    @Test
    public void formattingXmlWithCRNLShouldResultInOneLineBreakAtEnd() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\r\n", false, false)
                .setIndent("  ", false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Gurka />\r\n", actual);
    }

    @Test
    public void lineSeparatorWithSomehtingElseShouldThrowException() throws Exception {
        thrown.expect(MojoFailureException.class);
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [42, 42, 42]");

        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "***", false, false)
                .setIndent("  ", false)
                .createPluginParameters());

    }

    private Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }

}
