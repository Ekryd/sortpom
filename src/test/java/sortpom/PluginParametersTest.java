package sortpom;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sortpom.util.FileUtil;
import sortpom.util.ReflectionHelper;
import sortpom.wrapper.ElementWrapperCreator;
import sortpom.wrapper.TextWrapperCreator;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class PluginParametersTest {
    @Mock
    private File pomFile;

    private SortPomImpl sortPomImpl;
    private FileUtil fileUtil;
    private SortPomMojo mojo;
    private XmlProcessor xmlProcessor;
    private WrapperFactoryImpl wrapperFactoryImpl;
    private ElementWrapperCreator elementWrapperCreator;
    private TextWrapperCreator textWrapperCreator;

    @Before
    public void setup() throws SecurityException, IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException, MojoFailureException {
        MockitoAnnotations.initMocks(this);
        mojo = new SortPomMojo();
        new ReflectionHelper(mojo).setField("lineSeparator", "\n");

        sortPomImpl = new ReflectionHelper(mojo).getField(SortPomImpl.class);
        fileUtil = new ReflectionHelper(sortPomImpl).getField(FileUtil.class);
        xmlProcessor = new ReflectionHelper(sortPomImpl).getField(XmlProcessor.class);
        wrapperFactoryImpl = new ReflectionHelper(sortPomImpl).getField(WrapperFactoryImpl.class);
        elementWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(ElementWrapperCreator.class);
        textWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(TextWrapperCreator.class);
    }

    @Test
    public void pomFileParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("pomFile", pomFile, sortPomImpl, fileUtil);
    }

    @Test
    public void createBackupFileParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("createBackupFile", true, sortPomImpl);
    }

    @Test
    public void backupFileExtensionParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("backupFileExtension", ".gurka", sortPomImpl, fileUtil);
    }

    @Test
    public void encodingParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("encoding", "GURKA-2000", fileUtil, sortPomImpl, xmlProcessor);
    }

    @Test
    public void lineSeparatorParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("lineSeparator", "\r");

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
    public void expandEmptyElementsParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("expandEmptyElements", true, xmlProcessor);
    }

    @Test
    public void predefinedSortOrderParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("predefinedSortOrder", "tomatoSort", fileUtil);
    }

    @Test
    public void parameterSortOrderFileShouldEndUpInFileUtil() throws Exception {
        new ReflectionHelper(mojo).setField("sortOrderFile", "sortOrderFile.gurka");

        mojo.setup();

        Object actual = new ReflectionHelper(fileUtil).getField("customSortOrderFile");
        assertEquals("sortOrderFile.gurka", actual);
    }

    @Test
    public void parameterSortDependenciesShouldEndUpInElementWrapperCreator() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("sortDependencies", true, elementWrapperCreator);
    }

    @Test
    public void parameterSortPluginsShouldEndUpInWrapperFactoryImpl() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("sortPlugins", true, elementWrapperCreator);
    }

    @Test
    public void parameterSortPropertiesShouldEndUpInWrapperFactoryImpl() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("sortProperties", true, elementWrapperCreator);
    }

    @Test
    public void parameterKeepBlankLineShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("keepBlankLines", true, textWrapperCreator);
    }

    @Test
    public void parameterIndentBlankLineShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("indentBlankLines", true, xmlProcessor);
    }

    private void testParameterMoveFromMojoToRestOfApplication(String parameterName, Object parameterValue,
                                                              Object... whereParameterCanBeFound) throws NoSuchFieldException, IllegalAccessException,
            MojoFailureException {
        new ReflectionHelper(mojo).setField(parameterName, parameterValue);

        mojo.setup();

        for (Object someInstanceThatContainparameter : whereParameterCanBeFound) {
            Object actual = new ReflectionHelper(someInstanceThatContainparameter).getField(parameterName);
            assertSame(parameterValue, actual);
        }
    }

    private void testParameterMoveFromMojoToRestOfApplicationForBoolean(String parameterName, boolean parameterValue,
                                                                        Object... whereParameterCanBeFound) throws NoSuchFieldException, IllegalAccessException,
            MojoFailureException {
        new ReflectionHelper(mojo).setField(parameterName, parameterValue);

        mojo.setup();

        for (Object someInstanceThatContainparameter : whereParameterCanBeFound) {
            Object actual = new ReflectionHelper(someInstanceThatContainparameter).getField(parameterName);
            assertEquals(parameterValue, actual);
        }
    }

}
