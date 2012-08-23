package sortpom.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import sortpom.SortPomImpl;
import sortpom.parameter.PluginParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class TestHandler {
    static final String UTF_8 = "UTF-8";

    private final SortPomImpl sortPomImpl = new SortPomImpl();

    private final List<CharSequence> infoLogger = new ArrayList<CharSequence>();
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

    public List<CharSequence> getInfoLogger() {
        return infoLogger;
    }

    public void performTest() throws IOException, MojoFailureException {
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
            assertEquals(expectedSorted, actualSorted);
        } finally {
            cleanupAfterTest();
        }
    }

    public void performTestThatSorted() throws IOException, MojoFailureException {
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

    public void performNoSortTest() throws IOException, MojoFailureException {
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

    private void performSorting() throws MojoFailureException {
        sortPomImpl.setup(
                createDummyMojo().getLog(),
                pluginParameters);
        sortPomImpl.sortPom();
    }

    public boolean performVerify() throws IOException, MojoFailureException {
        try {
            removeOldTemporaryFiles();
            FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
            boolean verifyOk = isVerifyOk();

            assertTrue(testpom.exists());
            return verifyOk;
        } finally {
            cleanupAfterTest();
        }
    }

    public void performTestThatDidNotSort() throws IOException, MojoFailureException {
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

    private void performVerifyWithSort() throws MojoFailureException {
        SortPomImpl sortPomImpl = new SortPomImpl();
        sortPomImpl.setup(
                createDummyMojo().getLog(),
                pluginParameters);

        sortPomImpl.verifyPom();
    }

    private boolean isVerifyOk() throws MojoFailureException {
        sortPomImpl.setup(
                createDummyMojo().getLog(),
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

    private AbstractMojo createDummyMojo() {
        return new AbstractMojo() {

            @Override
            public void execute() throws MojoExecutionException, MojoFailureException {
            }

            @Override
            public Log getLog() {
                return new DefaultLog(null) {
                    @Override
                    public void info(CharSequence content) {
                        infoLogger.add(content);
                    }
                };
            }
        };
    }

}
