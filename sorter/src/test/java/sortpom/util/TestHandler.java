package sortpom.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import sortpom.SortPomImpl;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class TestHandler {
    static final String UTF_8 = "UTF-8";

    private final SortPomImpl sortPomImpl = new SortPomImpl();

    private final List<String> infoLogger = new ArrayList<String>();
    private final String inputResourceFileName;
    private final String expectedResourceFileName;
    private final File testpom;
    private final PluginParameters pluginParameters;

    private FileInputStream backupFileInputStream = null;
    private FileInputStream originalPomInputStream = null;
    private FileInputStream actualSortedPomInputStream = null;
    private FileInputStream expectedSortedPomInputStream = null;
    private final File backupFile;

    public TestHandler(String inputResourceFileName, PluginParameters pluginParameters) {
        this(inputResourceFileName, null, pluginParameters);
    }

    public TestHandler(String inputResourceFileName, String expectedResourceFileName, PluginParameters pluginParameters) {
        this.inputResourceFileName = inputResourceFileName;
        this.expectedResourceFileName = expectedResourceFileName;
        this.pluginParameters = pluginParameters;
        this.testpom = pluginParameters.pomFile;
        backupFile = new File(testpom.getAbsolutePath() + pluginParameters.backupFileExtension);
    }

    public List<String> getInfoLogger() {
        return infoLogger;
    }

    public void performTest() throws Exception {
        try {
            removeOldTemporaryFiles();

            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            performSorting();

            assertTrue(testpom.exists());
            assertTrue(backupFile.exists());

            backupFileInputStream = new FileInputStream(backupFile);
            String actualBackup = IOUtils.toString(backupFileInputStream, UTF_8);

            originalPomInputStream = new FileInputStream("src/test/resources/" + inputResourceFileName);
            String expectedBackup = IOUtils.toString(originalPomInputStream, UTF_8);
            assertEquals(expectedBackup, actualBackup);

            actualSortedPomInputStream = new FileInputStream(testpom);
            String actualSorted = IOUtils.toString(actualSortedPomInputStream, UTF_8);

            expectedSortedPomInputStream = new FileInputStream("src/test/resources/" + expectedResourceFileName);
            String expectedSorted = IOUtils.toString(expectedSortedPomInputStream, UTF_8);
            assertThat(actualSorted, is(expectedSorted));
            assertEquals(expectedSorted, actualSorted);
        } finally {
            cleanupAfterTest();
        }
    }

    public void performTestThatSorted() throws Exception {
        try {
            removeOldTemporaryFiles();

            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            performVerifyWithSort();

            assertTrue(testpom.exists());
            assertTrue(backupFile.exists());

            backupFileInputStream = new FileInputStream(backupFile);
            String actualBackup = IOUtils.toString(backupFileInputStream, UTF_8);

            originalPomInputStream = new FileInputStream("src/test/resources/" + inputResourceFileName);
            String expectedBackup = IOUtils.toString(originalPomInputStream, UTF_8);
            assertEquals(expectedBackup, actualBackup);

            actualSortedPomInputStream = new FileInputStream(testpom);
            String actualSorted = IOUtils.toString(actualSortedPomInputStream, UTF_8);

            expectedSortedPomInputStream = new FileInputStream("src/test/resources/" + expectedResourceFileName);
            String expectedSorted = IOUtils.toString(expectedSortedPomInputStream, UTF_8);
            assertEquals(expectedSorted, actualSorted);
        } finally {
            cleanupAfterTest();
        }
    }

    public void performNoSortTest() throws Exception {
        try {
            removeOldTemporaryFiles();

            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            performSorting();

            assertTrue(testpom.exists());
            assertFalse(backupFile.exists());

            actualSortedPomInputStream = new FileInputStream(testpom);
            String actualSorted = IOUtils.toString(actualSortedPomInputStream, UTF_8);

            expectedSortedPomInputStream = new FileInputStream("src/test/resources/" + expectedResourceFileName);
            String expectedSorted = IOUtils.toString(expectedSortedPomInputStream, UTF_8);
            assertEquals(expectedSorted, actualSorted);
        } finally {
            cleanupAfterTest();
        }
    }

    private void performSorting() {
        sortPomImpl.setup(
                createDummyLog(),
                pluginParameters);
        sortPomImpl.sortPom();
    }

    public XmlOrderedResult performVerify() throws Exception {
        try {
            removeOldTemporaryFiles();
            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            XmlOrderedResult verifyOk = isVerifyOk();

            assertTrue(testpom.exists());
            return verifyOk;
        } finally {
            cleanupAfterTest();
        }
    }

    public void performTestThatDidNotSort() throws Exception {
        try {
            removeOldTemporaryFiles();

            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            performVerifyWithSort();

            assertTrue(testpom.exists());
            assertFalse(backupFile.exists());

            actualSortedPomInputStream = new FileInputStream(testpom);
            String actualSorted = IOUtils.toString(actualSortedPomInputStream, UTF_8);

            expectedSortedPomInputStream = new FileInputStream("src/test/resources/" + expectedResourceFileName);
            String expectedSorted = IOUtils.toString(expectedSortedPomInputStream, UTF_8);
            assertEquals(expectedSorted, actualSorted);
        } finally {
            cleanupAfterTest();
        }
    }

    private void performVerifyWithSort() {
        SortPomImpl sortPomImpl = new SortPomImpl();
        sortPomImpl.setup(
                createDummyLog(),
                pluginParameters);

        sortPomImpl.verifyPom();
    }

    private XmlOrderedResult isVerifyOk() {
        sortPomImpl.setup(
                createDummyLog(),
                pluginParameters);
        return sortPomImpl.isPomElementsSorted();
    }

    private void removeOldTemporaryFiles() {
        if (testpom.exists()) {
            assertTrue(testpom.delete());
        }
    }

    private void cleanupAfterTest() {
        IOUtils.closeQuietly(backupFileInputStream);
        IOUtils.closeQuietly(originalPomInputStream);
        IOUtils.closeQuietly(actualSortedPomInputStream);
        IOUtils.closeQuietly(expectedSortedPomInputStream);
        if (testpom.exists()) {
            testpom.delete();
        }
        if (backupFile.exists()) {
            backupFile.delete();
        }
    }

    private SortPomLogger createDummyLog() {
        return new SortPomLogger() {

            @Override
                    public void info(CharSequence content) {
                        infoLogger.add("[INFO] " + content);
                    }
                    @Override
                    public void warn(CharSequence content) {
                        infoLogger.add("[WARNING] " + content);
                    }
                    @Override
                    public void error(CharSequence content) {
                        infoLogger.add("[ERROR] " + content);
                    }
        };
    }

}
