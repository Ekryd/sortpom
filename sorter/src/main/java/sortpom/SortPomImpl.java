package sortpom;

import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.VerifyFailType;
import sortpom.util.FileUtil;
import sortpom.util.XmlOrderedResult;

import java.io.File;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pom file for a Maven project.
 */
public class SortPomImpl {
    private static final String TEXT_FILE_NOT_SORTED = "The file %s is not sorted";

    private final SortPomService sortPomService;
    private final FileUtil fileUtil;

    private SortPomLogger log;
    private File pomFile;
    private VerifyFailType verifyFailType;
    private boolean createBackupFile;

    public SortPomImpl() {
        this.fileUtil = new FileUtil();
        this.sortPomService = new SortPomService(fileUtil);
    }

    public void setup(SortPomLogger log, PluginParameters pluginParameters) {
        this.log = log;
        this.pomFile = pluginParameters.pomFile;
        this.verifyFailType = pluginParameters.verifyFailType;
        this.createBackupFile = pluginParameters.createBackupFile;

        fileUtil.setup(pluginParameters);
        sortPomService.setup(log, pluginParameters);

        warnAboutDeprecatedArguments(log, pluginParameters);
    }

    private void warnAboutDeprecatedArguments(SortPomLogger log, PluginParameters pluginParameters) {
        if (pluginParameters.sortDependencies.isDeprecatedValueTrue()) {
            log.warn("[DEPRECATED] The 'true' value in sortDependencies is not used anymore, please use value 'groupId,artifactId' instead. In the next major version 'true' or 'false' will cause an error!");
        }
        if (pluginParameters.sortDependencies.isDeprecatedValueFalse()) {
            log.warn("[DEPRECATED] The 'false' value in sortDependencies is not used anymore, please use empty value '' or omit sortDependencies instead. In the next major version 'true' or 'false' will cause an error!");
        }
        if (pluginParameters.sortPlugins.isDeprecatedValueTrue()) {
            log.warn("[DEPRECATED] The 'true' value in sortPlugins is not used anymore, please use value 'groupId,artifactId' instead. In the next major version 'true' or 'false' will cause an error!");
        }
        if (pluginParameters.sortPlugins.isDeprecatedValueFalse()) {
            log.warn("[DEPRECATED] The 'false' value in sortPlugins is not used anymore, please use empty value '' or omit sortPlugins instead. In the next major version 'true' or 'false' will cause an error!");
        }
    }

    /**
     * Sorts the pom file.
     */
    public void sortPom() {
        log.info("Sorting file " + pomFile.getAbsolutePath());

        String originalXml = fileUtil.getPomFileContent();
        String sortedXml = sortPomService.sortXml(originalXml);
        if (sortPomService.pomFileIsSorted(originalXml, sortedXml)) {
            log.info("Pom file is already sorted, exiting");
            return;
        }
        if (createBackupFile) {
            sortPomService.createBackupFile();
        }
        fileUtil.savePomFile(sortedXml);
        log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
    }

    /**
     * Verify that the pom-file is sorted regardless of formatting
     */
    public void verifyPom() {
        String pomFileName = pomFile.getAbsolutePath();
        log.info("Verifying file " + pomFileName);

        XmlOrderedResult xmlOrderedResult = sortPomService.isPomElementsSorted();
        if (!xmlOrderedResult.isOrdered()) {
            switch (verifyFailType) {
                case WARN:
                    log.warn(xmlOrderedResult.getErrorMessage());
                    sortPomService.saveViolationFile(xmlOrderedResult);
                    log.warn(String.format(TEXT_FILE_NOT_SORTED, pomFileName));
                    break;
                case SORT:
                    log.info(xmlOrderedResult.getErrorMessage());
                    sortPomService.saveViolationFile(xmlOrderedResult);
                    log.info(String.format(TEXT_FILE_NOT_SORTED, pomFileName));
                    sortPom();
                    break;
                case STOP:
                    log.error(xmlOrderedResult.getErrorMessage());
                    sortPomService.saveViolationFile(xmlOrderedResult);
                    log.error(String.format(TEXT_FILE_NOT_SORTED, pomFileName));
                    throw new FailureException(String.format(TEXT_FILE_NOT_SORTED, pomFileName));
            }
        }
    }
}
