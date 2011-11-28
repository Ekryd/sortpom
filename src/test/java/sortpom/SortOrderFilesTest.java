package sortpom;

import java.io.*;

import junit.framework.*;

import org.apache.commons.io.*;
import org.apache.maven.plugin.*;

import sortpom.util.*;

public class SortOrderFilesTest extends TestCase {

	private static final String UTF_8 = "UTF-8";

	public final void testSortDifferentOrder() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml", "difforder/differentOrder.xml");
	}

	public final void testSortOldOrder() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_expected.xml", "default_0_4_0.xml");
	}

	public final void testSortAltOrder() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_alt_expected.xml", "custom_1.xml");
	}

	public final void testSortDefaultOrder() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_default_expected.xml", "recommended_2008_06.xml");
	}

	private void testFiles(final String inputResourceFileName, final String expectedResourceFileName,
			final String defaultOrderFileName) throws IOException, NoSuchFieldException, IllegalAccessException,
			MojoFailureException {
		testFiles(inputResourceFileName, expectedResourceFileName, defaultOrderFileName, 2, false, false);
	}

	private void testFiles(final String inputResourceFileName, final String expectedResourceFileName,
			final String defaultOrderFileName, final int nrOfIndentSpace, final boolean sortDependencies,
			final boolean sortPlugins) throws IOException, NoSuchFieldException, IllegalAccessException,
			MojoFailureException {
		final String testPomFileName = "src/test/resources/testpom.xml";
		final File testpom = new File(testPomFileName);
		final String testPomBackupExtension = ".testExtension";
		final File backupFile = new File(testpom.getAbsolutePath() + testPomBackupExtension);
		FileInputStream inputStream1 = null;
		FileInputStream inputStream2 = null;
		FileInputStream inputStream3 = null;
		FileInputStream inputStream4 = null;
		try {
			if (testpom.exists()) {
				assertTrue(testpom.delete());
			}
			FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
			SortPomMojo sortPomMojo = new SortPomMojo();
			final ReflectionHelper reflectionHelper = new ReflectionHelper(sortPomMojo);
			reflectionHelper.setField("pomFile", testpom);
			reflectionHelper.setField("createBackupFile", true);
			reflectionHelper.setField("backupFileExtension", testPomBackupExtension);
			reflectionHelper.setField("encoding", UTF_8);
			reflectionHelper.setField("sortOrderFile", defaultOrderFileName);
			reflectionHelper.setField("lineSeparator", "\r\n");
			reflectionHelper.setField("nrOfIndentSpace", nrOfIndentSpace);
			reflectionHelper.setField("sortDependencies", sortDependencies);
			reflectionHelper.setField("sortPlugins", sortPlugins);
			sortPomMojo.execute();
			assertTrue(testpom.exists());
			assertTrue(backupFile.exists());
			inputStream1 = new FileInputStream(backupFile);
			String actualBackup = IOUtils.toString(inputStream1, UTF_8);
			inputStream2 = new FileInputStream("src/test/resources/" + inputResourceFileName);
			String expectedBackup = IOUtils.toString(inputStream2, UTF_8);
			assertEquals(expectedBackup, actualBackup);
			inputStream3 = new FileInputStream(testpom);
			String actualSorted = IOUtils.toString(inputStream3, UTF_8);
			// Remove extra line
			inputStream4 = new FileInputStream("src/test/resources/" + expectedResourceFileName);
			String expectedSorted = IOUtils.toString(inputStream4, UTF_8);
			assertEquals(expectedSorted, actualSorted);
		} finally {
			IOUtils.closeQuietly(inputStream1);
			IOUtils.closeQuietly(inputStream2);
			IOUtils.closeQuietly(inputStream3);
			IOUtils.closeQuietly(inputStream4);
			if (testpom.exists()) {
				testpom.delete();
			}
			if (backupFile.exists()) {
				backupFile.delete();
			}
		}
	}
}
