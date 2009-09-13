package sortpom;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Element;

import sortpom.util.FileUtil;
import sortpom.util.LineSeparator;
import sortpom.util.ReflectionHelper;
import sortpom.wrapper.AlfabeticalSortedWrapper;
import sortpom.wrapper.GroupWrapper;
import sortpom.wrapper.UnsortedWrapper;
import sortpom.wrapper.Wrapper;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperFactoryImpl;
import sortpom.wrapper.WrapperOperations;

public class XmlProcessorTest extends TestCase {

	private static final String UTF_8 = "UTF-8";

	public final void testSortXmlAttributes() throws Exception {
		testInputAndExpected("src/test/resources/Attribute_input.xml", "src/test/resources/Attribute_expected.xml",
				false);
	}

	public final void testSortXmlCharacter() throws Exception {
		testInputAndExpected("src/test/resources/Character_input.xml", "src/test/resources/Character_expected.xml",
				false);
	}

	public final void testSortXmlComplex() throws Exception {
		testInputAndExpected("src/test/resources/Complex_input.xml", "src/test/resources/Complex_expected.xml", false);
	}

	public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
		testInputAndExpected("src/test/resources/full_alfa_input.xml", "src/test/resources/full_expected.xml", false);
	}

	public final void testSortXmlFullToAlfabetical() throws Exception {
		testInputAndExpected("src/test/resources/full_unsorted_input.xml", "src/test/resources/full_alfa_input.xml",
				true);
	}

	public final void testSortXmlMultilineComment() throws Exception {
		testInputAndExpected("src/test/resources/MultilineComment_input.xml",
				"src/test/resources/MultilineComment_expected.xml", false);
	}

	public final void testSortXmlReal1() throws Exception {
		testInputAndExpected("src/test/resources/Real1_input.xml", "src/test/resources/Real1_expected.xml", false);
	}

	public final void testSortXmlSimple() throws Exception {
		testInputAndExpected("src/test/resources/Simple_input.xml", "src/test/resources/Simple_expected.xml", false);
	}

	private void testInputAndExpected(final String inputFileName, final String expectedFileName,
			final boolean sortAlfabeticalOnly) throws Exception {
		final String xml = IOUtils.toString(new FileInputStream(inputFileName), UTF_8);
		final XmlProcessor xmlProcessor = new XmlProcessor();
		final FileUtil fileUtil = new FileUtil();
		fileUtil.setup(null, null, "UTF-8", null);
		WrapperFactory wrapperFactory = new WrapperFactoryImpl();
		if (sortAlfabeticalOnly) {
			wrapperFactory = new WrapperFactory() {

				@Override
				public WrapperOperations create(final Element rootElement) {
					return new GroupWrapper(new AlfabeticalSortedWrapper(rootElement));
				}

				@SuppressWarnings("unchecked")
				@Override
				public <T extends Content> Wrapper<T> create(final T content) {
					if (content instanceof Element) {
						Element element = (Element) content;
						return (Wrapper<T>) new AlfabeticalSortedWrapper(element);
					}
					return new UnsortedWrapper<T>(content);
				}

				@Override
				public void initialize() {
				}

			};
		} else {
			new ReflectionHelper(wrapperFactory).setField(fileUtil);
		}
		wrapperFactory.initialize();
		new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
		new ReflectionHelper(xmlProcessor).setField(fileUtil);
		xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(UTF_8)));
		xmlProcessor.sortXml();
		final ByteArrayOutputStream sortedXmlOutputStream = new ByteArrayOutputStream();
		LineSeparator lineSeparator = new LineSeparator("\r\n");
		xmlProcessor.getSortedXml(lineSeparator, sortedXmlOutputStream);
		final String expected = IOUtils.toString(new FileInputStream(expectedFileName), UTF_8);
		String actual = sortedXmlOutputStream.toString(UTF_8);
		// final int startIndex = 280;
		// final int endIndex = 320;
		// final byte[] expectedBytes = expected.substring(startIndex,
		// endIndex).getBytes();
		// for (byte b : expectedBytes) {
		// System.out.print(b + " ");
		// }
		// System.out.println();
		// final byte[] actualBytes = actual.substring(startIndex,
		// endIndex).getBytes();
		// for (byte b : actualBytes) {
		// System.out.print(b + " ");
		// }
		// System.out.println();
		assertEquals(expected, actual);
	}
}
