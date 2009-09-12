package sortpom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import se.mine.mindif.Dependency;
import sortpom.util.FileUtil;
import sortpom.util.LineSeparator;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperFactoryImpl;
import sortpom.wrapper.WrapperOperations;

/**
 * Creates xml structure and sorts it.
 *
 * @author Bjorn
 *
 */
public class XmlProcessor {
	private Document document;

	@Dependency(WrapperFactoryImpl.class)
	private WrapperFactory factory;

	@Dependency
	private FileUtil fileUtil;

	private Document newDocument;

	public void getSortedXml(final LineSeparator lineSeparator, final OutputStream sortedXml) throws IOException {
		XMLOutputter outputter = new XMLOutputter();
		final Format prettyFormat = Format.getPrettyFormat();
		prettyFormat.setExpandEmptyElements(true);
		prettyFormat.setEncoding(fileUtil.getEncoding());
		prettyFormat.setLineSeparator(lineSeparator.toString());
		outputter.setFormat(prettyFormat);
		OutputStream outputStream = new NewlineOutputStream(lineSeparator.toString(), sortedXml);
		outputter.output(newDocument, outputStream);
	}

	public void setOriginalXml(final InputStream originalXml) throws JDOMException, IOException {
		SAXBuilder parser = new SAXBuilder();
		document = parser.build(originalXml);
	}

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
