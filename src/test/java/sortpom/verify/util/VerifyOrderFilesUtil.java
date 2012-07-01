package sortpom.verify.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import sortpom.SortPomImpl;
import sortpom.parameter.PluginParametersBuilder;
import sortpom.util.IndentCharacters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class VerifyOrderFilesUtil {

    private static final String UTF_8 = "UTF-8";
    private String inputResourceFileName;
    private String defaultOrderFileName = "default_0_4_0.xml";
    private boolean sortDependencies = false;
    private boolean sortPlugins = false;
    private boolean sortProperties = false;
    private String predefinedSortOrder = "";
    private String lineSeparator = "\r\n";
    private String testPomFileName = "src/test/resources/testpom.xml";
    private File testpom;
    private String testPomBackupExtension;

    private FileInputStream originalPomInputStream = null;
    private int nrOfIndentSpace = 2;
    private boolean keepBlankLines = false;
    private boolean indentBLankLines = false;

    private VerifyOrderFilesUtil() {
    }

    public static VerifyOrderFilesUtil create() {
        return new VerifyOrderFilesUtil();
    }

    public boolean isPomSorted(final String inputResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        this.inputResourceFileName = inputResourceFileName;
        setup();
        return performVerify();
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

    public VerifyOrderFilesUtil testPomFileNameUniqueNumber(int uniqueNumber) {
        this.testPomFileName = "src/test/resources/testpom" +
                uniqueNumber + ".xml";
        return this;
    }

    private void setup() {
        testpom = new File(testPomFileName);
        testPomBackupExtension = ".testExtension";
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
                        .setSortOrder(defaultOrderFileName, predefinedSortOrder).createPluginParameters());
        return sortPomImpl.isPomElementsSorted();
    }

    private void cleanupAfterTest() {
        IOUtils.closeQuietly(originalPomInputStream);
        if (testpom.exists()) {
            testpom.delete();
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
