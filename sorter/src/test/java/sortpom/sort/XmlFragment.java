package sortpom.sort;

import org.dom4j.Document;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultDocument;

public class XmlFragment {
  public static Document createXmlFragment() {
    Document newDocument = new DefaultDocument();
    newDocument.setRootElement(new BaseElement("Gurka"));
    return newDocument;
  }

  public static Document createXmlProjectFragment() {
    var xsi = Namespace.get("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    var rootElement = new BaseElement("project");
    rootElement.setNamespace(Namespace.get("http://maven.apache.org/POM/4.0.0"));
    rootElement.addAttribute(
        new QName("schemaLocation", xsi),
        "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd");
    rootElement.add(xsi);
    rootElement.add(new BaseElement("Gurka"));

    var defaultDocument = new DefaultDocument();
    defaultDocument.setRootElement(rootElement);
    return defaultDocument;
  }
}
