package sortpom.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

  private final File backupFile;
  private final String encoding;

  TestHandler(String inputResourceFileName, PluginParameters pluginParameters) {
    this(inputResourceFileName, null, pluginParameters);
  }

  TestHandler(
      String inputResourceFileName,
      String expectedResourceFileName,
      PluginParameters pluginParameters) {
    this.inputResourceFileName = "src/test/resources/" + inputResourceFileName;
    this.expectedResourceFileName = "src/test/resources/" + expectedResourceFileName;
    this.pluginParameters = pluginParameters;
    this.encoding = pluginParameters.encoding;
    this.testpom = pluginParameters.pomFile;
    backupFile = new File(testpom.getAbsolutePath() + pluginParameters.backupFileExtension);
    sortPomImpl.setup(createDummyLog(), pluginParameters);
  }

  List<String> getInfoLogger() {
    return infoLogger;
  }

  void performSortThatSorted() {
    removeOldTemporaryFiles();

    try {
      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    performSorting();

    assertTrue(testpom.exists());
    assertTrue(backupFile.exists());

    try (var backupFileInputStream = new FileInputStream(backupFile);
        var originalPomInputStream = new FileInputStream(inputResourceFileName);
        var actualSortedPomInputStream = new FileInputStream(testpom);
        var expectedSortedPomInputStream = new FileInputStream(expectedResourceFileName)) {
      var actualBackup = new String(backupFileInputStream.readAllBytes(), encoding);
      var expectedBackup = new String(originalPomInputStream.readAllBytes(), encoding);

      assertEquals(expectedBackup, actualBackup);

      var actualSorted = new String(actualSortedPomInputStream.readAllBytes(), encoding);
      var expectedSorted = new String(expectedSortedPomInputStream.readAllBytes(), encoding);

      assertEquals(expectedSorted, actualSorted);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  void performVerifyThatSorted() {
    removeOldTemporaryFiles();

    try {
      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    performVerifyWithSort();

    assertTrue(testpom.exists());

    if (pluginParameters.createBackupFile) {
      assertTrue(backupFile.exists());
      try (var backupFileInputStream = new FileInputStream(backupFile);
          var originalPomInputStream = new FileInputStream(inputResourceFileName)) {
        var actualBackup = new String(backupFileInputStream.readAllBytes(), encoding);
        var expectedBackup = new String(originalPomInputStream.readAllBytes(), encoding);

        assertEquals(expectedBackup, actualBackup);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    try (var actualSortedPomInputStream = new FileInputStream(testpom);
        var expectedSortedPomInputStream = new FileInputStream(expectedResourceFileName)) {
      var actualSorted = new String(actualSortedPomInputStream.readAllBytes(), encoding);
      var expectedSorted = new String(expectedSortedPomInputStream.readAllBytes(), encoding);

      assertEquals(expectedSorted, actualSorted);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  void performSortThatDidNotSort() {
    removeOldTemporaryFiles();

    try {
      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    performSorting();

    assertTrue(testpom.exists());
    assertFalse(backupFile.exists(), "No sort expected, backup file exists");

    try (var actualSortedPomInputStream = new FileInputStream(testpom);
        var expectedSortedPomInputStream = new FileInputStream(expectedResourceFileName)) {
      var actualSorted = new String(actualSortedPomInputStream.readAllBytes(), encoding);
      var expectedSorted = new String(expectedSortedPomInputStream.readAllBytes(), encoding);

      assertEquals(expectedSorted, actualSorted);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  private void performSorting() {
    sortPomImpl.sortPom();
  }

  XmlOrderedResult performVerify() {
    try {
      removeOldTemporaryFiles();
      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
      var verifyOk = isVerifyOk();

      assertTrue(testpom.exists());
      return verifyOk;
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  void performVerifyThatDidNotSort() {
    removeOldTemporaryFiles();

    try {
      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    performVerifyWithSort();

    assertTrue(testpom.exists());
    assertFalse(backupFile.exists());

    try (var actualSortedPomInputStream = new FileInputStream(testpom);
        var expectedSortedPomInputStream = new FileInputStream(expectedResourceFileName)) {
      var actualSorted = new String(actualSortedPomInputStream.readAllBytes(), encoding);
      var expectedSorted = new String(expectedSortedPomInputStream.readAllBytes(), encoding);

      assertEquals(expectedSorted, actualSorted);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  void performSortThatTestsTimestamps() {
    try {
      removeOldTemporaryFiles();

      Files.copy(Paths.get(inputResourceFileName), testpom.toPath());
      var pomTimestamp = testpom.lastModified();
      performSorting();

      if (pluginParameters.keepTimestamp) {
        assertThat(testpom.lastModified(), is(pomTimestamp));
        // Do not assert anything about the backup file, since that timestamp is OS dependent
      } else {
        assertThat(testpom.lastModified(), greaterThan(pomTimestamp));
        // Do not assert anything about the backup file, since that timestamp is OS dependent
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      cleanupAfterTest();
    }
  }

  private void performVerifyWithSort() {
    sortPomImpl.verifyPom();
  }

  private XmlOrderedResult isVerifyOk()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    var getVerificationResult = SortPomImpl.class.getDeclaredMethod("getVerificationResult");
    getVerificationResult.setAccessible(true);

    return (XmlOrderedResult) getVerificationResult.invoke(sortPomImpl);
  }

  private void removeOldTemporaryFiles() {
    if (testpom.exists()) {
      assertTrue(testpom.delete());
    }
  }

  private void cleanupAfterTest() {
    if (testpom.exists()) {
      assertTrue(testpom.delete());
    }
    if (backupFile.exists()) {
      assertTrue(backupFile.delete());
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
