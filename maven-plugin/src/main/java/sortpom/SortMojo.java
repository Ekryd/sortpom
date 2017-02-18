package sortpom;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import sortpom.exception.ExceptionConverter;
import sortpom.logger.MavenLogger;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

import java.io.File;

/**
 * Sorts the pom.xml for a Maven project.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "sort", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class SortMojo extends AbstractMojo {
    /**
     * This is the File instance that refers to the location of the pom that
     * should be sorted.
     */
    @Parameter(property = "sort.pomFile", defaultValue = "${project.file}")
    private File pomFile;

    /**
     * Should a backup copy be created for the sorted pom.
     */
    @Parameter(property = "sort.createBackupFile", defaultValue = "true")
    private boolean createBackupFile;

    /**
     * Name of the file extension for the backup file.
     */
    @Parameter(property = "sort.backupFileExtension", defaultValue = ".bak")
    private String backupFileExtension;

    /**
     * Encoding for the files.
     */
    @Parameter(property = "sort.encoding", defaultValue = "UTF-8")
    private String encoding;

    /**
     * Line separator for sorted pom. Can be either \n, \r or \r\n
     */
    @Parameter(property = "sort.lineSeparator", defaultValue = "${line.separator}")
    private String lineSeparator;

    /**
     * Ignore line separators when comparing current POM with sorted one
     */
    @Parameter(property = "sort.ignoreLineSeparators", defaultValue = "true")
    private boolean ignoreLineSeparators;

    /**
     * Should empty xml elements be expanded or not. Example:
     * &lt;configuration&gt;&lt;/configuration&gt; or &lt;configuration/&gt;
     */
    @Parameter(property = "sort.expandEmptyElements", defaultValue = "true")
    private boolean expandEmptyElements;

    /**
     * Should blank lines in the pom-file be preserved. A maximum of one line is preserved between each tag.
     */
    @Parameter(property = "sort.keepBlankLines", defaultValue = "false")
    private boolean keepBlankLines;

    /**
     * Number of space characters to use as indentation. A value of -1 indicates
     * that tab character should be used instead.
     */
    @Parameter(property = "sort.nrOfIndentSpace", defaultValue = "2")
    private int nrOfIndentSpace;

    /**
     * Should blank lines (if preserved) have indentation.
     */
    @Parameter(property = "sort.indentBlankLines", defaultValue = "false")
    private boolean indentBlankLines;

    /**
     * Choose between a number of predefined sort order files.
     */
    @Parameter(property = "sort.predefinedSortOrder")
    private String predefinedSortOrder;

    /**
     * Custom sort order file.
     */
    @Parameter(property = "sort.sortOrderFile")
    private String sortOrderFile;

    /**
     * Comma-separated ordered list how dependencies should be sorted. Example: scope,groupId,artifactId.
     * If scope is specified in the list then the scope ranking is COMPILE, PROVIDED, SYSTEM, RUNTIME, IMPORT and TEST.
     * The list can be separated by ",;:"
     */
    @Parameter(property = "sort.sortDependencies")
    private String sortDependencies;

    /**
     * Comma-separated ordered list how plugins should be sorted. Example: groupId,artifactId
     * The list can be separated by ",;:"
     */
    @Parameter(property = "sort.sortPlugins")
    private String sortPlugins;

    /**
     * Should the Maven pom properties be sorted alphabetically. Affects both
     * project/properties and project/profiles/profile/properties
     */
    @Parameter(property = "sort.sortProperties", defaultValue = "false")
    private boolean sortProperties;

    /**
     * Set this to 'true' to bypass sortpom plugin
     */
    @Parameter(property = "sort.skip", defaultValue = "false")
    private boolean skip;

    private final SortPomImpl sortPomImpl = new SortPomImpl();

    public SortMojo() {
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
            getLog().info("Skipping sortpom");
        } else {
            setup();
            sortPom();
        }

    }

    public void setup() throws MojoFailureException {
        new ExceptionConverter(() -> {
            PluginParameters pluginParameters = new PluginParametersBuilder()
                    .setPomFile(pomFile)
                    .setBackupInfo(createBackupFile, backupFileExtension)
                    .setEncoding(encoding)
                    .setFormatting(lineSeparator, expandEmptyElements, keepBlankLines, ignoreLineSeparators)
                    .setIndent(nrOfIndentSpace, indentBlankLines)
                    .setSortOrder(sortOrderFile, predefinedSortOrder)
                    .setSortEntities(sortDependencies, sortPlugins, sortProperties).createPluginParameters();

            sortPomImpl.setup(new MavenLogger(getLog()), pluginParameters);
        }).executeAndConvertException();
    }

    private void sortPom() throws MojoFailureException {
        new ExceptionConverter(sortPomImpl::sortPom).executeAndConvertException();
    }
    
}
