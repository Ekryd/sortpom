package sortpom.util;

import org.apache.maven.plugin.MojoFailureException;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SortPomImplUtil {

    private TestHandler testHandler;

    private String defaultOrderFileName = "default_0_4_0.xml";
    private boolean sortDependencies = false;
    private boolean sortPlugins = false;
    private boolean sortProperties = false;
    private String predefinedSortOrder = "";
    private String lineSeparator = "\r\n";
    private String testPomFileName = "src/test/resources/testpom.xml";
    private String testPomBackupExtension = ".testExtension";

    private int nrOfIndentSpace = 2;
    private boolean keepBlankLines = false;
    private boolean indentBLankLines = false;
    private String verifyFail = "SORT";
    private String encoding = testHandler.UTF_8;
    private File testpom;

    private SortPomImplUtil() {
    }

    public static SortPomImplUtil create() {
        return new SortPomImplUtil();
    }

    public void testFiles(final String inputResourceFileName, final String expectedResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, expectedResourceFileName, getPluginParameters());
        testHandler.performTest();
    }

    public void testNoSorting(final String inputResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, inputResourceFileName, getPluginParameters());
        testHandler.performNoSortTest();
        assertEquals("Pom file is already sorted, exiting", testHandler.getInfoLogger().get(1));
    }

    public void testVerifyXmlIsOrdered(final String inputResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        assertEquals("Expected that xml is ordered, ", true, testHandler.performVerify());
    }

    public void testVerifyXmlIsNotOrdered(final String inputResourceFileName, CharSequence warningMessage)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        assertEquals("Expected that xml is not ordered, ", false, testHandler.performVerify());
        assertEquals(warningMessage, testHandler.getInfoLogger().get(0));
    }

    public void testVerifySort(final String inputResourceFileName, final String expectedResourceFileName)
            throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, expectedResourceFileName, getPluginParameters());
        testHandler.performTestThatSorted();
    }

    public void testVerifyFail(String inputResourceFileName, Class<?> expectedExceptionClass) throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        try {
            testHandler.performTestThatDidNotSort();
            fail();
        } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
        }
    }

    public void testVerifyWarn(String inputResourceFileName, String warningMessage) throws IOException, NoSuchFieldException, MojoFailureException, IllegalAccessException {
        setup();
        testHandler = new TestHandler(inputResourceFileName, inputResourceFileName, getPluginParameters());
        testHandler.performTestThatDidNotSort();
        assertEquals(warningMessage, testHandler.getInfoLogger().get(1));
    }

    public SortPomImplUtil nrOfIndentSpace(int indent) {
        nrOfIndentSpace = indent;
        return this;
    }

    public SortPomImplUtil keepBlankLines() {
        keepBlankLines = true;
        return this;
    }

    public SortPomImplUtil indentBLankLines() {
        indentBLankLines = true;
        return this;
    }

    public SortPomImplUtil sortDependencies() {
        sortDependencies = true;
        return this;
    }

    public SortPomImplUtil sortPlugins() {
        sortPlugins = true;
        return this;
    }

    public SortPomImplUtil sortProperties() {
        sortProperties = true;
        return this;
    }

    public SortPomImplUtil defaultOrderFileName(String defaultOrderFileName) {
        this.defaultOrderFileName = defaultOrderFileName;
        return this;
    }

    public SortPomImplUtil predefinedSortOrder(String predefinedSortOrder) {
        this.predefinedSortOrder = predefinedSortOrder;
        this.defaultOrderFileName = null;
        return this;
    }

    public SortPomImplUtil lineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
        return this;
    }

    public SortPomImplUtil verifyFail(String verifyFail) {
        this.verifyFail = verifyFail;
        return this;
    }

    public SortPomImplUtil backupFileExtension(String backupFileExtension) {
        this.testPomBackupExtension = backupFileExtension;
        return this;
    }

    public SortPomImplUtil encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public SortPomImplUtil testPomFileNameUniqueNumber(int uniqueNumber) {
        this.testPomFileName = "src/test/resources/testpom" +
                uniqueNumber + ".xml";
        return this;
    }

    private void setup() {
        testpom = new File(testPomFileName);
    }

    private PluginParameters getPluginParameters() throws MojoFailureException {
        return new PluginParametersBuilder()
                .setPomFile(testpom)
                .setBackupInfo(true, testPomBackupExtension)
                .setEncoding(encoding)
                .setFormatting(lineSeparator,
                        true, keepBlankLines)
                .setIndent(nrOfIndentSpace, indentBLankLines)
                .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                .setSortOrder(defaultOrderFileName, predefinedSortOrder)
                .setVerifyFail(verifyFail)
                .createPluginParameters();
    }

}
