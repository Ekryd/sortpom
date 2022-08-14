package sortpom.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import sortpom.parameter.PluginParameters;
import sortpom.util.FileUtil;
import sortpom.wrapper.operation.HierarchyRootWrapper;

class CustomSortOrderFileTest {
  @Test
  void compareDefaultSortOrderFileToString() throws Exception {
    String expected =
        new String(
            new FileInputStream("src/test/resources/sortOrderFiles/with_newline_tagsToString.txt")
                .readAllBytes(),
            StandardCharsets.UTF_8);
    assertEquals(expected, getToStringOnCustomSortOrderFile());
  }

  private String getToStringOnCustomSortOrderFile()
      throws IOException, DocumentException, SAXException {
    PluginParameters pluginParameters =
        PluginParameters.builder()
            .setPomFile(null)
            .setFileOutput(false, ".bak", null, false)
            .setEncoding("UTF-8")
            .setFormatting("\r\n", true, true, true)
            .setIndent(2, false, false)
            .setSortOrder("src/test/resources/sortOrderFiles/with_newline_tags.xml", null)
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

    WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);

    Document documentFromDefaultSortOrderFile =
        wrapperFactory.createDocumentFromDefaultSortOrderFile();
    new HierarchyRootWrapper(
        wrapperFactory.create(documentFromDefaultSortOrderFile.getRootElement()));

    HierarchyRootWrapper rootWrapper =
        new HierarchyRootWrapper(
            wrapperFactory.create(documentFromDefaultSortOrderFile.getRootElement()));
    rootWrapper.createWrappedStructure(wrapperFactory);

    return rootWrapper.toString();
  }
}
