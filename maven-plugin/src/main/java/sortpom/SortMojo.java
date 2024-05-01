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
                          nrOfIndentSpace, indentBlankLines, indentSchemaLocation, indentAttribute)
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
}
