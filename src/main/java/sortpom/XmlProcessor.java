package sortpom;

import org.apache.commons.io.*;
import org.apache.maven.plugin.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import sortpom.jdomcontent.*;
import sortpom.util.*;
import sortpom.wrapper.*;

import java.io.*;

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
	}

	/**
	 * Returns the sorted xml as an OutputStream.
	 *
	 * @return the sorted xml
	 * @throws IOException
	 */
	public ByteArrayOutputStream getSortedXml() throws IOException {
		ByteArrayOutputStream sortedXml = new ByteArrayOutputStream();
		XMLOutputter xmlOutputter = new PatchedXMLOutputter();
		xmlOutputter.setFormat(createPrettyFormat());
		OutputStream outputStream = new LineSeparatorOutputStream(lineSeparatorUtil.toString(), sortedXml);
        xmlOutputter.output(newDocument, outputStream);
		IOUtils.closeQuietly(outputStream);
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
	 * @param originalXml
	 *            the new original xml
	 * @throws JDOMException
	 *             the jDOM exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void setOriginalXml(final InputStream originalXml) throws JDOMException, IOException {
		SAXBuilder parser = new SAXBuilder();
		originalDocument = parser.build(originalXml);
	}

	/**
	 * Creates a new dom document that contains the sorted xml.
	 */
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
        /** Stop XMLOutputter from printing comment <!-- --> chars if it is just a newline */
        @Override
        protected void printComment(Writer out, Comment comment) throws IOException {
            if (!(comment instanceof NewlineText)) {
                super.printComment(out, comment);
            }
        }
    }
}
