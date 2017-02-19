package sortpom.util;

import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;

public class SortPomImplUtil {

    private TestHandler testHandler;

    private String defaultOrderFileName = "default_0_4_0.xml";
    private String sortDependencies = "";
    private String sortPlugins = "";
    private boolean sortProperties = false;
    private String predefinedSortOrder = "";
    private String lineSeparator = "\r\n";
    private String testPomFileName = "src/test/resources/testpom.xml";
    private String testPomBackupExtension = ".testExtension";

    private int nrOfIndentSpace = 2;
    private boolean keepBlankLines = false;
    private boolean ignoreLineSeparators = true;
    private boolean indentBLankLines = false;
    private String verifyFail = "SORT";
    private String encoding = TestHandler.UTF_8;
    private File testpom;

    private SortPomImplUtil() {
    }

    public static SortPomImplUtil create() {
        return new SortPomImplUtil();
    }

    public void testFiles(final String inputResourceFileName, final String expectedResourceFileName)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, expectedResourceFileName, getPluginParameters());
        testHandler.performTest();
    }

    public List<String> testFilesAndReturnLogs(final String inputResourceFileName, final String expectedResourceFileName)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, expectedResourceFileName, getPluginParameters());
        testHandler.performTest();
        return testHandler.getInfoLogger();
    }

    public void testNoSorting(final String inputResourceFileName)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, inputResourceFileName, getPluginParameters());
        testHandler.performNoSortTest();
        assertEquals("[INFO] Pom file is already sorted, exiting", testHandler.getInfoLogger().get(1));
    }

    public void testVerifyXmlIsOrdered(final String inputResourceFileName)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        XmlOrderedResult xmlOrderedResult = testHandler.performVerify();
        assertEquals("Expected that xml is ordered, ", true, xmlOrderedResult.isOrdered());
    }

    public void testVerifyXmlIsNotOrdered(final String inputResourceFileName, CharSequence warningMessage)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        XmlOrderedResult xmlOrderedResult = testHandler.performVerify();
        assertEquals("Expected that xml is not ordered, ", false, xmlOrderedResult.isOrdered());
        assertEquals(warningMessage, xmlOrderedResult.getErrorMessage());
    }

    public void testVerifySort(final String inputResourceFileName, final String expectedResourceFileName, String warningMessage)
            throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, expectedResourceFileName, getPluginParameters());
        testHandler.performTestThatSorted();
        assertThat(testHandler.getInfoLogger().get(0), startsWith("[INFO] Verifying file "));
        assertEquals(warningMessage, testHandler.getInfoLogger().get(1));
        assertThat(testHandler.getInfoLogger().get(2), startsWith("[INFO] The file "));
        assertThat(testHandler.getInfoLogger().get(2), endsWith(" is not sorted"));
        assertThat(testHandler.getInfoLogger().get(3), startsWith("[INFO] Sorting file "));
    }

    public void testVerifyFail(String inputResourceFileName, Class<?> expectedExceptionClass, String warningMessage) {
        setup();
        testHandler = new TestHandler(inputResourceFileName, getPluginParameters());
        try {
            testHandler.performTestThatDidNotSort();
            fail();
        } catch (Exception e) {
            assertEquals(expectedExceptionClass, e.getClass());
            assertThat(testHandler.getInfoLogger().get(0), startsWith("[INFO] Verifying file "));
            assertEquals(warningMessage, testHandler.getInfoLogger().get(1));
            assertThat(testHandler.getInfoLogger().get(2), startsWith("[ERROR] The file "));
            assertThat(testHandler.getInfoLogger().get(2), endsWith(" is not sorted"));
        }
    }

    public void testVerifyWarn(String inputResourceFileName, String warningMessage) throws Exception {
        setup();
        testHandler = new TestHandler(inputResourceFileName, inputResourceFileName, getPluginParameters());
        testHandler.performTestThatDidNotSort();
        assertThat(testHandler.getInfoLogger().get(0), startsWith("[INFO] Verifying file "));
        assertEquals(warningMessage, testHandler.getInfoLogger().get(1));
        assertThat(testHandler.getInfoLogger().get(2), startsWith("[WARNING] The file "));
        assertThat(testHandler.getInfoLogger().get(2), endsWith(" is not sorted"));
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

    public SortPomImplUtil sortDependencies(String sortOrder) {
        sortDependencies = sortOrder;
        return this;
    }

    public SortPomImplUtil sortPlugins(String sortOrder) {
        sortPlugins = sortOrder;
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

    public SortPomImplUtil ignoreLineSeparators(boolean ignoreLineSeparators) {
        this.ignoreLineSeparators = ignoreLineSeparators;
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

    private PluginParameters getPluginParameters() {
        return new PluginParametersBuilder()
                .setPomFile(testpom)
                .setBackupInfo(true, testPomBackupExtension)
                .setEncoding(encoding)
                .setFormatting(lineSeparator, true, keepBlankLines)
                .setIndent(nrOfIndentSpace, indentBLankLines)
                .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                .setSortOrder(defaultOrderFileName, predefinedSortOrder)
                .setVerifyFail(verifyFail)
                .setTriggers(ignoreLineSeparators)
                .createPluginParameters();
    }

}
