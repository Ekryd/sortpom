package sortpom.wrapper;

import org.apache.commons.io.IOUtils;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.Test;
import sortpom.parameter.PluginParameters;
import sortpom.util.FileUtil;
import sortpom.wrapper.operation.HierarchyRootWrapper;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author bjorn
 * @since 2012-06-13
 */
public class ElementToStringTest {
    private static final String UTF_8 = "UTF-8";

    @Test
    public void testToString() throws Exception {
        String expected = IOUtils.toString(new FileInputStream("src/test/resources/Real1_expected_toString.txt"), UTF_8);
        assertEquals(expected, getToStringOnRootElementWrapper("Real1_input.xml"));
    }

    private String getToStringOnRootElementWrapper(String inputFileName) throws IOException, JDOMException {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setPomFile(null).setFileOutput(false, ".bak", null)
                .setEncoding("UTF-8")
                .setFormatting("\r\n", true, true)
                .setIndent(2, false)
                .setSortOrder("default_0_4_0.xml", null)
                .setSortEntities("scope,groupId,artifactId", "groupId,artifactId", true, true).build();

        FileUtil fileUtil = new FileUtil();
        fileUtil.setup(pluginParameters);

        String xml = IOUtils.toString(new FileInputStream("src/test/resources/" + inputFileName), UTF_8);
        SAXBuilder parser = new SAXBuilder();
        Document document = parser.build(new ByteArrayInputStream(xml.getBytes(UTF_8)));

        WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);
        wrapperFactory.setup(pluginParameters);
        HierarchyRootWrapper rootWrapper = wrapperFactory.createFromRootElement(document.getRootElement());
        rootWrapper.createWrappedStructure(wrapperFactory);

        return rootWrapper.toString();
    }

}
