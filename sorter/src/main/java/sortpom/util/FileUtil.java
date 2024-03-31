package sortpom.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import sortpom.exception.FailureException;
import sortpom.parameter.PluginParameters;

/**
 * Used to interface with file system
 *
 * @author Bjorn
 */
public class FileUtil {
  private File pomFile;
  private String backupFileExtension;
  private String encoding;
  private String newName;
  private File backupFile;
  private String violationFilename;
  private long timestamp;
  private boolean keepTimestamp;

  private final FileAttributeUtil fileAttrUtils = new FileAttributeUtil();

  /** Initializes the class with sortpom parameters. */
  public void setup(PluginParameters parameters) {
    this.pomFile = parameters.pomFile;
    this.backupFileExtension = parameters.backupFileExtension;
    this.encoding = parameters.encoding;
    this.violationFilename = parameters.violationFilename;
    this.keepTimestamp = parameters.keepTimestamp;
  }

  /** Saves a backup of the pom file before sorting. */
  public void backupFile() {
    createFileHandle();
    checkBackupFileAccess();
    createBackupFile();
  }

  void createFileHandle() {
    newName = pomFile.getAbsolutePath() + backupFileExtension;
    backupFile = new File(newName);
  }

  private void checkBackupFileAccess() {
    try {
      Files.deleteIfExists(backupFile.toPath());
    } catch (IOException e) {
      throw new FailureException("Could not remove old backup file, filename: " + newName, e);
    }
  }

  private void createBackupFile() {
    try {
      Files.copy(pomFile.toPath(), backupFile.toPath());
    } catch (IOException e) {
      throw new FailureException("Could not create backup file to filename: " + newName, e);
    }
  }

  /**
   * Loads the pom file that will be sorted.
   *
   * @return Content of the file
   */
  public String getPomFileContent() {
    String content;
    try (InputStream inputStream = new FileInputStream(pomFile)) {
      var charset = Charset.forName(encoding);
      content = new String(inputStream.readAllBytes(), charset);
    } catch (UnsupportedCharsetException ex) {
      throw new FailureException("Could not handle encoding: " + encoding, ex);
    } catch (IOException ex) {
      throw new FailureException("Could not read pom file: " + pomFile.getAbsolutePath(), ex);
    }
    savePomfileTimestamp();
    return content;
  }

  private void savePomfileTimestamp() {
    if (keepTimestamp) {
      timestamp = fileAttrUtils.getLastModifiedTimestamp(pomFile);
      if (timestamp == 0) {
        throw new FailureException(
            "Could not retrieve the timestamp of the pom file: " + pomFile.getAbsolutePath());
      }
    }
  }

  public void saveViolationFile(String violationXml) {
    var violationFile = new File(violationFilename);
    saveFile(
        violationFile,
        violationXml,
        "Could not save violation file: " + violationFile.getAbsolutePath());
  }

  /**
   * Saves sorted pom file.
   *
   * @param sortedXml The content to save
   */
  public void savePomFile(String sortedXml) {
    saveFile(pomFile, sortedXml, "Could not save sorted pom file: " + pomFile.getAbsolutePath());
    setPomfileTimestamp();
  }

  private void saveFile(File fileToSave, String content, String errorMessage) {
    try {
      Files.createDirectories(fileToSave.getParentFile().toPath());
      Files.write(fileToSave.toPath(), content.getBytes(encoding));
    } catch (IOException e) {
      throw new FailureException(errorMessage, e);
    }
  }

  private void setPomfileTimestamp() {
    // when requested, keep the original's file timestamps for the created files
    if (keepTimestamp) {
      try {
        fileAttrUtils.setTimestamps(pomFile, timestamp);
      } catch (IOException e) {
        throw new FailureException(
            "Could not change timestamp of new pom file: " + pomFile.getAbsolutePath(), e);
      }
    }
  }
}
