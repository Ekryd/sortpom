package sortpom;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.JDOMException;
import sortpom.util.FileUtil;
import sortpom.util.LineSeparator;
import sortpom.wrapper.WrapperFactoryImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Mojo (Maven plugin) that sorts the pomfile for a maven project.
 *
 * @author Bjorn Ekryd
 * @goal sort
 */
public class SortPomMojo extends AbstractMojo {
    /**
     * This is the File instance that refers to the location of the POM that should be sorted.
     *
     * @parameter expression="${sort.pomFile}" default-value="${project.file}"
     */
    private File pomFile;

    /**
     * Should a backup copy be created for the sorted pom.
     *
     * @parameter expression="${sort.createBackupFile}" default-value="true"
     */
    private boolean createBackupFile;

    /**
     * Name of the file extension for the backup file.
     *
     * @parameter expression="${sort.backupFileExtension}" default-value=".bak"
     */
    private String backupFileExtension;

    /**
     * Encoding for the files.
     *
     * @parameter expression="${sort.encoding}" default-value="UTF-8"
     */
    private String encoding;

    /**
     * Line separator for sorted pom. Can be either \n, \r or \r\n
     *
     * @parameter expression="${sort.lineSeparator}" default-value="${line.separator}"
     */
    private String lineSeparator;

    /**
     * Number of space characters to use as indentation. A value of -1 indicates that tab character should be used instead.
     *
     * @parameter expression="${sort.nrOfIndentSpace}" default-value="2"
     */
    private int nrOfIndentSpace;

    /**
     * Custom sort order file.
     *
     * @parameter expression="${sort.sortOrderFile}"
     */
    private String sortOrderFile;

    /** The fileutility. */
    private final FileUtil fileUtil;

    /** The xml processor. */
    private final XmlProcessor xmlProcessor;

    private static final int MAX_INDENT_SPACES = 255;

    /** Indicates that a tab character should be used instead of spaces. */
    private static final int INDENT_TAB = -1;


    /**
     * Instantiates a new sort pom mojo and initiates dependencies to other classes.
     */
    public SortPomMojo() {
        fileUtil = new FileUtil();
        final WrapperFactoryImpl wrapperFactory = new WrapperFactoryImpl(fileUtil);
        xmlProcessor = new XmlProcessor(wrapperFactory, fileUtil);
    }

    /**
     * Execute plugin.
     *
     * @throws MojoFailureException the mojo failure exception
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoFailureException {
        sortPom();
    }

    /**
     * Sorts the pomfile.
     *
     * @throws MojoFailureException the mojo failure exception
     */
    private void sortPom() throws MojoFailureException {
        final LineSeparator lineSeparatorInstance = new LineSeparator(lineSeparator);
        fileUtil.setup(pomFile, backupFileExtension, encoding, sortOrderFile);
        getLog().info("Sorting file " + pomFile.getAbsolutePath());

        String xml = fileUtil.getPomFileContent();
        String sortedXml = getSortedXml(lineSeparatorInstance, xml);
        if (xml.replaceAll("\\n|\\r", "").equals(sortedXml.replaceAll("\\n|\\r", ""))) {
            getLog().info("Pomfile is already sorted, exiting");
            return;
        }
        createBackupFile();
        saveSortedPomFile(sortedXml);
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
            getLog().info(
                    "Saved backup of " + pomFile.getAbsolutePath() + " to " + pomFile.getAbsolutePath()
                            + backupFileExtension);
        }
    }

    /**
     * Gets the indent characters from parameter.
     *
     * @return the indent characters
     * @throws MojoFailureException the mojo failure exception
     */
    private String getIndentCharacters() throws MojoFailureException {
        if (nrOfIndentSpace == 0) {
            return "";
        }
        if (nrOfIndentSpace == INDENT_TAB) {
            return "\t";
        }
        if (nrOfIndentSpace < INDENT_TAB || nrOfIndentSpace > MAX_INDENT_SPACES) {
            throw new MojoFailureException("nrOfIndentSpace cannot be below -1 or above 255: " + nrOfIndentSpace);
        }
        char[] chars = new char[nrOfIndentSpace];
        Arrays.fill(chars, ' ');
        return new String(chars);
    }


    /**
     * Sorts the incoming xml.
     *
     * @param lineSeparator the line separator
     * @param xml the xml that should be sorted.
     * @return the sorted xml
     * @throws MojoFailureException the mojo failure exception
     */
    private String getSortedXml(final LineSeparator lineSeparator, final String xml) throws MojoFailureException {
        ByteArrayInputStream originalXmlInputStream = null;
        ByteArrayOutputStream sortedXmlOutputStream = null;
        try {
            originalXmlInputStream = new ByteArrayInputStream(xml.getBytes(fileUtil.getEncoding()));
            xmlProcessor.setOriginalXml(originalXmlInputStream);
            xmlProcessor.sortXml();
            sortedXmlOutputStream = new ByteArrayOutputStream();
            xmlProcessor.getSortedXml(lineSeparator, getIndentCharacters(), sortedXmlOutputStream);
            return sortedXmlOutputStream.toString(fileUtil.getEncoding());
        } catch (JDOMException e) {
            throw new MojoFailureException("Could not sort pomfiles content: " + xml, e);
        } catch (IOException e) {
            throw new MojoFailureException("Could not sort pomfiles content: " + xml, e);
        } finally {
            IOUtils.closeQuietly(originalXmlInputStream);
            IOUtils.closeQuietly(sortedXmlOutputStream);
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
        getLog().info("Saved sorted pomfile to " + pomFile.getAbsolutePath());
    }
}
