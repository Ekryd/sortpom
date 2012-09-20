package sortpom;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.jdom.JDOMException;
import sortpom.parameter.PluginParameters;
import sortpom.util.FileUtil;
import sortpom.parameter.VerifyFailType;
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
    private Log log;
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
    }

    public void setup(Log log, PluginParameters pluginParameters) {
        this.log = log;
        fileUtil.setup(pluginParameters);
        wrapperFactory.setup(pluginParameters);
        xmlProcessor.setup(pluginParameters);
        pomFile = pluginParameters.pomFile;
        encoding = pluginParameters.encoding;
        createBackupFile = pluginParameters.createBackupFile;
        backupFileExtension = pluginParameters.backupFileExtension;
        verifyFailType = pluginParameters.verifyFailType;
        warnAboutDeprecatedArguments(log, pluginParameters);
    }

    private void warnAboutDeprecatedArguments(Log log, PluginParameters pluginParameters) {
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
     *
     * @throws MojoFailureException the mojo failure exception
     */
    public void sortPom() throws MojoFailureException {
        log.info("Sorting file " + pomFile.getAbsolutePath());

        String originalXml = fileUtil.getPomFileContent();
        String sortedXml = getSortedXml(originalXml);
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
     * @param xml the xml that should be sorted.
     * @return the sorted xml
     * @throws MojoFailureException the mojo failure exception
     */
    private String getSortedXml(final String xml) throws MojoFailureException {
        String errorMsg = "Could not sort pom files content: ";
        insertXmlInXmlProcessor(xml, errorMsg);
        xmlProcessor.sortXml();
        ByteArrayOutputStream sortedXmlOutputStream = null;
        try {
            sortedXmlOutputStream = xmlProcessor.getSortedXml();
            return sortedXmlOutputStream.toString(encoding);
        } catch (IOException e) {
            throw new MojoFailureException(errorMsg + xml, e);
        } finally {
            IOUtils.closeQuietly(sortedXmlOutputStream);
        }

    }

    private boolean pomFileIsSorted(String xml, String sortedXml) {
        return xml.replaceAll("\\n|\\r", "").equals(sortedXml.replaceAll("\\n|\\r", ""));
    }

    /**
     * Creates the backup file for pom.
     *
     * @throws MojoFailureException the mojo failure exception
     */
    private void createBackupFile() throws MojoFailureException {
        if (createBackupFile) {
            if (backupFileExtension.trim().length() == 0) {
                throw new MojoFailureException("Could not create backup file, extension name was empty");
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
     * @throws MojoFailureException the mojo failure exception
     */
    private void saveSortedPomFile(final String sortedXml) throws MojoFailureException {
        fileUtil.savePomFile(sortedXml);
        log.info("Saved sorted pom file to " + pomFile.getAbsolutePath());
    }

    /**
     * Verify that the pom-file is sorted regardless of formatting
     *
     * @throws MojoFailureException thrown if pom-file is not sorted
     */
    public void verifyPom() throws MojoFailureException {
        String pomFileName = pomFile.getAbsolutePath();
        log.info("Verifying file " + pomFileName);

        XmlOrderedResult xmlOrderedResult = isPomElementsSorted();
        if (!xmlOrderedResult.isOrdered()) {
            switch (verifyFailType) {
                case WARN:
                    log.warn(xmlOrderedResult.getMessage());
                    log.warn(String.format("The file %s is not sorted", pomFileName));
                    break;
                case SORT:
                    log.info(xmlOrderedResult.getMessage());
                    log.info(String.format("The file %s is not sorted", pomFileName));
                    sortPom();
                    break;
                case STOP:
                    log.error(xmlOrderedResult.getMessage());
                    log.error(String.format("The file %s is not sorted", pomFileName));
                    throw new MojoFailureException(String.format("The file %s is not sorted", pomFileName));
                default:
                    log.error(xmlOrderedResult.getMessage());
                    throw new IllegalStateException(verifyFailType.toString());
            }
        }
    }

    public XmlOrderedResult isPomElementsSorted() throws MojoFailureException {
        String originalXml = fileUtil.getPomFileContent();
        insertXmlInXmlProcessor(originalXml, "Could not verify pom files content: ");
        xmlProcessor.sortXml();

        return xmlProcessor.isXmlOrdered();
    }

    private void insertXmlInXmlProcessor(final String xml, String errorMsg) throws MojoFailureException {
        ByteArrayInputStream originalXmlInputStream = null;
        try {
            originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding));
            xmlProcessor.setOriginalXml(originalXmlInputStream);
        } catch (JDOMException e) {
            throw new MojoFailureException(errorMsg + xml, e);
        } catch (IOException e) {
            throw new MojoFailureException(errorMsg + xml, e);
        } finally {
            IOUtils.closeQuietly(originalXmlInputStream);
        }
    }

}
