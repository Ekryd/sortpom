package sortpom.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.parameter.PluginParameters;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author bjorn
 * @since 2013-08-16
 */
public class FileUtilTest {
    @Test
    public void defaultSortOrderFromFileShouldWork() throws Exception {
        FileUtil fileUtil = createFileUtil("Attribute_expected.xml");

        String defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
        assertThat(defaultSortOrderXml, startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                "<projec"));
    }

    @Test
    public void defaultSortOrderFromNonExistingShouldThrowException() {
        FileUtil fileUtil = createFileUtil("zzz_Attribute_expected.xml");

        final Executable testMethod = () -> fileUtil.getDefaultSortOrderXml();

        final IOException thrown = assertThrows(IOException.class, testMethod);

        assertThat(thrown.getMessage(), startsWith("Could not find"));
        assertThat(thrown.getMessage(), endsWith("or zzz_Attribute_expected.xml in classpath"));
    }

    @Test
    public void defaultSortOrderFromUrlShouldWork() throws IOException {
        FileUtil fileUtil = createFileUtil("https://en.wikipedia.org/wiki/Sweden");

        try {
        String defaultSortOrderXml = fileUtil.getDefaultSortOrderXml();
            assertThat(defaultSortOrderXml, containsString("Sverige"));
        } catch (UnknownHostException e) {
            // This is ok, we were not online when the test was perfomed
            // Which actually makes this test a bit pointless :-(
        }
    }

    @Test
    @Disabled
    public void defaultSortOrderFromNonExistingHostShouldThrowException() {
        FileUtil fileUtil = createFileUtil("http://jgerwzuujy.fjrmzaxklj.zfgmqavbhp/licenses/BSD-3-Clause");

        final Executable testMethod = () -> fileUtil.getDefaultSortOrderXml();

        final UnknownHostException thrown = assertThrows(UnknownHostException.class, testMethod);

        assertThat(thrown.getMessage(), is("jgerwzuujy.fjrmzaxklj.zfgmqavbhp"));
    }

    @Test
    public void defaultSortOrderFromNonExistingPageShouldThrowException() throws IOException {
        FileUtil fileUtil = createFileUtil("https://github.com/Ekryd/sortpom/where_are_the_donations");

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
        FileUtil fileUtil = new FileUtil();
        PluginParameters pluginParameters = PluginParameters.builder()
                .setSortOrder(customSortOrderFile, null)
                .setEncoding("UTF-8")
                .build();
        fileUtil.setup(pluginParameters);
        return fileUtil;
    }
}
