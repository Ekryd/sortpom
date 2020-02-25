package sortpom.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;
import sortpom.SortPomImpl;
import sortpom.VerifyMojo;
import sortpom.XmlOutputGenerator;
import sortpom.parameter.VerifyFailType;
import sortpom.util.FileUtil;
import sortpom.wrapper.ElementWrapperCreator;
import sortpom.wrapper.TextWrapperCreator;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;

public class VerifyMojoParametersTest {
    private final File pomFile = mock(File.class);

    private SortPomImpl sortPomImpl;
    private FileUtil fileUtil;
    private VerifyMojo verifyMojo;
    private ElementWrapperCreator elementWrapperCreator;
    private TextWrapperCreator textWrapperCreator;
    private XmlOutputGenerator xmlOutputGenerator;

    @BeforeEach
    public void setup() throws SecurityException, IllegalArgumentException {
        verifyMojo = new VerifyMojo();
        new ReflectionHelper(verifyMojo).setField("lineSeparator", "\n");
        new ReflectionHelper(verifyMojo).setField("encoding", "UTF-8");
        new ReflectionHelper(verifyMojo).setField("verifyFail", "SORT");

        sortPomImpl = new ReflectionHelper(verifyMojo).getField(SortPomImpl.class);
        ReflectionHelper sortPomImplHelper = new ReflectionHelper(sortPomImpl);
        fileUtil = sortPomImplHelper.getField(FileUtil.class);
        xmlOutputGenerator = sortPomImplHelper.getField(XmlOutputGenerator.class);
        WrapperFactoryImpl wrapperFactoryImpl = sortPomImplHelper.getField(WrapperFactoryImpl.class);
        elementWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(ElementWrapperCreator.class);
        textWrapperCreator = new ReflectionHelper(wrapperFactoryImpl).getField(TextWrapperCreator.class);
    }

    @Test
    public void pomFileParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("pomFile", pomFile, sortPomImpl, fileUtil);
    }

    @Test
    public void createBackupFileParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("createBackupFile", sortPomImpl);
    }

    @Test
    public void keepTimestampParameter() throws Exception {
    	testParameterMoveFromMojoToRestOfApplicationForBoolean("keepTimestamp", fileUtil);
    }

    @Test
    public void backupFileExtensionParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("backupFileExtension", ".gurka", sortPomImpl, fileUtil);
    }

    @Test
    public void violationFilenameParameterShouldBeFound() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("violationFilename", "violets.are", sortPomImpl, fileUtil);
    }

    @Test
    public void encodingParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("encoding", "GURKA-2000", fileUtil, sortPomImpl, xmlOutputGenerator);
    }

    @Test
    public void lineSeparatorParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("lineSeparator", "\r");

        final Object lineSeparatorUtil = new ReflectionHelper(xmlOutputGenerator).getField("lineSeparatorUtil");

        assertThat("\r", lineSeparatorUtil.toString(), is(equalTo("\r")));
    }

    @Test
    public void parameterNrOfIndentSpaceShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("nrOfIndentSpace", 6);

        assertThat(new ReflectionHelper(xmlOutputGenerator).getField("indentCharacters"), is(equalTo("      ")));
    }

    @Test
    public void expandEmptyElementsParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("expandEmptyElements", xmlOutputGenerator);
    }

    @Test
    public void predefinedSortOrderParameter() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("predefinedSortOrder", "tomatoSort", fileUtil);
    }

    @Test
    public void parameterSortOrderFileShouldEndUpInFileUtil() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("sortOrderFile", "sortOrderFile.gurka");

        Object actual = new ReflectionHelper(fileUtil).getField("customSortOrderFile");
        assertThat(actual, is(equalTo("sortOrderFile.gurka")));
    }

    @Test
    public void parameterSortDependenciesShouldEndUpInElementWrapperCreator() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("sortDependencies", "groupId,scope");

        Object sortDependencies = new ReflectionHelper(elementWrapperCreator).getField("sortDependencies");
        assertThat(sortDependencies.toString(), is("DependencySortOrder{childElementNames=[groupId, scope]}"));
    }

    @Test
    public void parameterSortPluginsShouldEndUpInWrapperFactoryImpl() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("sortPlugins", "alfa,beta");

        Object sortDependencies = new ReflectionHelper(elementWrapperCreator).getField("sortPlugins");
        assertThat(sortDependencies.toString(), is("DependencySortOrder{childElementNames=[alfa, beta]}"));
    }

    @Test
    public void parameterSortModulesShouldEndUpInWrapperFactoryImpl() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("sortModules", elementWrapperCreator);
    }

    @Test
    public void parameterSortPropertiesShouldEndUpInWrapperFactoryImpl() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("sortProperties", elementWrapperCreator);
    }

    @Test
    public void parameterKeepBlankLineShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("keepBlankLines", textWrapperCreator);
    }

    @Test
    public void parameterIndentBlankLineShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplicationForBoolean("indentBlankLines", xmlOutputGenerator);
    }

    @Test
    public void parameterVerifyFailShouldEndUpInXmlProcessor() throws Exception {
        testParameterMoveFromMojoToRestOfApplication("verifyFail", "STOP");

        final Object verifyFailType = new ReflectionHelper(sortPomImpl).getField("verifyFailType");

        assertThat(verifyFailType, is(equalTo(VerifyFailType.STOP)));
    }

    private void testParameterMoveFromMojoToRestOfApplication(String parameterName, Object parameterValue,
                                                              Object... whereParameterCanBeFound) throws
            Exception {
        new ReflectionHelper(verifyMojo).setField(parameterName, parameterValue);

        verifyMojo.setup();

        for (Object someInstanceThatContainparameter : whereParameterCanBeFound) {
            Object actual = new ReflectionHelper(someInstanceThatContainparameter).getField(parameterName);

            assertThat(actual, is(equalTo(parameterValue)));
        }
    }

    private void testParameterMoveFromMojoToRestOfApplicationForBoolean(String parameterName,
                                                                        Object... whereParameterCanBeFound) throws
            Exception {
        new ReflectionHelper(verifyMojo).setField(parameterName, true);

        verifyMojo.setup();

        for (Object someInstanceThatContainparameter : whereParameterCanBeFound) {
            Object actual = new ReflectionHelper(someInstanceThatContainparameter).getField(parameterName);

            assertThat(actual, is(equalTo(true)));
        }
    }

}
