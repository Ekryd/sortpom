package sortpom;

import org.jdom.*;
import org.junit.*;
import sortpom.util.*;

import static org.junit.Assert.*;

public class LineSeparatorTest {
	@Test
	public void formattingXmlWithNewlineShouldResultInOneLineBreakAtEnd() throws Exception {
		XmlProcessor xmlProcessor = new XmlProcessor(null);
		xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\n", "  ", false, false)
				.createPluginParameters());
		new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

		String actual = xmlProcessor.getSortedXml().toString("UTF-8");
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
	}

	@Test
	public void formattingXmlWithCRShouldResultInOneLineBreakAtEnd() throws Exception {
		XmlProcessor xmlProcessor = new XmlProcessor(null);
		xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\r", "  ", false, false)
				.createPluginParameters());
		new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

		String actual = xmlProcessor.getSortedXml().toString("UTF-8");
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r<Gurka />\r", actual);
	}

	@Test
	public void formattingXmlWithCRNLShouldResultInOneLineBreakAtEnd() throws Exception {
		XmlProcessor xmlProcessor = new XmlProcessor(null);
		xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\r\n", "  ", false, false)
				.createPluginParameters());
		new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

		String actual = xmlProcessor.getSortedXml().toString("UTF-8");
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<Gurka />\r\n", actual);
	}

	private Document createXmlFragment() {
		Document newDocument = new Document();
		newDocument.setRootElement(new Element("Gurka"));
		return newDocument;
	}

}
