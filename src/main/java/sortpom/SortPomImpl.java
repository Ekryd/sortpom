package sortpom;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.jdom.JDOMException;
import sortpom.util.FileUtil;
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

    /**
     * Instantiates a new sort pom mojo and initiates dependencies to other
     * classes.
     */
    public SortPomImpl() {
        fileUtil = new FileUtil();
        wrapperFactory = new WrapperFactoryImpl(fileUtil);
        xmlProcessor = new XmlProcessor(wrapperFactory);
    }

    public void setup(Log log, PluginParameters pluginParameters) throws MojoFailureException {
        this.log = log;
        fileUtil.setup(pluginParameters);
        wrapperFactory.setup(pluginParameters);
        xmlProcessor.setup(pluginParameters);
        pomFile = pluginParameters.pomFile;
        encoding = pluginParameters.encoding;
        createBackupFile = pluginParameters.createBackupFile;
        backupFileExtension = pluginParameters.backupFileExtension;
    }

    /**
     * Sorts the pom file.
     *
     * @throws MojoFailureException the mojo failure exception
     */
    void sortPom() throws MojoFailureException {
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
        ByteArrayInputStream originalXmlInputStream = null;
        ByteArrayOutputStream sortedXmlOutputStream = null;
        try {
            originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding));
            xmlProcessor.setOriginalXml(originalXmlInputStream);
            xmlProcessor.sortXml();
            sortedXmlOutputStream = xmlProcessor.getSortedXml();
            return sortedXmlOutputStream.toString(encoding);
        } catch (JDOMException e) {
            throw new MojoFailureException("Could not sort pom files content: " + xml, e);
        } catch (IOException e) {
            throw new MojoFailureException("Could not sort pom files content: " + xml, e);
        } finally {
            IOUtils.closeQuietly(originalXmlInputStream);
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
            log.info("Saved backup of " + pomFile.getAbsolutePath() + " to " + pomFile.getAbsolutePath()
                    + backupFileExtension);
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

}
