package sortpom.util;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Element;
import refutils.ReflectionHelper;
import sortpom.XmlProcessor;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.wrapper.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

/**
 * @author bjorn
 * @since 2012-06-10
 */
public class XmlProcessorTestUtil {
    private static final String UTF_8 = "UTF-8";
    private boolean sortAlfabeticalOnly = false;
    private boolean keepBlankLines = false;
    private boolean indentBlankLines = false;
    private String predefinedSortOrder = "default_0_4_0";
    private boolean expandEmptyElements = true;
    private String lineSeparator = "\r\n";

    public static XmlProcessorTestUtil create() {
        return new XmlProcessorTestUtil();
    }

    private XmlProcessorTestUtil() {
    }

    public void testInputAndExpected(final String inputFileName, final String expectedFileName) throws Exception {
        String actual = sortXmlAndReturnResult(inputFileName);

        final String expected = IOUtils.toString(new FileInputStream(expectedFileName), UTF_8);

        assertEquals(expected, actual);
    }

    public String sortXmlAndReturnResult(String inputFileName) throws Exception {
        final XmlProcessor xmlProcessor = setup(inputFileName);
        xmlProcessor.sortXml();
        final ByteArrayOutputStream sortedXmlOutputStream = xmlProcessor.getSortedXml();
        return sortedXmlOutputStream.toString(UTF_8);
    }

    public void testVerifyXmlIsOrdered(final String inputFileName) throws Exception {
        final XmlProcessor xmlProcessor = setup(inputFileName);
        xmlProcessor.sortXml();
        assertEquals(true, xmlProcessor.isXmlOrdered().isOrdered());
    }

    public void testVerifyXmlIsNotOrdered(final String inputFileName, String infoMessage) throws Exception {
        final XmlProcessor xmlProcessor = setup(inputFileName);
        xmlProcessor.sortXml();
        XmlOrderedResult xmlOrdered = xmlProcessor.isXmlOrdered();
        assertEquals(false, xmlOrdered.isOrdered());
        assertEquals(infoMessage, xmlOrdered.getMessage());
    }

    private XmlProcessor setup(String inputFileName) throws Exception {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setPomFile(null)
                .setBackupInfo(false, ".bak")
                .setEncoding("UTF-8")
                .setFormatting(lineSeparator, expandEmptyElements, keepBlankLines)
                .setIndent(2, indentBlankLines)
                .setSortOrder(predefinedSortOrder + ".xml", null)
                .setSortEntities("", "", false).createPluginParameters();
        final String xml = IOUtils.toString(new FileInputStream(inputFileName), UTF_8);

        final FileUtil fileUtil = new FileUtil();
        fileUtil.setup(pluginParameters);

        WrapperFactory wrapperFactory = new WrapperFactoryImpl(fileUtil);
        ((WrapperFactoryImpl) wrapperFactory).setup(pluginParameters);

        final XmlProcessor xmlProcessor = new XmlProcessor(wrapperFactory);
        xmlProcessor.setup(pluginParameters);

        if (sortAlfabeticalOnly) {
            wrapperFactory = new WrapperFactory() {

                @Override
                public WrapperOperations createFromRootElement(final Element rootElement) {
                    return new GroupWrapper(new AlphabeticalSortedWrapper(rootElement));
                }

                @SuppressWarnings("unchecked")
                @Override
                public <T extends Content> Wrapper<T> create(final T content) {
                    if (content instanceof Element) {
                        Element element = (Element) content;
                        return (Wrapper<T>) new AlphabeticalSortedWrapper(element);
                    }
                    return new UnsortedWrapper<T>(content);
                }

            };
        } else {
            new ReflectionHelper(wrapperFactory).setField(fileUtil);
        }
        new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
        xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(UTF_8)));
        return xmlProcessor;
    }

    public XmlProcessorTestUtil sortAlfabeticalOnly() {
        sortAlfabeticalOnly = true;
        return this;
    }

    public XmlProcessorTestUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public XmlProcessorTestUtil lineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public XmlProcessorTestUtil indentBlankLines() {
        indentBlankLines = true;
        return this;
    }

    public XmlProcessorTestUtil predefinedSortOrder(String predefinedSortOrder) {
        this.predefinedSortOrder = predefinedSortOrder;
        return this;
    }

    public XmlProcessorTestUtil expandEmptyElements(boolean expand) {
        this.expandEmptyElements = expand;
        return this;
    }
}
