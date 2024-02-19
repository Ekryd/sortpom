package sortpom;

import java.io.InputStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
import sortpom.util.XmlOrderedResult;
import sortpom.verify.ElementComparator;
import sortpom.wrapper.operation.WrapperFactory;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn Ekryd
 */
public class XmlProcessor {
  // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
  public static final String DISALLOW_DOCTYPE_DECL =
      "http://apache.org/xml/features/disallow-doctype-decl";
  private final WrapperFactory factory;

  private Document originalDocument;
  private Document newDocument;

  public XmlProcessor(WrapperFactory factory) {
    this.factory = factory;
  }

  /**
   * Sets the original xml that should be sorted. Builds a dom document of the xml.
   *
   * @param originalXml the new original xml
   */
  public void setOriginalXml(InputStream originalXml) throws DocumentException, SAXException {
    var parser = new SAXReader();
    parser.setFeature(DISALLOW_DOCTYPE_DECL, true);
    parser.setMergeAdjacentText(true);
    originalDocument = parser.read(originalXml);
  }

  /** Creates a new dom document that contains the sorted xml. */
  public void sortXml() {
    newDocument = (Document) originalDocument.clone();
    var rootElement = originalDocument.getRootElement().createCopy();

    var rootWrapper = factory.createFromRootElement(rootElement);

    rootWrapper.createWrappedStructure(factory);
    rootWrapper.detachStructure();
    rootWrapper.sortStructureAttributes();
    rootWrapper.sortStructureElements();
    rootWrapper.connectXmlStructure();

    replaceRootElementInNewDocument(rootWrapper.getElementContent().getContent());
  }

  /** Setting root element directly on the document will clear other content */
  private void replaceRootElementInNewDocument(Element newElement) {
    var rootElement = newDocument.getRootElement();
    var content = newDocument.content();

    newDocument.clearContent();

    for (var node : content) {
      if (node == rootElement) {
        newDocument.add(newElement);
      } else {
        newDocument.add(node);
      }
    }
  }

  public Document getNewDocument() {
    return newDocument;
  }

  public XmlOrderedResult isXmlOrdered() {
    var elementComparator =
        new ElementComparator(originalDocument.getRootElement(), newDocument.getRootElement());
    return elementComparator.isElementOrdered();
  }
}
