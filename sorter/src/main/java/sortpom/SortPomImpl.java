package sortpom;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.VerifyFailType;
import sortpom.processinstruction.XmlProcessingInstructionParser;
import sortpom.util.FileUtil;
import sortpom.util.XmlOrderedResult;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pom file for a
 * maven project.
 *
 * @author Bjorn Ekryd
 */
public class SortPomImpl {

    private final FileUtil fileUtil;
    private final XmlProcessor xmlProcessor;
    private final WrapperFactoryImpl wrapperFactory;
    private final XmlProcessingInstructionParser xmlProcessingInstructionParser;
    private SortPomLogger log;
    private File pomFile;
    private String encoding;
    private boolean createBackupFile;
    private String backupFileExtension;
    private VerifyFailType verifyFailType;

    /**
     * Instantiates a new sort pom mojo and initiates dependencies to other
     * classes.
     */
    public SortPomImpl() {
        fileUtil = new FileUtil();
        wrapperFactory = new WrapperFactoryImpl(fileUtil);
        xmlProcessor = new XmlProcessor(wrapperFactory);
        xmlProcessingInstructionParser = new XmlProcessingInstructionParser();
    }

    public void setup(SortPomLogger log, PluginParameters pluginParameters) {
        this.log = log;
        fileUtil.setup(pluginParameters);
        wrapperFactory.setup(pluginParameters);
        xmlProcessor.setup(pluginParameters);
        xmlProcessingInstructionParser.setup(log);
        pomFile = pluginParameters.pomFile;
        encoding = pluginParameters.encoding;
        createBackupFile = pluginParameters.createBackupFile;
        backupFileExtension = pluginParameters.backupFileExtension;
        verifyFailType = pluginParameters.verifyFailType;
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
        String sortedXml = sortXml(originalXml);
        if (pomFileIsSorted(originalXml, sortedXml)) {
            log.info("Pom file is already sorted, exiting");
            return;
        }
        createBackupFile();
        saveSortedPomFile(sortedXml);
    }

    /**
     * Sorts the incoming xml.
     *
     * @param originalXml the xml that should be sorted.
     * @return the sorted xml
     */
    String sortXml(final String originalXml) {
        String errorMsg = "Could not sort pom files content: ";

        xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
        String xml = xmlProcessingInstructionParser.replaceIgnoredSections();
        
        insertXmlInXmlProcessor(xml, errorMsg);
        xmlProcessor.sortXml();
        ByteArrayOutputStream sortedXmlOutputStream = null;
        try {
            sortedXmlOutputStream = xmlProcessor.getSortedXml();
            String sortedXml = sortedXmlOutputStream.toString(encoding);
            if (xmlProcessingInstructionParser.existsIgnoredSections()) {
                sortedXml = xmlProcessingInstructionParser.revertIgnoredSections(sortedXml);
            }
            return sortedXml;
        } catch (IOException e) {
            throw new FailureException(errorMsg + xml, e);
        } finally {
            IOUtils.closeQuietly(sortedXmlOutputStream);
        }

    }

    private boolean pomFileIsSorted(String xml, String sortedXml) {
        return xml.replaceAll("\\n|\\r", "").equals(sortedXml.replaceAll("\\n|\\r", ""));
    }

    /**
     * Creates the backup file for pom.
     */
    private void createBackupFile() {
        if (createBackupFile) {
            if (backupFileExtension.trim().length() == 0) {
                throw new FailureException("Could not create backup file, extension name was empty");
            }
            fileUtil.backupFile();
            log.info(String.format("Saved backup of %s to %s%s", pomFile.getAbsolutePath(), 
                    pomFile.getAbsolutePath(), backupFileExtension));
        }
    }

    /**
     * Saves the sorted pom file.
     *
     * @param sortedXml the sorted xml
     */
    private void saveSortedPomFile(final String sortedXml) {
        fileUtil.savePomFile(sortedXml);
        log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
    }

    /**
     * Verify that the pom-file is sorted regardless of formatting
     */
    public void verifyPom() {
        String pomFileName = pomFile.getAbsolutePath();
        log.info("Verifying file " + pomFileName);

        XmlOrderedResult xmlOrderedResult = isPomElementsSorted();
        if (!xmlOrderedResult.isOrdered()) {
            switch (verifyFailType) {
                case WARN:
                    log.warn(xmlOrderedResult.getErrorMessage());
                    log.warn(String.format("The file %s is not sorted", pomFileName));
                    break;
                case SORT:
                    log.info(xmlOrderedResult.getErrorMessage());
                    log.info(String.format("The file %s is not sorted", pomFileName));
                    sortPom();
                    break;
                case STOP:
                    log.error(xmlOrderedResult.getErrorMessage());
                    log.error(String.format("The file %s is not sorted", pomFileName));
                    throw new FailureException(String.format("The file %s is not sorted", pomFileName));
                default:
                    log.error(xmlOrderedResult.getErrorMessage());
                    throw new IllegalStateException(verifyFailType.toString());
            }
        }
    }

    public XmlOrderedResult isPomElementsSorted() {
        String originalXml = fileUtil.getPomFileContent();
        xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
        String xml = xmlProcessingInstructionParser.replaceIgnoredSections();

        insertXmlInXmlProcessor(xml, "Could not verify pom files content: ");
        xmlProcessor.sortXml();

        return xmlProcessor.isXmlOrdered();
    }

    private void insertXmlInXmlProcessor(String xml, String errorMsg) {
        ByteArrayInputStream originalXmlInputStream = null;
        try {
            originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding));
            xmlProcessor.setOriginalXml(originalXmlInputStream);
        } catch (JDOMException e) {
            throw new FailureException(errorMsg + xml, e);
        } catch (IOException e) {
            throw new FailureException(errorMsg + xml, e);
        } finally {
            IOUtils.closeQuietly(originalXmlInputStream);
        }
    }

}
