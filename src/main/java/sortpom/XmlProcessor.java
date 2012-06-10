package sortpom;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import sortpom.jdomcontent.NewlineText;
import sortpom.util.BufferedLineSeparatorOutputStream;
import sortpom.util.LineSeparatorUtil;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperOperations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn Ekryd
 */
public class XmlProcessor {
    private final WrapperFactory factory;

    private Document originalDocument;
    private Document newDocument;
    private String encoding;
    private LineSeparatorUtil lineSeparatorUtil;
    private String indentCharacters;
    private boolean expandEmptyElements;
    private boolean indentBlankLines;

    public XmlProcessor(WrapperFactory factory) {
        this.factory = factory;
    }

    /**
     * Setup default configuration
     *
     * @throws MojoFailureException
     */
    public void setup(PluginParameters pluginParameters) throws MojoFailureException {
        this.indentCharacters = pluginParameters.indentCharacters;
        this.lineSeparatorUtil = new LineSeparatorUtil(pluginParameters.lineSeparator);
        this.encoding = pluginParameters.encoding;
        this.expandEmptyElements = pluginParameters.expandEmptyElements;
        this.indentBlankLines = pluginParameters.indentBlankLines;
    }

    /**
     * Returns the sorted xml as an OutputStream.
     *
     * @return the sorted xml
     * @throws IOException
     */
    public ByteArrayOutputStream getSortedXml() throws IOException {
        ByteArrayOutputStream sortedXml = new ByteArrayOutputStream();
        BufferedLineSeparatorOutputStream bufferedLineOutputStream = new BufferedLineSeparatorOutputStream(lineSeparatorUtil.toString(), sortedXml);

        XMLOutputter xmlOutputter = new PatchedXMLOutputter(bufferedLineOutputStream, indentBlankLines);
        xmlOutputter.setFormat(createPrettyFormat());
        xmlOutputter.output(newDocument, bufferedLineOutputStream);

        IOUtils.closeQuietly(bufferedLineOutputStream);
        return sortedXml;
    }

    private Format createPrettyFormat() {
        final Format prettyFormat = Format.getPrettyFormat();
        prettyFormat.setExpandEmptyElements(expandEmptyElements);
        prettyFormat.setEncoding(encoding);
        prettyFormat.setLineSeparator("\n");
        prettyFormat.setIndent(indentCharacters);
        return prettyFormat;
    }

    /**
     * Sets the original xml that should be sorted. Builds a dom document of the
     * xml.
     *
     * @param originalXml the new original xml
     * @throws JDOMException the jDOM exception
     * @throws IOException   Signals that an I/O exception has occurred.
     */
    public void setOriginalXml(final InputStream originalXml) throws JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        originalDocument = parser.build(originalXml);
    }

    /** Creates a new dom document that contains the sorted xml. */
    public void sortXml() {
        newDocument = (Document) originalDocument.clone();
        final Element rootElement = originalDocument.getRootElement();
        factory.initialize();
        WrapperOperations rootWrapper = factory.createFromRootElement(rootElement);
        rootWrapper.createWrappedStructure(factory);
        rootWrapper.detachStructure();
        rootWrapper.sortStructureAttributes();
        rootWrapper.sortStructureElements();
        newDocument.setRootElement((Element) rootWrapper.getWrappedStructure().get(0));
    }

    private static class PatchedXMLOutputter extends XMLOutputter {
        private final BufferedLineSeparatorOutputStream bufferedLineOutputStream;
        private final boolean indentBlankLines;

        public PatchedXMLOutputter(BufferedLineSeparatorOutputStream bufferedLineOutputStream, boolean indentBlankLines) {
            this.bufferedLineOutputStream = bufferedLineOutputStream;
            this.indentBlankLines = indentBlankLines;
        }

        /** Stop XMLOutputter from printing comment <!-- --> chars if it is just a newline */
        @Override
        protected void printComment(Writer stringWriter, Comment comment) throws IOException {
            if (comment instanceof NewlineText) {
                if (!indentBlankLines) {
                    clearIndentationForCurrentLine(stringWriter);
                }
            } else {
                super.printComment(stringWriter, comment);
            }
        }

        private void clearIndentationForCurrentLine(Writer stringWriter) throws IOException {
            // Force all xml lines to be written to stream (via the writer)
            stringWriter.flush();

            // Remove all inset that has just been written since last newline
            bufferedLineOutputStream.clearLineBuffer();
        }
    }
}
