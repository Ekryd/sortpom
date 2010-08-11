package sortpom;

import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import sortpom.util.FileUtil;
import sortpom.util.LineSeparator;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn Ekryd
 */
public class XmlProcessor {

    private Document document;

    private final WrapperFactory factory;

    private final FileUtil fileUtil;

    private Document newDocument;

    /**
     * Instantiates a new xml processor.
     *
     * @param factory the factory
     * @param fileUtil the file util
     */
    public XmlProcessor(WrapperFactory factory, FileUtil fileUtil) {
        this.factory = factory;
        this.fileUtil = fileUtil;
    }

    /**
     * Puts the sorted xml on the outputstream. XXX: This is a mighty sucky implementation.
     *
     * @param lineSeparator the line separator
     * @param indent the indent
     * @param sortedXml the sorted xml
     * @return the sorted xml
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void getSortedXml(final LineSeparator lineSeparator, final String indent, final OutputStream sortedXml) throws IOException {
        XMLOutputter outputter = new XMLOutputter();
        final Format prettyFormat = Format.getPrettyFormat();
        prettyFormat.setExpandEmptyElements(true);
        prettyFormat.setEncoding(fileUtil.getEncoding());
        prettyFormat.setLineSeparator(lineSeparator.toString());
        prettyFormat.setIndent(indent);
        outputter.setFormat(prettyFormat);
        OutputStream outputStream = new NewlineOutputStream(lineSeparator.toString(), sortedXml);
        outputter.output(newDocument, outputStream);
        IOUtils.closeQuietly(outputStream);
    }


    /**
     * Sets the original xml that should be sorted. Builds a dom document of the xml.
     *
     * @param originalXml the new original xml
     * @throws JDOMException the jDOM exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void setOriginalXml(final InputStream originalXml) throws JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        document = parser.build(originalXml);
    }


    /**
     * Creates a new dom document that contains the sorted xml.
     */
    public void sortXml() {
        newDocument = (Document) document.clone();
        final Element rootElement = document.getRootElement();
        factory.initialize();
        WrapperOperations rootWrapper = factory.create(rootElement);
        rootWrapper.createWrappedStructure(factory);
        rootWrapper.detachStructure();
        rootWrapper.sortStructureAttributes();
        rootWrapper.sortStructureElements();
        newDocument.setRootElement((Element) rootWrapper.getWrappedStructure().get(0));
    }

}
