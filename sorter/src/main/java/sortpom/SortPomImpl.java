package sortpom;

import java.io.File;
import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.VerifyFailOnType;
import sortpom.parameter.VerifyFailType;
import sortpom.util.XmlOrderedResult;

/** The implementation of the Mojo (Maven plugin) that sorts the pom file for a Maven project. */
public class SortPomImpl {
  private static final String TEXT_FILE_NOT_SORTED = "The file %s is not sorted";

  private final SortPomService sortPomService;

  private SortPomLogger log;
  private File pomFile;
  private VerifyFailType verifyFailType;
  private VerifyFailOnType verifyFailOn;

  public SortPomImpl() {
    this.sortPomService = new SortPomService();
  }

  public void setup(SortPomLogger log, PluginParameters pluginParameters) {
    this.log = log;
    this.pomFile = pluginParameters.pomFile;
    this.verifyFailType = pluginParameters.verifyFailType;
    this.verifyFailOn = pluginParameters.verifyFailOn;

    sortPomService.setup(log, pluginParameters);

    warnAboutDeprecatedArguments(pluginParameters);
  }

  private void warnAboutDeprecatedArguments(PluginParameters pluginParameters) {
    if (pluginParameters.sortDependencies.isDeprecatedValueTrue()) {
      throw new FailureException(
          "The 'true' value in 'sortDependencies' is no longer supported. Please use value 'groupId,artifactId' instead.");
    }
    if (pluginParameters.sortDependencies.isDeprecatedValueFalse()) {
      throw new FailureException(
          "The 'false' value in 'sortDependencies' is no longer supported. Please use empty value '' or omit sortDependencies instead.");
    }
    if (pluginParameters.sortDependencyExclusions.isDeprecatedValueTrue()) {
      throw new FailureException(
          "The 'true' value in 'sortDependencyExclusions' is no longer supported. Please use value 'groupId,artifactId' instead.");
    }
    if (pluginParameters.sortDependencyExclusions.isDeprecatedValueFalse()) {
      throw new FailureException(
          "The 'false' value in 'sortDependencyExclusions' is no longer supported. Please use empty value '' or omit sortDependencyExclusions instead.");
    }
    if (pluginParameters.sortPlugins.isDeprecatedValueTrue()) {
      throw new FailureException(
          "The 'true' value in 'sortPlugins' is no longer supported. Please use value 'groupId,artifactId' instead.");
    }
    if (pluginParameters.sortPlugins.isDeprecatedValueFalse()) {
      throw new FailureException(
          "The 'false' value in 'sortPlugins' is no longer supported. Please use an empty value '' or omit 'sortPlugins' instead.");
    }
    if (pluginParameters.indentSchemaLocation) {
      log.warn(
          "[DEPRECATED] The parameter 'indentSchemaLocation' is no longer supported. Please use <indentAttribute>schemaLocation</indentAttribute> instead. In the next major version, using 'indentSchemaLocation' will cause an error!");
    }
  }

  /** Sorts the pom file. */
  public void sortPom() {
    log.info("Sorting file " + pomFile.getAbsolutePath());

    sortPomService.sortOriginalXml();
    sortPomService.generateSortedXml();
    if (sortPomService.isOriginalXmlStringSorted().isOrdered()) {
      log.info("Pom file is already sorted, exiting");
      return;
    }
    sortPomService.createBackupFile();
    sortPomService.saveGeneratedXml();
    log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
  }

  /** Verify that the pom-file is sorted regardless of formatting */
  public void verifyPom() {
    var xmlOrderedResult = getVerificationResult();
    performVerfificationResult(xmlOrderedResult);
  }

  private XmlOrderedResult getVerificationResult() {
    log.info("Verifying file " + pomFile.getAbsolutePath());

    sortPomService.sortOriginalXml();

    XmlOrderedResult xmlOrderedResult;
    if (verifyFailOn == VerifyFailOnType.XMLELEMENTS) {
      xmlOrderedResult = sortPomService.isOriginalXmlElementsSorted();
    } else {
      sortPomService.generateSortedXml();
      xmlOrderedResult = sortPomService.isOriginalXmlStringSorted();
    }
    return xmlOrderedResult;
  }

  private void performVerfificationResult(XmlOrderedResult xmlOrderedResult) {
    if (!xmlOrderedResult.isOrdered()) {
      switch (verifyFailType) {
        case WARN:
          log.warn(xmlOrderedResult.getErrorMessage());
          sortPomService.saveViolationFile(xmlOrderedResult);
          log.warn(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
          break;
        case SORT:
          log.info(xmlOrderedResult.getErrorMessage());
          sortPomService.saveViolationFile(xmlOrderedResult);
          log.info(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
          log.info("Sorting file " + pomFile.getAbsolutePath());
          sortPomService.generateSortedXml();
          sortPomService.createBackupFile();
          sortPomService.saveGeneratedXml();
          log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
          break;
        case STOP:
          log.error(xmlOrderedResult.getErrorMessage());
          sortPomService.saveViolationFile(xmlOrderedResult);
          log.error(String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
          throw new FailureException(
              String.format(TEXT_FILE_NOT_SORTED, pomFile.getAbsolutePath()));
      }
    }
  }
}
