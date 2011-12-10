package sortpom;

import java.io.*;

import org.apache.commons.io.*;
import org.apache.maven.plugin.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;

import sortpom.util.*;
import sortpom.wrapper.*;

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
	 * Returns the sorted xml as an outputstream.
	 * 
	 * @param sortedXml
	 *            the sorted xml
	 * @return
	 * @return the sorted xml
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public ByteArrayOutputStream getSortedXml() throws IOException {
		ByteArrayOutputStream sortedXml = new ByteArrayOutputStream();
		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(createPrettyFormat());
		OutputStream outputStream = new NewlineOutputStream(lineSeparatorUtil.toString(), sortedXml);
		outputter.output(newDocument, outputStream);
		IOUtils.closeQuietly(outputStream);
		return sortedXml;
	}

	private Format createPrettyFormat() {
		final Format prettyFormat = Format.getPrettyFormat();
		prettyFormat.setExpandEmptyElements(expandEmptyElements);
		prettyFormat.setEncoding(encoding);
		prettyFormat.setLineSeparator(lineSeparatorUtil.toString());
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
		WrapperOperations rootWrapper = factory.create(rootElement);
		rootWrapper.createWrappedStructure(factory);
		rootWrapper.detachStructure();
		rootWrapper.sortStructureAttributes();
		rootWrapper.sortStructureElements();
		newDocument.setRootElement((Element) rootWrapper.getWrappedStructure().get(0));
	}

}
