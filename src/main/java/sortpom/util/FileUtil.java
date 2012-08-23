package sortpom.util;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoFailureException;
import sortpom.parameter.PluginParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

/**
 * Used to interface with file system
 *
 * @author Bjorn
 */
public class FileUtil {
    private static final String DEFAULT_SORT_ORDER_FILENAME = "default_1_0_0";
    private static final String XML_FILE_EXTENSION = ".xml";
    private File pomFile;
    private String backupFileExtension;
    private String encoding;
    private String customSortOrderFile;
    private String predefinedSortOrder;
    private String newName;
    private File backupFile;

    /** Initializes the class with sortpom parameters. */
    public void setup(PluginParameters parameters) {
        this.pomFile = parameters.pomFile;
        this.backupFileExtension = parameters.backupFileExtension;
        this.encoding = parameters.encoding;
        this.customSortOrderFile = parameters.customSortOrderFile;
        this.predefinedSortOrder = parameters.predefinedSortOrder;
    }

    /**
     * Saves a backup of the pom file before sorting.
     *
     * @throws MojoFailureException
     */
    public void backupFile() throws MojoFailureException {
        createFileHandle();
        checkBackupFileAccess();
        createBackupFile();
    }

    void createFileHandle() {
        newName = pomFile.getAbsolutePath() + backupFileExtension;
        backupFile = new File(newName);
    }

    private void checkBackupFileAccess() throws MojoFailureException {
        if (backupFile.exists() && !backupFile.delete()) {
            throw new MojoFailureException("Could not remove old backup file, filename: " + newName);
        }
    }

    private void createBackupFile() throws MojoFailureException {
        FileInputStream source = null;
        FileOutputStream newFile = null;
        try {
            source = new FileInputStream(pomFile);
            newFile = new FileOutputStream(backupFile);
            IOUtils.copy(source, newFile);
        } catch (IOException e) {
            throw new MojoFailureException("Could not create backup file to filename: " + newName, e);
        } finally {
            IOUtils.closeQuietly(newFile);
            IOUtils.closeQuietly(source);
        }
    }

    /**
     * Loads the pom file that will be sorted.
     *
     * @return Content of the file
     * @throws MojoFailureException
     */
    public String getPomFileContent() throws MojoFailureException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(pomFile);
            return IOUtils.toString(inputStream, encoding);
        } catch (UnsupportedEncodingException ueex) {
            throw new MojoFailureException("Could not handle encoding: " + encoding, ueex);
        } catch (IOException ioex) {
            throw new MojoFailureException("Could not read pom file: " + pomFile.getAbsolutePath(), ioex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * Saves sorted pom file.
     *
     * @param sortedXml The content to save
     * @throws MojoFailureException
     */
    public void savePomFile(final String sortedXml) throws MojoFailureException {
        FileOutputStream saveFile = null;
        try {
            saveFile = new FileOutputStream(pomFile);
            IOUtils.write(sortedXml, saveFile, encoding);
        } catch (IOException e) {
            throw new MojoFailureException("Could not save sorted pom file: " + pomFile.getAbsolutePath(), e);
        } finally {
            IOUtils.closeQuietly(saveFile);
        }
    }

    public byte[] getDefaultSortOrderXmlBytes() throws IOException {
        return getDefaultSortOrderXml().getBytes(encoding);
    }

    /**
     * Retrieves the default sort order for sortpom
     *
     * @return Content of the default sort order file
     */
    private String getDefaultSortOrderXml() throws IOException {
        InputStream inputStream = null;
        try {
            if (customSortOrderFile != null) {
                inputStream = getFileFromRelativeOrClassPath();
            } else if (predefinedSortOrder != null) {
                inputStream = getPredefinedSortOrder(predefinedSortOrder);
            } else {
                inputStream = getPredefinedSortOrder(DEFAULT_SORT_ORDER_FILENAME);
            }
            return IOUtils.toString(inputStream, encoding);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private InputStream getFileFromRelativeOrClassPath() throws IOException {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(customSortOrderFile);
        } catch (FileNotFoundException fnfex) {
            // try classpath
            try {
                URL resource = this.getClass().getClassLoader().getResource(customSortOrderFile);
                if (resource == null) {
                    throw new IOException("Cannot find resource");
                }
                inputStream = resource.openStream();
            } catch (IOException e1) {
                throw new FileNotFoundException(String.format("Could not find %s or %s in classpath", new File(
                        customSortOrderFile).getAbsolutePath(), customSortOrderFile));
            }
        }
        return inputStream;
    }

    private InputStream getPredefinedSortOrder(String predefinedSortOrder) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(predefinedSortOrder + XML_FILE_EXTENSION);
        if (resource == null) {
            throw new IllegalArgumentException(String.format("Cannot find %s among the predefined plugin resources",
                    predefinedSortOrder + XML_FILE_EXTENSION));
        }
        return resource.openStream();
    }

}
