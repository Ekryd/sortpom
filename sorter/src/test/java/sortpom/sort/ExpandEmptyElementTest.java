package sortpom.sort;

import org.jdom.Document;
import org.jdom.Element;
import org.junit.jupiter.api.Test;
import sortpom.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpandEmptyElementTest {
    @Test
    void trueExpandedParameterShouldExpandEmptyXmlElements() {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\n", true, true, false)
                .setIndent(2, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n", actual);
    }

    @Test
    void falseExpandedParameterShouldCompressEmptyXmlElements() {
        XmlOutputGenerator xmlOutputGenerator = new XmlOutputGenerator();
        xmlOutputGenerator.setup(PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("\n", false, true, false)
                .setIndent(2, false)
                .build());

        String actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
    }

    public static Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }
}
