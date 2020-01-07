package sortpom.util;

import org.apache.commons.io.IOUtils;
import sortpom.exception.FailureException;
import sortpom.parameter.PluginParameters;

import java.io.*;
import java.net.URL;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.util.Optional;

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
    private String violationFilename;
    private long timestamp;
    private boolean keepTimestamp;

    /** Initializes the class with sortpom parameters. */
    public void setup(PluginParameters parameters) {
        this.pomFile = parameters.pomFile;
        this.backupFileExtension = parameters.backupFileExtension;
        this.encoding = parameters.encoding;
        this.customSortOrderFile = parameters.customSortOrderFile;
        this.predefinedSortOrder = parameters.predefinedSortOrder;
        this.violationFilename = parameters.violationFilename;
        this.keepTimestamp = parameters.keepTimestamp;
    }

    /**
     * Saves a backup of the pom file before sorting.
     */
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
        if (backupFile.exists() && !backupFile.delete()) {
            throw new FailureException("Could not remove old backup file, filename: " + newName);
        }
    }

    private void createBackupFile() {
        try (FileInputStream source = new FileInputStream(pomFile); FileOutputStream newFile = new FileOutputStream(backupFile)) {
            IOUtils.copy(source, newFile);
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
            content = IOUtils.toString(inputStream, encoding);
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
            timestamp = pomFile.lastModified();
            if (timestamp == 0) {
                throw new FailureException("Cound not save the timestamp of the pom file: " + pomFile.getAbsolutePath());
            }
        }
    }

    public void saveViolationFile(String violationXml) {
        File violationFile = new File(violationFilename);
        saveFile(violationFile, violationXml, "Could not save violation file: " + violationFile.getAbsolutePath());
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
        fileToSave.getParentFile().mkdirs();
        try (FileOutputStream saveFile = new FileOutputStream(fileToSave)) {
            IOUtils.write(content, saveFile, encoding);
        } catch (IOException e) {
            throw new FailureException(errorMessage, e);
        }
    }

    private void setPomfileTimestamp() {
        // when requested, keep the original's file timestamps for the created files
        if (keepTimestamp) {
            try {
                BasicFileAttributeView attributes = Files.getFileAttributeView(pomFile.toPath(), BasicFileAttributeView.class);
                FileTime time = FileTime.fromMillis(timestamp);
                attributes.setTimes(time, time, time);
            } catch (IOException e) {
                throw new FailureException("Could not change timestamp of new pom file: " + pomFile.getAbsolutePath(), e);
            }
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
        CheckedSupplier<InputStream, IOException> createStreamFunc = () -> {
            if (customSortOrderFile != null) {
                UrlWrapper urlWrapper = new UrlWrapper(customSortOrderFile);
                if (urlWrapper.isUrl()) {
                    return urlWrapper.openStream();
                } else {
                    return openCustomSortOrderFile();
                }
            } else if (predefinedSortOrder != null) {
                return getPredefinedSortOrder(predefinedSortOrder);
            }
            return getPredefinedSortOrder(DEFAULT_SORT_ORDER_FILENAME);
        };

        try (InputStream inputStream = createStreamFunc.get()) {
            return IOUtils.toString(inputStream, encoding);
        }
    }

    /**
     * Load custom sort order file from absolute or class path.
     *
     * @return a stream to the opened resource
     */
    private InputStream openCustomSortOrderFile() throws FileNotFoundException {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(customSortOrderFile);
        } catch (FileNotFoundException ex) {
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
        Optional<URL> resourceOptional = Optional
                .of(getClass())
                .map(Class::getClassLoader)
                .map(classLoader -> classLoader.getResource(predefinedSortOrder + XML_FILE_EXTENSION));

        URL resource = resourceOptional.orElseThrow(() -> new IllegalArgumentException(
                String.format("Cannot find %s among the predefined plugin resources", predefinedSortOrder + XML_FILE_EXTENSION)));

        return resource.openStream();
    }

}
