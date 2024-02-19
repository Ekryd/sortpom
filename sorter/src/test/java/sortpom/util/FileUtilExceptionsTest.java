package sortpom.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import refutils.ReflectionHelper;
import sortpom.exception.FailureException;

class FileUtilExceptionsTest {

  private File backupFileTemp;
  private File pomFileTemp;

  @BeforeEach
  void setup() throws IOException {
    pomFileTemp = File.createTempFile("pom", ".xml", new File("target"));
    pomFileTemp.deleteOnExit();
    backupFileTemp = File.createTempFile("backupFile", ".xml", new File("target"));
    backupFileTemp.deleteOnExit();
  }

  @Test
  void whenOldBackupFileCannotBeDeletedAnExceptionShouldBeThrown() {
    var fileUtil = createFileUtil();
    doNotAccessRealBackupFile(fileUtil);

    // Set backup file to a directory (which raises DirectoryNotEmptyException)
    new ReflectionHelper(fileUtil).setField("backupFile", backupFileTemp.getParentFile());

    Executable testMethod = fileUtil::backupFile;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not remove old backup file, filename: backupFileName")));
  }

  private void doNotAccessRealBackupFile(FileUtil fileUtil) {
    doNothing().when(fileUtil).createFileHandle();
  }

  @Test
  void whenSourceFileCannotBeCopiedAnExceptionShouldBeThrown() {
    assertTrue(pomFileTemp.delete());

    var fileUtil = createFileUtil();

    Executable testMethod = fileUtil::backupFile;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(
            equalTo(
                "Could not create backup file to filename: "
                    + pomFileTemp.getAbsolutePath()
                    + ".bak")));
  }

  @Test
  void whenPomFileCannotBeReadAnExceptionShouldBeThrown() {
    assertTrue(pomFileTemp.delete());

    var fileUtil = createFileUtil();
    new ReflectionHelper(fileUtil).setField("pomFile", pomFileTemp);

    Executable testMethod = fileUtil::getPomFileContent;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not read pom file: " + pomFileTemp.getAbsolutePath())));
  }

  @Test
  void whenPomFileHasWrongEncodingAnExceptionShouldBeThrown() {
    var fileUtil = createFileUtil();

    new ReflectionHelper(fileUtil).setField("encoding", "gurka-2000");

    Executable testMethod = fileUtil::getPomFileContent;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not handle encoding: gurka-2000")));
  }

  @Test
  void whenPomFileCannotBeSavedAnExceptionShouldBeThrown() {
    assertTrue(pomFileTemp.setReadOnly());

    var fileUtil = createFileUtil();

    Executable testMethod = () -> fileUtil.savePomFile("Whatever");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(equalTo("Could not save sorted pom file: " + pomFileTemp.getAbsolutePath())));
    assertTrue(pomFileTemp.setReadable(true));
  }

  @Test
  void whenPomFileTimestampCannotBeRetrievedAnExceptionShouldBeThrown() {
    var fileUtil = createFileUtil();
    new ReflectionHelper(fileUtil).setField("keepTimestamp", true);

    Executable testMethod = fileUtil::getPomFileContent;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(
            equalTo(
                "Could not retrieve the timestamp of the pom file: "
                    + pomFileTemp.getAbsolutePath())));
  }

  @Test
  void whenPomFileTimestampCannotBeSetAnExceptionShouldBeThrown() {
    var fileUtil = createFileUtil();
    new ReflectionHelper(fileUtil).setField("keepTimestamp", true);

    Executable testMethod = () -> fileUtil.savePomFile("Whatever");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        "Unexpected message",
        thrown.getMessage(),
        is(
            equalTo(
                "Could not change timestamp of new pom file: " + pomFileTemp.getAbsolutePath())));
  }

  private FileUtil createFileUtil() {
    var originalFileUtil = new FileUtil();

    var helper = new ReflectionHelper(originalFileUtil);
    helper.setField("pomFile", pomFileTemp);
    helper.setField("newName", "backupFileName");
    helper.setField("backupFileExtension", ".bak");
    helper.setField("encoding", "UTF-8");
    helper.setField(new FileAttributeUtilStub());
    return spy(originalFileUtil);
  }

  private static class FileAttributeUtilStub extends FileAttributeUtil {
    @Override
    public long getLastModifiedTimestamp(File file) {
      return 0;
    }

    @Override
    public void setTimestamps(File file, long millis) throws IOException {
      throw new IOException();
    }
  }
}
