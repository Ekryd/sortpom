package sortpom.verify.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import sortpom.SortPomImpl;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.util.IndentCharacters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class VerifyOrderFilesUtil {

    private static final String UTF_8 = "UTF-8";
    private String inputResourceFileName;
    private String expectedResourceFileName;
    private String defaultOrderFileName = "default_0_4_0.xml";
    private boolean sortDependencies = false;
    private boolean sortPlugins = false;
    private boolean sortProperties = false;
    private String predefinedSortOrder = "";
    private String lineSeparator = "\r\n";
    private String testPomFileName = "src/test/resources/testpom.xml";
    private File testpom;
    private String testPomBackupExtension;
    private File backupFile;

    private FileInputStream backupFileInputStream = null;
    private FileInputStream originalPomInputStream = null;
    private FileInputStream actualSortedPomInputStream = null;
    private FileInputStream expectedSortedPomInputStream = null;
    private int nrOfIndentSpace = 2;
    private boolean keepBlankLines = false;
    private boolean indentBLankLines = false;
    private String verifyFail = "sort";
    private List<CharSequence> infoLogger = new ArrayList<CharSequence>();

    private VerifyOrderFilesUtil() {
    }

    public static VerifyOrderFilesUtil create() {
        return new VerifyOrderFilesUtil();
    }

    public void testVerifyXmlIsOrdered(final String inputResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        this.inputResourceFileName = inputResourceFileName;
        setup();
        assertEquals("Expected that xml is ordered, ", true, performVerify());
    }

    public void testVerifyXmlIsNotOrdered(final String inputResourceFileName, CharSequence warningMessage)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        this.inputResourceFileName = inputResourceFileName;
        setup();
        assertEquals("Expected that xml is not ordered, ", false, performVerify());
        assertEquals(warningMessage, infoLogger.get(0));
    }

    public void testVerifySort(final String inputResourceFileName, final String expectedResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        this.inputResourceFileName = inputResourceFileName;
        this.expectedResourceFileName = expectedResourceFileName;
        setup();
        performTestThatSorted();
    }

    public void testVerifyFail(String inputResourceFileName, Class<?> expectedExceptionClass) throws IOException, NoSuchFieldException, IllegalAccessException {
        this.inputResourceFileName = inputResourceFileName;
        setup();
        try {
            performTestThatDidNotSort();
            fail();
         } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
        }
    }

    public void testVerifyWarn(String inputResourceFileName, String warningMessage) throws IOException, NoSuchFieldException, MojoFailureException, IllegalAccessException {
        this.inputResourceFileName = inputResourceFileName;
        this.expectedResourceFileName = inputResourceFileName;
        setup();
        performTestThatDidNotSort();
        assertEquals(warningMessage, infoLogger.get(1));
    }

    public VerifyOrderFilesUtil nrOfIndentSpace(int indent) {
        nrOfIndentSpace = indent;
        return this;
    }

    public VerifyOrderFilesUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public VerifyOrderFilesUtil indentBLankLines() {
        indentBLankLines = true;
        return this;
    }

    public VerifyOrderFilesUtil sortDependencies() {
        sortDependencies = true;
        return this;
    }

    public VerifyOrderFilesUtil sortPlugins() {
        sortPlugins = true;
        return this;
    }

    public VerifyOrderFilesUtil sortProperties() {
        sortProperties = true;
        return this;
    }

    public VerifyOrderFilesUtil defaultOrderFileName(String defaultOrderFileName) {
        this.defaultOrderFileName = defaultOrderFileName;
        return this;
    }

    public VerifyOrderFilesUtil predefinedSortOrder(String predefinedSortOrder) {
        this.predefinedSortOrder = predefinedSortOrder;
        this.defaultOrderFileName = null;
        return this;
    }

    public VerifyOrderFilesUtil lineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public VerifyOrderFilesUtil verifyFail(String behaviour) {
        this.verifyFail = behaviour;
        return this;
    }

    public VerifyOrderFilesUtil testPomFileNameUniqueNumber(int uniqueNumber) {
        this.testPomFileName = "src/test/resources/testpom" +
                uniqueNumber + ".xml";
        return this;
    }

    private void setup() {
        testpom = new File(testPomFileName);
        testPomBackupExtension = ".testExtension";
        backupFile = new File(testpom.getAbsolutePath() + testPomBackupExtension);
    }

    private boolean performVerify() throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
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

    private void performTestThatSorted() throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
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

    private void performTestThatDidNotSort() throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
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

    private void removeOldTemporaryFiles() {
        if (testpom.exists()) {
            assertTrue(testpom.delete());
        }
    }

    private boolean isVerifyOk() throws MojoFailureException {
        SortPomImpl sortPomImpl = new SortPomImpl();
        sortPomImpl.setup(
                createDummyMojo().getLog(),
                new PluginParametersBuilder()
                        .setPomFile(testpom)
                        .setBackupInfo(true, testPomBackupExtension)
                        .setEncoding(UTF_8)
                        .setFormatting(lineSeparator,
                                true, keepBlankLines)
                        .setIndent(new IndentCharacters(nrOfIndentSpace).getIndentCharacters(), indentBLankLines)
                        .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                        .setSortOrder(defaultOrderFileName, predefinedSortOrder)
                        .setVerifyFail(verifyFail)
                        .createPluginParameters());
        return sortPomImpl.isPomElementsSorted();
    }

    private void performVerifyWithSort() throws MojoFailureException {
        SortPomImpl sortPomImpl = new SortPomImpl();
        sortPomImpl.setup(
                createDummyMojo().getLog(),
                new PluginParametersBuilder()
                        .setPomFile(testpom)
                        .setBackupInfo(true, testPomBackupExtension)
                        .setEncoding(UTF_8)
                        .setFormatting(lineSeparator,
                                true, keepBlankLines)
                        .setIndent(new IndentCharacters(nrOfIndentSpace).getIndentCharacters(), indentBLankLines)
                        .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                        .setSortOrder(defaultOrderFileName, predefinedSortOrder)
                        .setVerifyFail(verifyFail)
                        .createPluginParameters());
        
        sortPomImpl.verifyPom();
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
