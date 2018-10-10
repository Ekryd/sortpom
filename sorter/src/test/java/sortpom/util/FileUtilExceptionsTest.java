package sortpom.util;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.ReflectionHelper;
import sortpom.exception.FailureException;

import java.io.File;

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

    private FileUtil createFileUtil() {
        ReflectionHelper helper = new ReflectionHelper(originalFileUtil);
        helper.setField("backupFile", backupFileMock);
        helper.setField("pomFile", pomFileMock);
        helper.setField("newName", "backupFileName");
        helper.setField("backupFileExtension", ".bak");
        return spy(originalFileUtil);
    }

}
