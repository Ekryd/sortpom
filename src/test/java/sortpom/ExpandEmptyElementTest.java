package sortpom;

import org.jdom.*;
import org.junit.*;

import sortpom.util.*;

public class ExpandEmptyElementTest {
	@Test
	public void trueExpandedParameterShouldExpandEmptyXmlElements() throws Exception {
		XmlProcessor xmlProcessor = new XmlProcessor(null);
		xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\n", "  ", true)
				.createPluginParameters());
		new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

		String actual = xmlProcessor.getSortedXml().toString("UTF-8");
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka></Gurka>\n", actual);
	}

	@Test
	public void falseExpandedParameterShouldCompressEmptyXmlElements() throws Exception {
		XmlProcessor xmlProcessor = new XmlProcessor(null);
		xmlProcessor.setup(new PluginParametersBuilder().setFormatting("UTF-8", "\n", "  ", false)
				.createPluginParameters());
		new ReflectionHelper(xmlProcessor).setField("newDocument", createXmlFragment());

		String actual = xmlProcessor.getSortedXml().toString("UTF-8");
		Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Gurka />\n", actual);
	}

	private Document createXmlFragment() {
		Document newDocument = new Document();
		newDocument.setRootElement(new Element("Gurka"));
		return newDocument;
	}
}
