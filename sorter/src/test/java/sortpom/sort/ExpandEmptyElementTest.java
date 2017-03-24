package sortpom.sort;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Test;
import sortpom.XmlOutputGenerator;
import sortpom.parameter.PluginParametersBuilder;

public class ExpandEmptyElementTest {
    @Test
    public void trueExpandedParameterShouldExpandEmptyXmlElements() throws Exception {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", true, false)
                .setIndent(2, false)
                .createPluginParameters());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n", actual);
    }

    @Test
    public void falseExpandedParameterShouldCompressEmptyXmlElements() throws Exception {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("\n", false, false)
                .setIndent(2, false)
                .createPluginParameters());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    public static Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }
}
