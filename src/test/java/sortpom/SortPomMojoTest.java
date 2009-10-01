package sortpom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;

import sortpom.util.ReflectionHelper;

public class SortPomMojoTest extends TestCase {

	private static final String UTF_8 = "UTF-8";

	public final void testSortDifferentClassPath() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml", new File(
				"difforder/differentOrder.xml"));
	}

	public final void testSortDifferentRelativePath() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_differentorder_expected.xml", new File(
				"src/test/resources/difforder/differentOrder.xml"));
	}

	public final void testSortXmlCharacterToAlfabetical() throws Exception {
		testFiles("/Character_input.xml", "/Character_expected.xml");
	}

	public final void testSortXmlComplexToAlfabetical() throws Exception {
		testFiles("/Complex_input.xml", "/Complex_expected.xml");
	}

	public final void testSortXmlFullFromAlfabeticalOrder() throws Exception {
		testFiles("/full_alfa_input.xml", "/full_expected.xml");
	}

	public final void testSortXmlFullToAlfabetical() throws Exception {
		testFiles("/full_unsorted_input.xml", "/full_expected.xml");
	}

	public final void testSortXmlReal1() throws Exception {
		testFiles("/Real1_input.xml", "/Real1_expected.xml");
	}

	public final void testSortXmlSimple() throws Exception {
		testFiles("/Simple_input.xml", "/Simple_expected.xml");
	}

	private void testFiles(final String inputResourceFileName, final String expectedResourceFileName)
			throws IOException, NoSuchFieldException, IllegalAccessException, MojoFailureException {
		testFiles(inputResourceFileName, expectedResourceFileName, null);
	}

	private void testFiles(final String inputResourceFileName, final String expectedResourceFileName,
			final File defaultOrderFileName) throws IOException, NoSuchFieldException, IllegalAccessException,
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
			reflectionHelper.setField("defaultOrderFileName", defaultOrderFileName);
			reflectionHelper.setField("lineSeparatorString", "\r\n");
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
