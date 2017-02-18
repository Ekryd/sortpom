package sortpom.sort;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Test;
import refutils.ReflectionHelper;
import sortpom.XmlProcessor;
import sortpom.parameter.PluginParametersBuilder;

public class ExpandEmptyElementTest {
    @Test
    public void trueExpandedParameterShouldExpandEmptyXmlElements() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", true, false, false)
                .setIndent(2, false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n", actual);
    }

    @Test
    public void falseExpandedParameterShouldCompressEmptyXmlElements() throws Exception {
        XmlProcessor xmlProcessor = new XmlProcessor(null);
        xmlProcessor.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", false, false, false)
                .setIndent(2, false)
                .createPluginParameters());
        new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

        String actual = xmlProcessor.getSortedXml().toString("UTF-8");
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    private Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }
}
