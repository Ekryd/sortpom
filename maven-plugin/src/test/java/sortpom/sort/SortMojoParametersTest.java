package sortpom.sort;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import refutils.ReflectionHelper;
import sortpom.SortMojo;
import sortpom.SortPomImpl;
import sortpom.SortPomService;
import sortpom.logger.MavenLogger;
import sortpom.logger.SortPomLogger;
import sortpom.output.XmlOutputGenerator;
import sortpom.processinstruction.XmlProcessingInstructionParser;
import sortpom.util.FileUtil;
import sortpom.wrapper.ElementWrapperCreator;
import sortpom.wrapper.TextWrapperCreator;
import sortpom.wrapper.WrapperFactoryImpl;

class SortMojoParametersTest {
  private final File pomFile = mock(File.class);

  private SortPomImpl sortPomImpl;
  private SortPomService sortPomService;
  private FileUtil fileUtil;
  private SortMojo sortMojo;
  private ElementWrapperCreator elementWrapperCreator;
  private TextWrapperCreator textWrapperCreator;
  private XmlOutputGenerator xmlOutputGenerator;
  private XmlProcessingInstructionParser xmlProcessingInstructionParser;

  @BeforeEach
  void setup() throws SecurityException, IllegalArgumentException {
    sortMojo = new SortMojo();
    new ReflectionHelper(sortMojo).setField("lineSeparator", "\n");
    new ReflectionHelper(sortMojo).setField("encoding", "UTF-8");

    sortPomImpl = new ReflectionHelper(sortMojo).getField(SortPomImpl.class);
    sortPomService = new ReflectionHelper(sortPomImpl).getField(SortPomService.class);
    var sortPomServiceHelper = new ReflectionHelper(sortPomService);
    fileUtil = sortPomServiceHelper.getField(FileUtil.class);
    xmlOutputGenerator = sortPomServiceHelper.getField(XmlOutputGenerator.class);
    var wrapperFactoryImpl = sortPomServiceHelper.getField(WrapperFactoryImpl.class);
    xmlProcessingInstructionParser =
        sortPomServiceHelper.getField(XmlProcessingInstructionParser.class);

    elementWrapperCreator =
        new ReflectionHelper(wrapperFactoryImpl).getField(ElementWrapperCreator.class);
    textWrapperCreator =
        new ReflectionHelper(wrapperFactoryImpl).getField(TextWrapperCreator.class);
  }

  @Test
  void pomFileParameter() {
    assertParameterMoveFromMojoToRestOfApplication("pomFile", pomFile, sortPomImpl, fileUtil);
  }

  @Test
  void createBackupFileParameter() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean("createBackupFile", sortPomService);
  }

  @Test
  void keepTimestampParameter() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean("keepTimestamp", fileUtil);
  }

  @Test
  void backupFileExtensionParameter() {
    assertParameterMoveFromMojoToRestOfApplication(
        "backupFileExtension", ".gurka", sortPomService, fileUtil);
  }

  @Test
  void encodingParameter() {
    assertParameterMoveFromMojoToRestOfApplication(
        "encoding", "GURKA-2000", fileUtil, sortPomService, xmlOutputGenerator);
  }

  @Test
  void lineSeparatorParameter() {
    assertParameterMoveFromMojoToRestOfApplication("lineSeparator", "\r");

    var lineSeparator =
        new ReflectionHelper(xmlOutputGenerator).getField("lineSeparator").toString();

    assertThat(lineSeparator, is(equalTo("\r")));
  }

  @Test
  void parameterNrOfIndentSpaceShouldEndUpInXmlProcessor() {
    assertParameterMoveFromMojoToRestOfApplication("nrOfIndentSpace", 6);

    var indentCharacters = new ReflectionHelper(xmlOutputGenerator).getField("indentCharacters");

    assertThat(indentCharacters, is(equalTo("      ")));
  }

  @Test
  void expandEmptyElementsParameter() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean(
        "expandEmptyElements", xmlOutputGenerator);
  }

  @Test
  void spaceBeforeCloseEmptyElementParameter() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean(
        "spaceBeforeCloseEmptyElement", xmlOutputGenerator);
  }

  @Test
  void predefinedSortOrderParameter() {
    assertParameterMoveFromMojoToRestOfApplication("predefinedSortOrder", "tomatoSort", fileUtil);
  }

  @Test
  void parameterSortOrderFileShouldEndUpInFileUtil() {
    assertParameterMoveFromMojoToRestOfApplication("sortOrderFile", "sortOrderFile.gurka");

    var actual = new ReflectionHelper(fileUtil).getField("customSortOrderFile");

    assertThat(actual, is(equalTo("sortOrderFile.gurka")));
  }

  @Test
  void parameterSortDependenciesShouldEndUpInElementWrapperCreator() {
    assertParameterMoveFromMojoToRestOfApplication("sortDependencies", "groupId,scope");

    var sortDependencies = new ReflectionHelper(elementWrapperCreator).getField("sortDependencies");
    assertThat(
        sortDependencies.toString(), is("DependencySortOrder{childElementNames=[groupId, scope]}"));
  }

  @Test
  void parameterSortDependencyManagementShouldEndUpInElementWrapperCreator() {
    assertParameterMoveFromMojoToRestOfApplication("sortDependencyManagement", "scope,groupId");

    var sortDependencies =
        new ReflectionHelper(elementWrapperCreator).getField("sortDependencyManagement");
    assertThat(
        sortDependencies.toString(), is("DependencySortOrder{childElementNames=[scope, groupId]}"));
  }

  @Test
  void parameterSortDependencyExclusionsShouldEndUpInElementWrapperCreator() {
    assertParameterMoveFromMojoToRestOfApplication("sortDependencyExclusions", "groupId,scope");

    var sortDependencyExclusions =
        new ReflectionHelper(elementWrapperCreator).getField("sortDependencyExclusions");
    assertThat(
        sortDependencyExclusions.toString(),
        is("DependencySortOrder{childElementNames=[groupId, scope]}"));
  }

  @Test
  void parameterSortPluginsShouldEndUpInWrapperFactoryImpl() {
    assertParameterMoveFromMojoToRestOfApplication("sortPlugins", "alfa,beta");

    var sortPlugins = new ReflectionHelper(elementWrapperCreator).getField("sortPlugins");
    assertThat(sortPlugins.toString(), is("DependencySortOrder{childElementNames=[alfa, beta]}"));
  }

  @Test
  void parameterSortModulesShouldEndUpInWrapperFactoryImpl() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean("sortModules", elementWrapperCreator);
  }

  @Test
  void parameterSortExecutionsShouldEndUpInWrapperFactoryImpl() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean(
        "sortExecutions", elementWrapperCreator);
  }

  @Test
  void parameterSortPropertiesShouldEndUpInWrapperFactoryImpl() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean(
        "sortProperties", elementWrapperCreator);
  }

  @Test
  void parameterKeepBlankLineShouldEndUpInTextWrapperCreator() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean("keepBlankLines", textWrapperCreator);
  }

  @Test
  void parameterEndWithNewlineShouldEndUpInXmlProcessor() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean("endWithNewline", xmlOutputGenerator);
  }

  @Test
  void parameterIndentBlankLineShouldEndUpInXmlProcessor() {
    assertParameterMoveFromMojoToRestOfApplicationForBoolean(
        "indentBlankLines", xmlOutputGenerator);
  }

  @Test
  void xmlProcessingInstructionParserShouldGetLogger() throws MojoFailureException {
    var expectedLogger = new MavenLogger(null, false);
    sortMojo.setup(expectedLogger);
    var logger = new ReflectionHelper(xmlProcessingInstructionParser).getField(SortPomLogger.class);
    assertThat(logger, not(nullValue()));
    assertThat(logger, sameInstance(expectedLogger));
  }

  @Test
  void indentSchemaLocationShouldWarnForDeprecation() throws MojoFailureException {
    var logger = mock(SortPomLogger.class);
    new ReflectionHelper(sortMojo).setField("indentSchemaLocation", true);
    sortMojo.setup(logger);
    verify(logger)
        .warn(
            "[DEPRECATED] The parameter 'indentSchemaLocation' is no longer supported. Please use <indentAttribute>schemaLocation</indentAttribute> instead. In the next major version, using 'indentSchemaLocation' will cause an error!");
  }

  private void assertParameterMoveFromMojoToRestOfApplication(
      String parameterName, Object parameterValue, Object... whereParameterCanBeFound) {
    new ReflectionHelper(sortMojo).setField(parameterName, parameterValue);

    try {
      sortMojo.setup(null);
    } catch (MojoFailureException e) {
      throw new RuntimeException(e);
    }

    for (var someInstanceThatContainParameter : whereParameterCanBeFound) {
      var actual = new ReflectionHelper(someInstanceThatContainParameter).getField(parameterName);

      assertThat(actual, is(equalTo(parameterValue)));
    }
  }

  private void assertParameterMoveFromMojoToRestOfApplicationForBoolean(
      String parameterName, Object... whereParameterCanBeFound) {
    new ReflectionHelper(sortMojo).setField(parameterName, true);

    try {
      sortMojo.setup(null);
    } catch (MojoFailureException e) {
      throw new RuntimeException(e);
    }

    for (var someInstanceThatContainParameter : whereParameterCanBeFound) {
      var actual =
          (boolean) new ReflectionHelper(someInstanceThatContainParameter).getField(parameterName);

      assertThat(actual, is(equalTo(true)));
    }
  }
}
