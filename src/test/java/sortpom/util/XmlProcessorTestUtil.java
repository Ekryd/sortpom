package sortpom.util;

import org.apache.commons.io.IOUtils;
import org.jdom.Content;
import org.jdom.Element;
import sortpom.PluginParameters;
import sortpom.PluginParametersBuilder;
import sortpom.XmlProcessor;
import sortpom.wrapper.AlphabeticalSortedWrapper;
import sortpom.wrapper.GroupWrapper;
import sortpom.wrapper.UnsortedWrapper;
import sortpom.wrapper.Wrapper;
import sortpom.wrapper.WrapperFactory;
import sortpom.wrapper.WrapperFactoryImpl;
import sortpom.wrapper.WrapperOperations;

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

    public static XmlProcessorTestUtil create() {
        return new XmlProcessorTestUtil();
    }

    private XmlProcessorTestUtil() {
    }

    public void testInputAndExpected(final String inputFileName, final String expectedFileName) throws Exception {
        PluginParameters pluginParameters = new PluginParametersBuilder().setPomFile(null).setBackupInfo(false, ".bak")
                .setFormatting("UTF-8", "\r\n", true, keepBlankLines)
                .setIndent("  ", indentBlankLines)
                .setSortOrder("default_0_4_0.xml", null)
                .setSortEntities(false, false, false).createPluginParameters();
        final String xml = IOUtils.toString(new FileInputStream(inputFileName), UTF_8);
        final FileUtil fileUtil = new FileUtil();
        WrapperFactory wrapperFactory = new WrapperFactoryImpl(fileUtil);
        ((WrapperFactoryImpl) wrapperFactory).setup(pluginParameters);
        final XmlProcessor xmlProcessor = new XmlProcessor(wrapperFactory);
        fileUtil.setup(pluginParameters);
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

                @Override
                public void initialize() {
                }

            };
        } else {
            new ReflectionHelper(wrapperFactory).setField(fileUtil);
        }
        wrapperFactory.initialize();
        new ReflectionHelper(xmlProcessor).setField(wrapperFactory);
        xmlProcessor.setOriginalXml(new ByteArrayInputStream(xml.getBytes(UTF_8)));
        xmlProcessor.sortXml();
        final ByteArrayOutputStream sortedXmlOutputStream = xmlProcessor.getSortedXml();
        final String expected = IOUtils.toString(new FileInputStream(expectedFileName), UTF_8);
        String actual = sortedXmlOutputStream.toString(UTF_8);
        assertEquals(expected, actual);
    }

    public XmlProcessorTestUtil sortAlfabeticalOnly() {
        sortAlfabeticalOnly = true;
        return this;
    }

    public XmlProcessorTestUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public XmlProcessorTestUtil indentBlankLines() {
        indentBlankLines = true;
        return this;
    }
}
