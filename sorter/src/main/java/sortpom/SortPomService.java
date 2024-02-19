package sortpom;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.dom4j.DocumentException;
import org.xml.sax.SAXException;
import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;
import sortpom.output.XmlOutputGenerator;
import sortpom.parameter.PluginParameters;
import sortpom.processinstruction.XmlProcessingInstructionParser;
import sortpom.util.FileUtil;
import sortpom.util.XmlOrderedResult;
import sortpom.wrapper.WrapperFactoryImpl;

/**
 * The implementation of the Mojo (Maven plugin) that sorts the pom file for a maven project.
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
  private boolean createBackupFile;

  private String originalXml;
  private String sortedXml;

  /** Instantiates a new sort pom mojo and initiates dependencies to other classes. */
  public SortPomService() {
    this.fileUtil = new FileUtil();
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
    this.createBackupFile = pluginParameters.createBackupFile;
  }

  /** Fetches and sorts the original xml. */
  void sortOriginalXml() {
    originalXml = fileUtil.getPomFileContent();
    xmlProcessingInstructionParser.scanForIgnoredSections(originalXml);
    var xml = xmlProcessingInstructionParser.replaceIgnoredSections();

    try (var originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(encoding))) {
      xmlProcessor.setOriginalXml(originalXmlInputStream);
    } catch (DocumentException | IOException | SAXException e) {
      throw new FailureException(
          "Could not sort " + pomFile.getAbsolutePath() + " content: " + xml, e);
    }
    xmlProcessor.sortXml();
  }

  /** Generates the sorted XML */
  void generateSortedXml() {
    if (sortedXml != null) {
      return;
    }
    sortedXml = xmlOutputGenerator.getSortedXml(xmlProcessor.getNewDocument());
    if (xmlProcessingInstructionParser.existsIgnoredSections()) {
      sortedXml = xmlProcessingInstructionParser.revertIgnoredSections(sortedXml);
    }
  }

  /** Creates the backup file for pom. */
  void createBackupFile() {
    if (!createBackupFile) {
      return;
    }
    if (backupFileExtension.trim().isEmpty()) {
      throw new FailureException("Could not create backup file, extension name was empty");
    }
    fileUtil.backupFile();
    log.info(
        String.format(
            "Saved backup of %s to %s%s",
            pomFile.getAbsolutePath(), pomFile.getAbsolutePath(), backupFileExtension));
  }

  void saveGeneratedXml() {
    fileUtil.savePomFile(sortedXml);
  }

  XmlOrderedResult isOriginalXmlStringSorted() {
    try (var originalXmlReader = new BufferedReader(new StringReader(originalXml));
        var sortedXmlReader = new BufferedReader(new StringReader(sortedXml))) {
      var originalXmlLine = originalXmlReader.readLine();
      var sortedXmlLine = sortedXmlReader.readLine();
      var line = 1;

      while (originalXmlLine != null && sortedXmlLine != null) {
        if (!originalXmlLine.equals(sortedXmlLine)) {
          return XmlOrderedResult.lineDiffers(line, "'" + sortedXmlLine + "'");
        }
        line++;
        originalXmlLine = originalXmlReader.readLine();
        sortedXmlLine = sortedXmlReader.readLine();
      }
      if (originalXmlLine != null || sortedXmlLine != null) {
        return XmlOrderedResult.lineDiffers(
            line, sortedXmlLine == null ? "empty" : "'" + sortedXmlLine + "'");
      }
    } catch (IOException ioex) {
      throw new FailureException(ioex.getMessage(), ioex);
    }
    if (ignoreLineSeparators || originalXml.equals(sortedXml)) {
      return XmlOrderedResult.ordered();
    }
    return XmlOrderedResult.lineSeparatorCharactersDiffer();
  }

  XmlOrderedResult isOriginalXmlElementsSorted() {
    return xmlProcessor.isXmlOrdered();
  }

  void saveViolationFile(XmlOrderedResult xmlOrderedResult) {
    if (violationFilename != null) {
      log.info("Saving violation report to " + new File(violationFilename).getAbsolutePath());
      var violationXmlProcessor = new ViolationXmlProcessor();
      var document =
          violationXmlProcessor.createViolationXmlContent(
              pomFile, xmlOrderedResult.getErrorMessage());
      var violationXmlString = xmlOutputGenerator.getSortedXml(document);
      fileUtil.saveViolationFile(violationXmlString);
    }
  }
}
