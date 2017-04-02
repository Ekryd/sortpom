package sortpom;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import sortpom.util.XmlOrderedResult;
import sortpom.verify.ElementComparator;
import sortpom.wrapper.operation.HierarchyRootWrapper;
import sortpom.wrapper.operation.WrapperFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn Ekryd
 */
public class XmlProcessor {
    private final WrapperFactory factory;

    private Document originalDocument;
    private Document newDocument;

    public XmlProcessor(WrapperFactory factory) {
        this.factory = factory;
    }

    /**
     * Sets the original xml that should be sorted. Builds a dom document of the
     * xml.
     *
     * @param originalXml the new original xml
     * @throws org.jdom.JDOMException the jDOM exception
     * @throws java.io.IOException    Signals that an I/O exception has occurred.
     */
    public void setOriginalXml(final InputStream originalXml) throws JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        originalDocument = parser.build(originalXml);
    }

    /** Creates a new dom document that contains the sorted xml. */
    public void sortXml() {
        newDocument = (Document) originalDocument.clone();
        final Element rootElement = (Element) originalDocument.getRootElement().clone();

        HierarchyRootWrapper rootWrapper = factory.createFromRootElement(rootElement);

        rootWrapper.createWrappedStructure(factory);
        rootWrapper.detachStructure();
        rootWrapper.sortStructureAttributes();
        rootWrapper.sortStructureElements();
        rootWrapper.connectXmlStructure();

        newDocument.setRootElement(rootWrapper.getElementContent().getContent());
    }

    public Document getNewDocument() {
        return newDocument;
    }

    public XmlOrderedResult isXmlOrdered() {
        ElementComparator elementComparator = new ElementComparator(originalDocument.getRootElement(), newDocument.getRootElement());
        return elementComparator.isElementOrdered();
    }

}
