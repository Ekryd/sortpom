package sortpom;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import sortpom.exception.ExceptionConverter;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;

/**
 * Sorts the pom.xml for a Maven project.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "sort", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
@SuppressWarnings({"UnusedDeclaration"})
public class SortMojo extends AbstractParentMojo {

  public void setup(SortPomLogger mavenLogger) throws MojoFailureException {
    warnAboutDeprecatedArguments(mavenLogger, indentSchemaLocation);
    new ExceptionConverter(
            () -> {
              var pluginParameters =
                  PluginParameters.builder()
                      .setPomFile(pomFile)
                      .setFileOutput(createBackupFile, backupFileExtension, null, keepTimestamp)
                      .setEncoding(encoding)
                      .setFormatting(
                          lineSeparator,
                          expandEmptyElements,
                          spaceBeforeCloseEmptyElement,
                          keepBlankLines,
                          endWithNewline)
                      .setIndent(
                          nrOfIndentSpace,
                          indentBlankLines,
                          getProcessedIndentAttribute(indentAttribute, indentSchemaLocation))
                      .setSortOrder(sortOrderFile, predefinedSortOrder)
                      .setSortEntities(
                          sortDependencies,
                          sortDependencyExclusions,
                          sortDependencyManagement,
                          sortPlugins,
                          sortProperties,
                          sortModules,
                          sortExecutions)
                      .setIgnoreLineSeparators(ignoreLineSeparators)
                      .build();

              sortPomImpl.setup(mavenLogger, pluginParameters);
            })
        .executeAndConvertException();
  }

  protected void sortPom() throws MojoFailureException {
    new ExceptionConverter(sortPomImpl::sortPom).executeAndConvertException();
  }

  static String getProcessedIndentAttribute(String indentAttribute, boolean indentSchemaLocation) {
    return indentAttribute != null
        ? indentAttribute
        : (indentSchemaLocation ? "schemaLocation" : null);
  }

  static void warnAboutDeprecatedArguments(SortPomLogger log, boolean indentSchemaLocation) {
    if (indentSchemaLocation) {
      log.warn(
          "[DEPRECATED] The parameter 'indentSchemaLocation' is no longer used. Please use <indentAttribute>schemaLocation</indentAttribute> instead. In the next major version, using 'indentSchemaLocation' will cause an error!");
    }
  }
}
