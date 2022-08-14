package sortpom.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.jupiter.api.Test;
import sortpom.parameter.PluginParameters;
import sortpom.util.FileUtil;
import sortpom.wrapper.operation.HierarchyRootWrapper;

class ElementToStringTest {
  @Test
  void testToString() throws Exception {
    String expected =
        new String(
            new FileInputStream("src/test/resources/Real1_expected_toString.txt").readAllBytes(),
            StandardCharsets.UTF_8);
    assertEquals(expected, getToStringOnRootElementWrapper());
  }

  private String getToStringOnRootElementWrapper() throws IOException, DocumentException {
    PluginParameters pluginParameters =
        PluginParameters.builder()
            .setPomFile(null)
            .setFileOutput(false, ".bak", null, false)
            .setEncoding("UTF-8")
            .setFormatting("\r\n", true, true, true)
            .setIndent(2, false, false)
            .setSortOrder("default_0_4_0.xml", null)
            .setSortEntities(
                "scope,groupId,artifactId",
                "groupId,artifactId",
                "scope,groupId,artifactId",
                "groupId,artifactId",
                true,
                true,
                true)
            .build();

    FileUtil fileUtil = new FileUtil();
    fileUtil.setup(pluginParameters);

    String xml =
        new String(
            new FileInputStream("src/test/resources/" + "Real1_input.xml").readAllBytes(),
            StandardCharsets.UTF_8);
    SAXReader parser = new SAXReader();
    Document document = parser.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);
    wrapperFactory.setup(pluginParameters);
    HierarchyRootWrapper rootWrapper =
        wrapperFactory.createFromRootElement(document.getRootElement());
    rootWrapper.createWrappedStructure(wrapperFactory);

    return rootWrapper.toString();
  }
}
