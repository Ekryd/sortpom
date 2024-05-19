package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static sortpom.sort.XmlFragment.createXmlFragment;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.output.XmlOutputGenerator;
import sortpom.util.FileUtil;
import sortpom.util.SortPomImplUtil;

class EncodingParameterTest {

  @Test
  void illegalEncodingWhenGettingPomFileShouldNotWork() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .encoding("gurka-2000")
                .testFiles("/Xml_deviations_input.xml", "/Xml_deviations_output.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Could not handle encoding: gurka-2000")));
  }

  @Test
  void illegalEncodingWhenGeneratingPomFileShouldWork() {
    var xmlOutputGenerator = new XmlOutputGenerator();
    xmlOutputGenerator.setup(
        PluginParameters.builder()
            .setEncoding("gurka-2000")
            .setFormatting("\n", true, true, false, true)
            .setIndent(2, false, false, null)
            .build());

    var actual = xmlOutputGenerator.getSortedXml(createXmlFragment());
    assertThat(actual, is("<?xml version=\"1.0\" encoding=\"gurka-2000\"?>\n<Gurka></Gurka>\n"));
  }

  @Test
  void illegalEncodingWhenSavingPomFileShouldNotWork() throws IOException {
    var pomFileTemp = File.createTempFile("pom", ".xml", new File("target"));
    pomFileTemp.deleteOnExit();

    var fileUtil = new FileUtil();
    var builder = PluginParameters.builder();

    builder.setPomFile(pomFileTemp);
    builder.setEncoding("gurka-2000");
    fileUtil.setup(builder.build());

    Executable testMethod =
        () ->
            fileUtil.savePomFile(
                "<?xml version=\"1.0\" encoding=\"gurka-2000\"?>\n<Gurka></Gurka>\n");

    var thrown = assertThrows(RuntimeException.class, testMethod);

    assertThat(thrown.getCause().getClass().getName(), is("java.io.UnsupportedEncodingException"));
    assertThat(thrown.getCause().getMessage(), is("gurka-2000"));
  }

  @Test
  void differentEncodingShouldWork1() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .encoding("UTF-32BE")
        .testFiles("/UTF32Encoding_input.xml", "/UTF32Encoding_expected.xml");
  }

  @Test
  void differentEncodingShouldWork2() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .encoding("UTF-16")
        .testFiles("/UTF16Encoding_input.xml", "/UTF16Encoding_expected.xml");
  }

  @Test
  void differentEncodingShouldWork3() {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .encoding("ISO-8859-1")
        .testFiles("/ISO88591Encoding_input.xml", "/ISO88591Encoding_expected.xml");
  }
}
