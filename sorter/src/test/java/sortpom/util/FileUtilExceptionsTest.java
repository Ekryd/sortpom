package sortpom.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.ReflectionHelper;
import sortpom.exception.FailureException;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class FileUtilExceptionsTest {
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private final File backupFileMock = mock(File.class);

    private final File pomFileMock = mock(File.class);

    private FileUtil originalFileUtil;

    @Before
    public void setup() {
        originalFileUtil = new FileUtil();
    }

    @Test
    public void whenOldBackupFileCannotBeDeletedAnExceptionShouldBeThrown() {
        FileUtil fileUtil = createFileUtil();
        doNotAccessRealBackupFile(fileUtil);

        when(backupFileMock.exists()).thenReturn(true);
        when(backupFileMock.delete()).thenReturn(false);

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not remove old backup file, filename: backupFileName");

        fileUtil.backupFile();
    }

    private void doNotAccessRealBackupFile(FileUtil fileUtil) {
        doNothing().when(fileUtil).createFileHandle();
    }

    @Test
    public void whenSourceFileCannotBeCopiedAnExceptionShouldBeThrown() throws Exception {
        File tempFile = File.createTempFile("pom", ".xml", new File("target"));
        tempFile.delete();

        FileUtil fileUtil = createFileUtil();
        new ReflectionHelper(fileUtil).setField("pomFile", tempFile);

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not create backup file to filename: " + tempFile.getAbsolutePath() + ".bak");

        fileUtil.backupFile();
    }

    @Test
    public void whenPomFileCannotBeReadAnExceptionShouldBeThrown() throws Exception {
        File tempFile = File.createTempFile("pom", ".xml", new File("target"));
        tempFile.delete();

        FileUtil fileUtil = createFileUtil();
        new ReflectionHelper(fileUtil).setField("pomFile", tempFile);

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not read pom file: " + tempFile.getAbsolutePath());

        fileUtil.getPomFileContent();
    }

    @Test
    public void whenPomFileHasWrongEncodingAnExceptionShouldBeThrown() throws Exception {
        File tempFile = File.createTempFile("pom", ".xml", new File("target"));

        FileUtil fileUtil = createFileUtil();
        new ReflectionHelper(fileUtil).setField("pomFile", tempFile);
        new ReflectionHelper(fileUtil).setField("encoding", "gurka-2000");

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not handle encoding: gurka-2000");

        fileUtil.getPomFileContent();
    }

    @Test
    public void whenPomFileCannotBeSavedAnExceptionShouldBeThrown() throws Exception {
        File tempFile = File.createTempFile("pom", ".xml", new File("target"));
        tempFile.setReadOnly();

        FileUtil fileUtil = createFileUtil();
        new ReflectionHelper(fileUtil).setField("pomFile", tempFile);

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not save sorted pom file: " + tempFile.getAbsolutePath());

        fileUtil.savePomFile(null);
    }

    @Test
    public void whenPomFileTimestampCannotBeRetrievedAnExceptionShouldBeThrown() throws Exception {
      File pomFile = new File("src/test/resources/full_unsorted_input.xml");

      FileUtil fileUtil = createFileUtil();
      new ReflectionHelper(fileUtil).setField("pomFile", pomFile);
      new ReflectionHelper(fileUtil).setField("keepTimestamp", true);
      new ReflectionHelper(fileUtil).setField("fileAttrUtils", new FileAttributeUtilTest());

      thrown.expect(FailureException.class);
      thrown.expectMessage("Cound not retrieve the timestamp of the pom file: " + pomFile.getAbsolutePath());

      fileUtil.getPomFileContent();
    }

    @Test
    public void whenPomFileTimestampCannotBeSetAnExceptionShouldBeThrown() throws Exception {
      File tempFile = File.createTempFile("pom", ".xml", new File("target"));

      FileUtil fileUtil = createFileUtil();
      new ReflectionHelper(fileUtil).setField("pomFile", tempFile);
      new ReflectionHelper(fileUtil).setField("keepTimestamp", true);
      new ReflectionHelper(fileUtil).setField("fileAttrUtils", new FileAttributeUtilTest());

      thrown.expect(FailureException.class);
      thrown.expectMessage("Could not change timestamp of new pom file: " + tempFile.getAbsolutePath());

      fileUtil.savePomFile(null);
    }

    private FileUtil createFileUtil() {
        ReflectionHelper helper = new ReflectionHelper(originalFileUtil);
        helper.setField("backupFile", backupFileMock);
        helper.setField("pomFile", pomFileMock);
        helper.setField("newName", "backupFileName");
        helper.setField("backupFileExtension", ".bak");
        return spy(originalFileUtil);
    }

    private class FileAttributeUtilTest extends FileAttributeUtil {
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
