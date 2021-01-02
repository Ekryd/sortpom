package sortpom;

import org.jdom.Document;
import org.jdom.JDOMException;
import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;
import sortpom.processinstruction.XmlProcessingInstructionParser;
import sortpom.util.FileUtil;
import sortpom.util.XmlOrderedResult;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.function.Supplier;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pom file for a
 * maven project.
 *
 * @author Bjorn Ekryd
 */
public class SortPomService {

    private final FileUtil fileUtil;
    private final XmlProcessor xmlProcessor;
    private final WrapperFactoryImpl wrapperFactory;
    private final XmlProcessingInstructionParser xmlProcessingInstructionParser;
    private final XmlOutputGenerator xmlOutputGenerator;

    private SortPomLogger log;
    private File pomFile;
    private String encoding;
    private String backupFileExtension;
    private boolean ignoreLineSeparators;
    private String violationFilename;

    /**
     * Instantiates a new sort pom mojo and initiates dependencies to other
     * classes.
     */
    public SortPomService(FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        this.wrapperFactory = new WrapperFactoryImpl(fileUtil);
        this.xmlProcessor = new XmlProcessor(wrapperFactory);
        this.xmlProcessingInstructionParser = new XmlProcessingInstructionParser();
        this.xmlOutputGenerator = new XmlOutputGenerator();
    }

    public void setup(SortPomLogger log, PluginParameters pluginParameters) {
        this.log = log;
        fileUtil.setup(pluginParameters);
        wrapperFactory.setup(pluginParameters);
        xmlProcessingInstructionParser.setup(log);
        xmlOutputGenerator.setup(pluginParameters);

        this.pomFile = pluginParameters.pomFile;
        this.encoding = pluginParameters.encoding;
        this.backupFileExtension = pluginParameters.backupFileExtension;
        this.ignoreLineSeparators = pluginParameters.ignoreLineSeparators;
        this.violationFilename = pluginParameters.violationFilename;
    }

    /**
     * Sorts the incoming xml.
     *
     * @param originalXml the xml that should be sorted.
     * @return the sorted xml
     */
    String sortXml(final String originalXml) {
        xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
        String xml = xmlProcessingInstructionParser.replaceIgnoredSections();

        insertXmlInXmlProcessor(xml, () -> "Could not sort " + pomFile.getAbsolutePath() + " content: ");
        xmlProcessor.sortXml();
        Document newDocument = xmlProcessor.getNewDocument();

        String sortedXml = xmlOutputGenerator.getSortedXml(newDocument);
        if (xmlProcessingInstructionParser.existsIgnoredSections()) {
            sortedXml = xmlProcessingInstructionParser.revertIgnoredSections(sortedXml);
        }
        return sortedXml;
    }

    boolean pomFileIsSorted(String xml, String sortedXml) {
        if (ignoreLineSeparators) {
            return xml.replaceAll("[\\n\\r]", "").equals(sortedXml.replaceAll("[\\n\\r]", ""));
        } else {
            return xml.equals(sortedXml);
        }
    }

    /**
     * Creates the backup file for pom.
     */
    void createBackupFile() {
        if (backupFileExtension.trim().length() == 0) {
            throw new FailureException("Could not create backup file, extension name was empty");
        }
        fileUtil.backupFile();
        log.info(String.format("Saved backup of %s to %s%s", pomFile.getAbsolutePath(),
                pomFile.getAbsolutePath(), backupFileExtension));
    }

    void saveViolationFile(XmlOrderedResult xmlOrderedResult) {
        if (violationFilename != null) {
            log.info("Saving violation report to " + new File(violationFilename).getAbsolutePath());
            ViolationXmlProcessor violationXmlProcessor = new ViolationXmlProcessor();
            Document document = violationXmlProcessor.createViolationXmlContent(pomFile, xmlOrderedResult.getErrorMessage());
            String violationXmlString = xmlOutputGenerator.getSortedXml(document);
            fileUtil.saveViolationFile(violationXmlString);
        }
    }

    XmlOrderedResult isPomElementsSorted() {
        String originalXml = fileUtil.getPomFileContent();
        xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
        String xml = xmlProcessingInstructionParser.replaceIgnoredSections();

        insertXmlInXmlProcessor(xml, () -> "Could not verify " + pomFile.getAbsolutePath() + " content: ");
        xmlProcessor.sortXml();

        return xmlProcessor.isXmlOrdered();
    }

    private void insertXmlInXmlProcessor(String xml, Supplier<String> errorMsg) {
        try (ByteArrayInputStream originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding))) {
            xmlProcessor.setOriginalXml(originalXmlInputStream);
        } catch (JDOMException | IOException e) {
            throw new FailureException(errorMsg.get() + xml, e);
        }
    }

}
