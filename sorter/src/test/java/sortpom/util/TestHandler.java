package sortpom.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import sortpom.SortPomImpl;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class TestHandler {

  private final SortPomImpl sortPomImpl = new SortPomImpl();

  private final List<String> infoLogger = new ArrayList<>();
  private final String inputResourceFileName;
  private final String expectedResourceFileName;
  private final File testpom;
  private final PluginParameters pluginParameters;

  private FileInputStream backupFileInputStream = null;
  private FileInputStream originalPomInputStream = null;
  private FileInputStream actualSortedPomInputStream = null;
  private FileInputStream expectedSortedPomInputStream = null;
  private final File backupFile;

  TestHandler(String inputResourceFileName, PluginParameters pluginParameters) {
    this(inputResourceFileName, null, pluginParameters);
  }

  TestHandler(
      String inputResourceFileName,
      String expectedResourceFileName,
      PluginParameters pluginParameters) {
    this.inputResourceFileName = inputResourceFileName;
    this.expectedResourceFileName = expectedResourceFileName;
    this.pluginParameters = pluginParameters;
    this.testpom = pluginParameters.pomFile;
    backupFile = new File(testpom.getAbsolutePath() + pluginParameters.backupFileExtension);
  }

  List<String> getInfoLogger() {
    return infoLogger;
  }

  void performSortThatSorted() throws Exception {
    try {
      removeOldTemporaryFiles();

      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      performSorting();

      assertTrue(testpom.exists());
      assertTrue(backupFile.exists());

      backupFileInputStream = new FileInputStream(backupFile);
      String actualBackup =
          new String(backupFileInputStream.readAllBytes(), pluginParameters.encoding);

      originalPomInputStream = new FileInputStream("src/test/resources/" + inputResourceFileName);
      String expectedBackup =
          new String(originalPomInputStream.readAllBytes(), pluginParameters.encoding);
      assertEquals(expectedBackup, actualBackup);

      actualSortedPomInputStream = new FileInputStream(testpom);
      String actualSorted =
          new String(actualSortedPomInputStream.readAllBytes(), pluginParameters.encoding);

      expectedSortedPomInputStream =
          new FileInputStream("src/test/resources/" + expectedResourceFileName);
      String expectedSorted =
          new String(expectedSortedPomInputStream.readAllBytes(), pluginParameters.encoding);
      assertEquals(expectedSorted, actualSorted);
    } finally {
      cleanupAfterTest();
    }
  }

  void performVerifyThatSorted() throws Exception {
    try {
      removeOldTemporaryFiles();

      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      performVerifyWithSort();

      assertTrue(testpom.exists());
      if (pluginParameters.createBackupFile) {
        assertTrue(backupFile.exists());

        backupFileInputStream = new FileInputStream(backupFile);
        String actualBackup =
            new String(backupFileInputStream.readAllBytes(), pluginParameters.encoding);

        originalPomInputStream = new FileInputStream("src/test/resources/" + inputResourceFileName);
        String expectedBackup =
            new String(originalPomInputStream.readAllBytes(), pluginParameters.encoding);
        assertEquals(expectedBackup, actualBackup);
      }

      actualSortedPomInputStream = new FileInputStream(testpom);
      String actualSorted =
          new String(actualSortedPomInputStream.readAllBytes(), pluginParameters.encoding);

      expectedSortedPomInputStream =
          new FileInputStream("src/test/resources/" + expectedResourceFileName);
      String expectedSorted =
          new String(expectedSortedPomInputStream.readAllBytes(), pluginParameters.encoding);
      assertEquals(expectedSorted, actualSorted);
    } finally {
      cleanupAfterTest();
    }
  }

  void performSortThatDidNotSort() throws Exception {
    try {
      removeOldTemporaryFiles();

      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      performSorting();

      assertTrue(testpom.exists());
      assertFalse(backupFile.exists(), "No sort expected, backup file exists");

      actualSortedPomInputStream = new FileInputStream(testpom);
      String actualSorted =
          new String(actualSortedPomInputStream.readAllBytes(), pluginParameters.encoding);

      expectedSortedPomInputStream =
          new FileInputStream("src/test/resources/" + expectedResourceFileName);
      String expectedSorted =
          new String(expectedSortedPomInputStream.readAllBytes(), pluginParameters.encoding);
      assertEquals(expectedSorted, actualSorted);
    } finally {
      cleanupAfterTest();
    }
  }

  private void performSorting() {
    sortPomImpl.setup(createDummyLog(), pluginParameters);
    sortPomImpl.sortPom();
  }

  XmlOrderedResult performVerify() throws Exception {
    try {
      removeOldTemporaryFiles();
      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      XmlOrderedResult verifyOk = isVerifyOk();

      assertTrue(testpom.exists());
      return verifyOk;
    } finally {
      cleanupAfterTest();
    }
  }

  void performVerifyThatDidNotSort() throws Exception {
    try {
      removeOldTemporaryFiles();

      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      performVerifyWithSort();

      assertTrue(testpom.exists());
      assertFalse(backupFile.exists());

      actualSortedPomInputStream = new FileInputStream(testpom);
      String actualSorted =
          new String(actualSortedPomInputStream.readAllBytes(), pluginParameters.encoding);

      expectedSortedPomInputStream =
          new FileInputStream("src/test/resources/" + expectedResourceFileName);
      String expectedSorted =
          new String(expectedSortedPomInputStream.readAllBytes(), pluginParameters.encoding);
      assertEquals(expectedSorted, actualSorted);
    } finally {
      cleanupAfterTest();
    }
  }

  void performSortThatTestsTimestamps() throws Exception {
    try {
      removeOldTemporaryFiles();

      FileUtils.copyFile(new File("src/test/resources/" + inputResourceFileName), testpom);
      long pomTimestamp = testpom.lastModified();
      performSorting();

      if (pluginParameters.keepTimestamp) {
        assertThat(testpom.lastModified(), is(pomTimestamp));
        // Do not assert anything about the backup file, since that timestamp is OS dependent
      } else {
        assertThat(testpom.lastModified(), greaterThan(pomTimestamp));
        // Do not assert anything about the backup file, since that timestamp is OS dependent
      }
    } finally {
      cleanupAfterTest();
    }
  }

  private void performVerifyWithSort() {
    SortPomImpl sortPomImpl = new SortPomImpl();
    sortPomImpl.setup(createDummyLog(), pluginParameters);

    sortPomImpl.verifyPom();
  }

  private XmlOrderedResult isVerifyOk()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    sortPomImpl.setup(createDummyLog(), pluginParameters);

    Method getVerificationResult = SortPomImpl.class.getDeclaredMethod("getVerificationResult");
    getVerificationResult.setAccessible(true);

    return (XmlOrderedResult) getVerificationResult.invoke(sortPomImpl);
  }

  private void removeOldTemporaryFiles() {
    if (testpom.exists()) {
      assertTrue(testpom.delete());
    }
  }

  private void cleanupAfterTest() {
    closeQuietly(backupFileInputStream);
    closeQuietly(originalPomInputStream);
    closeQuietly(actualSortedPomInputStream);
    closeQuietly(expectedSortedPomInputStream);
    if (testpom.exists()) {
      assertTrue(testpom.delete());
    }
    if (backupFile.exists()) {
      assertTrue(backupFile.delete());
    }
  }

  private void closeQuietly(Closeable stream) {
    if (stream != null) {
      try {
        stream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private SortPomLogger createDummyLog() {
    return new SortPomLogger() {

      @Override
      public void info(String content) {
        infoLogger.add("[INFO] " + content);
      }

      @Override
      public void warn(String content) {
        infoLogger.add("[WARNING] " + content);
      }

      @Override
      public void error(String content) {
        infoLogger.add("[ERROR] " + content);
      }
    };
  }
}
