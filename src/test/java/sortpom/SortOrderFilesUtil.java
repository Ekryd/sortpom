package sortpom;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.util.IndentCharacters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SortOrderFilesUtil {

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

    private SortOrderFilesUtil() {
    }

    public static SortOrderFilesUtil create() {
        return new SortOrderFilesUtil();
    }

    public void testFiles(final String inputResourceFileName, final String expectedResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        this.inputResourceFileName = inputResourceFileName;
        this.expectedResourceFileName = expectedResourceFileName;
        setup();
        performTest();
    }

    public SortOrderFilesUtil nrOfIndentSpace(int indent) {
        nrOfIndentSpace = indent;
        return this;
    }

    public SortOrderFilesUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public SortOrderFilesUtil indentBLankLines() {
        indentBLankLines = true;
        return this;
    }

    public SortOrderFilesUtil sortDependencies() {
        sortDependencies = true;
        return this;
    }

    public SortOrderFilesUtil sortPlugins() {
        sortPlugins = true;
        return this;
    }

    public SortOrderFilesUtil sortProperties() {
        sortProperties = true;
        return this;
    }

    public SortOrderFilesUtil defaultOrderFileName(String defaultOrderFileName) {
        this.defaultOrderFileName = defaultOrderFileName;
        return this;
    }

    public SortOrderFilesUtil predefinedSortOrder(String predefinedSortOrder) {
        this.predefinedSortOrder = predefinedSortOrder;
        this.defaultOrderFileName = null;
        return this;
    }

    public SortOrderFilesUtil lineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public SortOrderFilesUtil testPomFileNameUniqueNumber(int uniqueNumber) {
        this.testPomFileName = "src/test/resources/testpom" +
                uniqueNumber + ".xml";
        return this;
    }

    private void setup() {
        testpom = new File(testPomFileName);
        testPomBackupExtension = ".testExtension";
        backupFile = new File(testpom.getAbsolutePath() + testPomBackupExtension);
    }

    private void performTest() throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
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

    private void removeOldTemporaryFiles() {
        if (testpom.exists()) {
            assertTrue(testpom.delete());
        }
    }

    private void performSorting() throws MojoFailureException {
        SortPomImpl sortPomImpl = new SortPomImpl();
        sortPomImpl.setup(
                createDummyMojo().getLog(),
                new PluginParametersBuilder()
                        .setPomFile(testpom)
                        .setBackupInfo(true, testPomBackupExtension)
                        .setFormatting(UTF_8, lineSeparator,
                                true, keepBlankLines)
                        .setIndent(new IndentCharacters(nrOfIndentSpace).getIndentCharacters(), indentBLankLines)
                        .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                        .setSortOrder(defaultOrderFileName, predefinedSortOrder).createPluginParameters());
        sortPomImpl.sortPom();
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
        };
    }
}
