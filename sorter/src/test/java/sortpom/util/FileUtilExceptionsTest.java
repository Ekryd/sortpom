package sortpom.util;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.ReflectionHelper;
import sortpom.exception.FailureException;

import java.io.File;

import static org.mockito.Mockito.*;

public class FileUtilExceptionsTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private File backupFileMock = mock(File.class);

    private File pomFileMock = mock(File.class);

    private FileUtil originalFileUtil;

    @Before
    public void setup() {
        originalFileUtil = new FileUtil();
    }

    @Test
    public void whenOldBackupFileCannotBeDeletedAnExceptionShouldBeThrown() throws Exception {
        FileUtil fileUtil = createFileUtil();
        doNotAccessRealBackupFile(fileUtil);

        when(backupFileMock.exists()).thenReturn(true);
        when(backupFileMock.delete()).thenReturn(false);

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not remove old backup file, filename: backupFileName");

        fileUtil.backupFile();
    }

    @Ignore("This test does not work under JDK7 due to internal JDK changes")
    @Test
    public void whenSourceFileCannotBeCopiedAnExceptionShouldBeThrown() throws Exception {
        FileUtil fileUtil = createFileUtil();
        doNotAccessRealBackupFile(fileUtil);

        when(backupFileMock.exists()).thenReturn(true);
        when(backupFileMock.delete()).thenReturn(true);
        when(pomFileMock.getPath()).thenReturn("gurka");

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not create backup file to filename: backupFileName");

        fileUtil.backupFile();
    }

    @Ignore("This test does not work under JDK7 due to internal JDK changes")
    @Test
    public void whenPomFileCannotBeReadAnExceptionShouldBeThrown() throws Exception {
        FileUtil fileUtil = createFileUtil();
        doNotAccessRealBackupFile(fileUtil);

        when(pomFileMock.getPath()).thenReturn("gurka");
        when(pomFileMock.getAbsolutePath()).thenReturn("pomFileName");

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not read pom file: pomFileName");

        fileUtil.getPomFileContent();
    }

    @Ignore("This test does not work under JDK7 due to internal JDK changes")
    @Test
    public void whenPomFileCannotBeSavedAnExceptionShouldBeThrown() throws Exception {
        FileUtil fileUtil = createFileUtil();
        doNotAccessRealBackupFile(fileUtil);

        when(pomFileMock.getPath()).thenReturn("/\\");
        when(pomFileMock.getAbsolutePath()).thenReturn("pomFileName");

        thrown.expect(FailureException.class);
        thrown.expectMessage("Could not save sorted pom file: pomFileName");

        fileUtil.savePomFile(null);
    }

    private FileUtil createFileUtil() throws NoSuchFieldException, IllegalAccessException {
        ReflectionHelper helper = new ReflectionHelper(originalFileUtil);
        helper.setField("backupFile", backupFileMock);
        helper.setField("pomFile", pomFileMock);
        helper.setField("newName", "backupFileName");
        return spy(originalFileUtil);
    }

    private void doNotAccessRealBackupFile(FileUtil fileUtil) {
        doNothing().when(fileUtil).createFileHandle();
    }

}
