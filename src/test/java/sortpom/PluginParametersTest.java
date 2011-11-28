package sortpom;

import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;
import org.mockito.*;

import sortpom.util.*;
import sortpom.wrapper.*;

public class PluginParametersTest {
	@Mock
	private File pomFile;

	private SortPomImpl sortPomImpl;
	private FileUtil fileUtil;
	private SortPomMojo mojo;
	private XmlProcessor xmlProcessor;
	private WrapperFactoryImpl wrapperFactoryImpl;

	@Before
	public void setup() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
			IllegalAccessException {
		MockitoAnnotations.initMocks(this);
		mojo = new SortPomMojo();
		new ReflectionHelper(mojo).setField("lineSeparator", "\n");

		sortPomImpl = new ReflectionHelper(mojo).getField(SortPomImpl.class);
		fileUtil = new ReflectionHelper(sortPomImpl).getField(FileUtil.class);
		xmlProcessor = new ReflectionHelper(sortPomImpl).getField(XmlProcessor.class);
		wrapperFactoryImpl = new ReflectionHelper(sortPomImpl).getField(WrapperFactoryImpl.class);
	}

	@Test
	public void parameterPomFileShouldEndUpInFileUtil() throws Exception {
		new ReflectionHelper(mojo).setField("pomFile", pomFile);

		mojo.setup();

		Object actual = new ReflectionHelper(fileUtil).getField("pomFile");
		assertSame(pomFile, actual);
	}

	@Test
	public void parameterCreateBackupFileShouldEndUpInSortPomImpl() throws Exception {
		new ReflectionHelper(mojo).setField("createBackupFile", true);

		mojo.setup();

		assertSame(true, new ReflectionHelper(sortPomImpl).getField(PluginParameters.class).createBackupFile);
	}

	@Test
	public void parameterBackupFileExtensionShouldEndUpInFileUtil() throws Exception {
		new ReflectionHelper(mojo).setField("backupFileExtension", ".gurka");

		mojo.setup();

		Object actual = new ReflectionHelper(fileUtil).getField("backupFileExtension");
		assertEquals(".gurka", actual);
	}

	@Test
	public void parameterEncodingShouldEndUpInLostOfPlaces() throws Exception {
		new ReflectionHelper(mojo).setField("encoding", "GURKA-2000");

		mojo.setup();

		assertEquals("GURKA-2000", new ReflectionHelper(fileUtil).getField("encoding"));
		assertEquals("GURKA-2000", new ReflectionHelper(sortPomImpl).getField(PluginParameters.class).encoding);
		assertEquals("GURKA-2000", new ReflectionHelper(xmlProcessor).getField("encoding"));
	}

	@Test
	public void parameterLineSeparatorShouldEndUpInXmlProcessor() throws Exception {
		new ReflectionHelper(mojo).setField("lineSeparator", "\r");

		mojo.setup();

		assertEquals("\r", new ReflectionHelper(xmlProcessor).getField("lineSeparatorUtil").toString());
	}

	@Test
	public void parameterNrOfIndentSpaceShouldEndUpInXmlProcessor() throws Exception {
		new ReflectionHelper(mojo).setField("nrOfIndentSpace", 6);

		mojo.setup();

		assertEquals("      ", new ReflectionHelper(xmlProcessor).getField("indentCharacters"));
	}

	@Test
	public void parameterSortOrderFileShouldEndUpInFileUtil() throws Exception {
		new ReflectionHelper(mojo).setField("sortOrderFile", ".gurka");

		mojo.setup();

		Object actual = new ReflectionHelper(fileUtil).getField("defaultOrderFileName");
		assertEquals(".gurka", actual);
	}

	@Test
	public void parameterSortDependenciesShouldEndUpInWrapperFactoryImpl() throws Exception {
		new ReflectionHelper(mojo).setField("sortDependencies", true);

		mojo.setup();

		Object actual = new ReflectionHelper(wrapperFactoryImpl).getField("sortDependencies");
		assertEquals(true, actual);
	}

	@Test
	public void parameterSortPluginsShouldEndUpInWrapperFactoryImpl() throws Exception {
		new ReflectionHelper(mojo).setField("sortPlugins", true);

		mojo.setup();

		Object actual = new ReflectionHelper(wrapperFactoryImpl).getField("sortPlugins");
		assertEquals(true, actual);
	}

}
