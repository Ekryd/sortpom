package sortpom.sort;

import org.jdom.Document;
import org.jdom.Element;

public class XmlFragment {
    public static Document createXmlFragment() {
        Document newDocument = new Document();
        newDocument.setRootElement(new Element("Gurka"));
        return newDocument;
    }

}
