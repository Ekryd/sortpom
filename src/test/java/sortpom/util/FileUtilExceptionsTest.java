package sortpom.util;

import static org.mockito.Mockito.*;

import java.io.*;

import org.junit.*;
import org.junit.rules.*;
import org.mockito.*;

public class FileUtilExceptionsTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Mock
	private File backupFileMock;
	@Mock
	private File pomFileMock;

	private FileUtil originalFileUtil;

	@Before
	public void setup() {
		originalFileUtil = new FileUtil();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void whenOldBackupFileCannotBeDeletedAnExceptionShouldBeThrown() throws Exception {
		FileUtil fileUtil = createFileUtil();
		doNotAccessRealBackupFile(fileUtil);

		when(backupFileMock.exists()).thenReturn(true);
		when(backupFileMock.delete()).thenReturn(false);

		thrown.expectMessage("Could not remove old backup file, filename: backupFileName");

		fileUtil.backupFile();
	}

	@Test
	public void whenSourceFileCannotBeCopiedAnExceptionShouldBeThrown() throws Exception {
		FileUtil fileUtil = createFileUtil();
		doNotAccessRealBackupFile(fileUtil);

		when(backupFileMock.exists()).thenReturn(true);
		when(backupFileMock.delete()).thenReturn(true);
		when(pomFileMock.getPath()).thenReturn("gurka");

		thrown.expectMessage("Could not create backup file to filename: backupFileName");

		fileUtil.backupFile();
	}

	@Test
	public void whenPomFileCannotBeReadAnExceptionShouldBeThrown() throws Exception {
		FileUtil fileUtil = createFileUtil();
		doNotAccessRealBackupFile(fileUtil);

		when(pomFileMock.getPath()).thenReturn("gurka");
		when(pomFileMock.getAbsolutePath()).thenReturn("pomFileName");

		thrown.expectMessage("Could not read pomfile: pomFileName");

		fileUtil.getPomFileContent();
	}

	@Test
	public void whenPomFileCannotBeSavedAnExceptionShouldBeThrown() throws Exception {
		FileUtil fileUtil = createFileUtil();
		doNotAccessRealBackupFile(fileUtil);

		when(pomFileMock.getPath()).thenReturn("/\\");
		when(pomFileMock.getAbsolutePath()).thenReturn("pomFileName");

		thrown.expectMessage("Could not save sorted pomfile: pomFileName");

		fileUtil.savePomFile(null);
	}

	private FileUtil createFileUtil() throws NoSuchFieldException, IllegalAccessException {
		ReflectionHelper helper = new ReflectionHelper(originalFileUtil);
		helper.setField("backupFile", backupFileMock);
		helper.setField("pomFile", pomFileMock);
		helper.setField("newName", "backupFileName");
		FileUtil fileUtil = spy(originalFileUtil);
		return fileUtil;
	}

	private void doNotAccessRealBackupFile(FileUtil fileUtil) {
		doNothing().when(fileUtil).createFileHandle();
	}

}
