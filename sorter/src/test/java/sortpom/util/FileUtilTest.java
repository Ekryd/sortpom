package sortpom.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import org.junit.jupiter.api.Test;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2013-08-16
 */
class FileUtilTest {
  @Test
  void defaultSortOrderFromFileShouldWork() throws Exception {
    var fileUtil = createFileUtil("Attribute_expected.xml");

    var defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
    assertThat(
        defaultSortOrderXml,
        startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<projec"));
  }

  @Test
  void defaultSortOrderFromNonExistingShouldThrowException() {
    var fileUtil = createFileUtil("zzz_Attribute_expected.xml");

    var thrown = assertThrows(IOException.class, fileUtil::getDefaultSortOrderXml);

    assertThat(thrown.getMessage(), startsWith("Could not find"));
    assertThat(thrown.getMessage(), endsWith("or zzz_Attribute_expected.xml in classpath"));
  }

  @Test
  void defaultSortOrderFromUrlShouldWork() throws IOException {
    var fileUtil = createFileUtil("https://spaceweather.com/");

    try {
      var defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
      assertThat(defaultSortOrderXml, containsString("spaceweather"));
    } catch (UnknownHostException e) {
      // This is ok, we were not online when the test was performed
      // Which actually makes this test a bit pointless :-(
    }
  }

  @Test
  void defaultSortOrderFromNonExistingHostShouldThrowException() {
    var fileUtil = createFileUtil("http://jgerwzuujy.fjrmzaxklj.zfgmqavbhp/licenses/BSD-3-Clause");

    var thrown = assertThrows(UnknownHostException.class, fileUtil::getDefaultSortOrderXml);

    assertThat(thrown.getMessage(), is("jgerwzuujy.fjrmzaxklj.zfgmqavbhp"));
  }

  @Test
  void defaultSortOrderFromNonExistingPageShouldThrowException() throws IOException {
    var fileUtil = createFileUtil("https://github.com/Ekryd/sortpom/where_are_the_donations");

    try {
      fileUtil.getDefaultSortOrderXml();
      fail();
    } catch (UnknownHostException e) {
      // This is ok, we were not online when the test was performed
    } catch (FileNotFoundException e) {
      assertThat(e.getMessage(), is("https://github.com/Ekryd/sortpom/where_are_the_donations"));
    }
  }

  private FileUtil createFileUtil(String customSortOrderFile) {
    var fileUtil = new FileUtil();
    var pluginParameters =
        PluginParameters.builder()
            .setSortOrder(customSortOrderFile, null)
            .setEncoding("UTF-8")
            .build();
    fileUtil.setup(pluginParameters);
    return fileUtil;
  }
}
