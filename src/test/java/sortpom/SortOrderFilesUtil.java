package sortpom;

import static org.junit.Assert.*;

import java.io.*;

import org.apache.commons.io.*;
import org.apache.maven.plugin.*;

import sortpom.util.*;

public class SortOrderFilesUtil {

	private static final String UTF_8 = "UTF-8";
	private final String inputResourceFileName;
	private final String expectedResourceFileName;
	private final String defaultOrderFileName;
	private final boolean sortDependencies;
	private final boolean sortPlugins;
	private final String predefinedSortOrder;
	private final String lineSeparator;
	private String testPomFileName;
	private File testpom;
	private String testPomBackupExtension;
	private File backupFile;

	private FileInputStream backupFileInputStream = null;
	private FileInputStream originalPomInputStream = null;
	private FileInputStream actualSortedPomInputStream = null;
	private FileInputStream expectedSortedPomInputStream = null;
	private final int nrOfIndentSpace;

	public static void testFilesWithCustomSortOrder(final String inputResourceFileName,
			final String expectedResourceFileName, final String defaultOrderFileName) throws IOException,
			NoSuchFieldException, IllegalAccessException, MojoFailureException {
		SortOrderFilesUtil sortOrderFilesUtil = new SortOrderFilesUtil(inputResourceFileName, expectedResourceFileName,
				defaultOrderFileName, 2, false, false, "", "\n");
		sortOrderFilesUtil.setup();
		sortOrderFilesUtil.performTest();
	}

	public static void testFilesWithPredefinedSortOrder(final String inputResourceFileName,
			final String expectedResourceFileName, final String predefinedSortOrder) throws IOException,
			NoSuchFieldException, IllegalAccessException, MojoFailureException {
		SortOrderFilesUtil sortOrderFilesUtil = new SortOrderFilesUtil(inputResourceFileName, expectedResourceFileName,
				null, 2, false, false, predefinedSortOrder, "\n");
		sortOrderFilesUtil.setup();
		sortOrderFilesUtil.performTest();
	}

	public static void testFilesDefaultOrder(final String inputResourceFileName, final String expectedResourceFileName)
			throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
		SortOrderFilesUtil sortOrderFilesUtil = new SortOrderFilesUtil(inputResourceFileName, expectedResourceFileName,
				"default_0_4_0.xml", 2, false, false, "", "\r\n");
		sortOrderFilesUtil.setup();
		sortOrderFilesUtil.performTest();
	}

	public static void testFiles(String inputResourceFileName, String expectedResourceFileName,
			String defaultOrderFileName, final int nrOfIndentSpace, boolean sortDependencies, boolean sortPlugins,
			String predefinedSortOrder, String lineSeparator) throws IOException, NoSuchFieldException,
			IllegalAccessException, MojoFailureException {
		SortOrderFilesUtil sortOrderFilesUtil = new SortOrderFilesUtil(inputResourceFileName, expectedResourceFileName,
				defaultOrderFileName, nrOfIndentSpace, sortDependencies, sortPlugins, predefinedSortOrder,
				lineSeparator);
		sortOrderFilesUtil.setup();
		sortOrderFilesUtil.performTest();
	}

	private SortOrderFilesUtil(String inputResourceFileName, String expectedResourceFileName,
			String defaultOrderFileName, final int nrOfIndentSpace, boolean sortDependencies, boolean sortPlugins,
			String predefinedSortOrder, String lineSeparator) {
		this.inputResourceFileName = inputResourceFileName;
		this.expectedResourceFileName = expectedResourceFileName;
		this.defaultOrderFileName = defaultOrderFileName;
		this.nrOfIndentSpace = nrOfIndentSpace;
		this.sortDependencies = sortDependencies;
		this.sortPlugins = sortPlugins;
		this.predefinedSortOrder = predefinedSortOrder;
		this.lineSeparator = lineSeparator;
	}

	private void setup() {
		testPomFileName = "src/test/resources/testpom.xml";
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
								new IndentCharacters(nrOfIndentSpace).getIndentCharacters(), true)
						.setSortEntities(sortDependencies, sortPlugins)
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
				// TODO Auto-generated method stub

			}
		};
	}
}
