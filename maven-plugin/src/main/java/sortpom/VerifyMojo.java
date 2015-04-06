package sortpom;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import sortpom.exception.ExceptionHandler;
import sortpom.exception.FailureException;
import sortpom.logger.MavenLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

import java.io.File;

/**
 * Verifies that the pom.xml is sorted. If the verification fails then the pom.xml is sorted.
 *
 * @author Bjorn Ekryd
 * @goal verify
 * @threadSafe true
 */
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class VerifyMojo extends AbstractMojo {
    /**
     * This is the File instance that refers to the location of the pom that
     * should be sorted.
     *
     * @parameter property="sort.pomFile" default-value="${project.file}"
     */
    private File pomFile;

    /**
     * Choose between a number of predefined sort order files.
     *
     * @parameter property="sort.predefinedSortOrder"
     */
    private String predefinedSortOrder;

    /**
     * Custom sort order file.
     *
     * @parameter property="sort.sortOrderFile"
     */
    private String sortOrderFile;

    /**
     * Comma-separated ordered list how dependencies should be sorted. Example: scope,groupId,artifactId
     * If scope is specified in the list then the scope ranking is COMPILE, PROVIDED, SYSTEM, RUNTIME, IMPORT and TEST.
     * The list can be separated by ",;:"
     *
     * @parameter property="sort.sortDependencies" default-value=""
     */
    private String sortDependencies;

    /**
     * Comma-separated ordered list how plugins should be sorted. Example: groupId,artifactId
     * The list can be separated by ",;:"
     *
     * @parameter property="sort.sortPlugins" default-value=""
     */
    private String sortPlugins;

    /**
     * Should the Maven pom properties be sorted alphabetically. Affects both
     * project/properties and project/profiles/profile/properties
     *
     * @parameter property="sort.sortProperties" default-value="false"
     */
    private boolean sortProperties;

    /**
     * Encoding for the files.
     *
     * @parameter property="sort.encoding" default-value="UTF-8"
     */
    private String encoding;

    /**
     * What should happen if verification fails. Can be either 'sort', 'warn' or 'stop'
     *
     * @parameter property="sort.verifyFail" default-value="sort"
     */
    private String verifyFail;


    /**
     * Should a backup copy be created for the sorted pom.
     *
     * @parameter property="sort.createBackupFile" default-value="true"
     */
    private boolean createBackupFile;

    /**
     * Name of the file extension for the backup file.
     *
     * @parameter property="sort.backupFileExtension" default-value=".bak"
     */
    private String backupFileExtension;

    /**
     * Line separator for sorted pom. Can be either \n, \r or \r\n
     *
     * @parameter property="sort.lineSeparator"
     * default-value="${line.separator}"
     */
    private String lineSeparator;

    /**
     * Should empty xml elements be expanded or not. Example:
     * &lt;configuration&gt;&lt;/configuration&gt; or &lt;configuration/&gt;
     *
     * @parameter property="sort.expandEmptyElements" default-value="true"
     */
    private boolean expandEmptyElements;

    /**
     * Should blank lines in the pom-file be preserved. A maximum of one line is preserved between each tag.
     *
     * @parameter property="sort.keepBlankLines" default-value="false"
     */
    private boolean keepBlankLines;

    /**
     * Number of space characters to use as indentation. A value of -1 indicates
     * that tab character should be used instead.
     *
     * @parameter property="sort.nrOfIndentSpace" default-value="2"
     */
    private int nrOfIndentSpace;

    /**
     * Should blank lines (if preserved) have indentation.
     *
     * @parameter property="sort.indentBlankLines" default-value="false"
     */
    private boolean indentBlankLines;

    /**
     * Set this to 'true' to bypass sortpom plugin
     *
     * @parameter property="sort.skip" default-value="false"
     */
    private boolean skip;

    private final SortPomImpl sortPomImpl = new SortPomImpl();

    public VerifyMojo() {
    }

    /**
     * Execute plugin.
     *
     * @throws org.apache.maven.plugin.MojoFailureException exception that will be handled by plugin framework
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoFailureException {
        if (skip) {
            getLog().info("Skipping Sortpom");
        } else {
            setup();
            sortPom();
        }
    }

    public void setup() throws MojoFailureException {
        try {
            PluginParameters pluginParameters = new PluginParametersBuilder()
                    .setPomFile(pomFile)
                    .setBackupInfo(createBackupFile, backupFileExtension)
                    .setEncoding(encoding)
                    .setFormatting(lineSeparator, expandEmptyElements, keepBlankLines)
                    .setIndent(nrOfIndentSpace, indentBlankLines)
                    .setSortOrder(sortOrderFile, predefinedSortOrder)
                    .setSortEntities(sortDependencies, sortPlugins, sortProperties)
                    .setVerifyFail(verifyFail)
                    .createPluginParameters();

            sortPomImpl.setup(new MavenLogger(getLog()), pluginParameters);
        } catch (FailureException fex) {
            new ExceptionHandler(fex).throwMojoFailureException();
        }
    }

    private void sortPom() throws MojoFailureException {
        try {
            sortPomImpl.verifyPom();
        } catch (FailureException fex) {
            new ExceptionHandler(fex).throwMojoFailureException();
        }
    }

}
