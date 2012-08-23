package sortpom.verify.util;

import org.apache.commons.io.IOUtils;
import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import sortpom.SortPomImpl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class VerifyOrderFilesUtil {

    SortPomImpl sortPomImpl = new SortPomImpl();

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
    private String testPomBackupExtension = ".testExtension";
    private File backupFile;

    private FileInputStream backupFileInputStream = null;
    private FileInputStream originalPomInputStream = null;
    private FileInputStream actualSortedPomInputStream = null;
    private FileInputStream expectedSortedPomInputStream = null;
    private int nrOfIndentSpace = 2;
    private boolean keepBlankLines = false;
    private boolean indentBLankLines = false;
    private String verifyFail = "SORT";
    private List<CharSequence> infoLogger = new ArrayList<CharSequence>();
    private String encoding = UTF_8;

    private VerifyOrderFilesUtil() {
    }

    public static VerifyOrderFilesUtil create() {
        return new VerifyOrderFilesUtil();
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

    public VerifyOrderFilesUtil backupFileExtension(String backupFileExtension) {
        this.testPomBackupExtension = backupFileExtension;
        return this;
    }

    public VerifyOrderFilesUtil encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public VerifyOrderFilesUtil testPomFileNameUniqueNumber(int uniqueNumber) {
        this.testPomFileName = "src/test/resources/testpom" +
                uniqueNumber + ".xml";
        return this;
    }

    private void setup() {
        testpom = new File(testPomFileName);
        backupFile = new File(testpom.getAbsolutePath() + testPomBackupExtension);
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
