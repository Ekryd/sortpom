package sortpom;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import sortpom.parameter.PluginParameters;
import sortpom.parameter.PluginParametersBuilder;

import java.io.File;

/**
 * Mojo (Maven plugin) that sorts the pom file for a maven project.
 *
 * @author Bjorn Ekryd
 * @goal verify
 * @threadSafe true
 */
public class VerifyMojo extends AbstractMojo {
    /**
     * This is the File instance that refers to the location of the pom that
     * should be sorted.
     *
     * @parameter expression="${sort.pomFile}" default-value="${project.file}"
     */
    private File pomFile;

    /**
     * Choose between a number of predefined sort order files.
     *
     * @parameter expression="${sort.predefinedSortOrder}"
     */
    private String predefinedSortOrder;

    /**
     * Custom sort order file.
     *
     * @parameter expression="${sort.sortOrderFile}"
     */
    private String sortOrderFile;

    /**
     * Should dependencies be sorted by groupId and artifactId.
     *
     * @parameter expression="${sort.sortDependencies}" default-value="false"
     */
    private boolean sortDependencies;

    /**
     * Should plugins be sorted by groupId and artifactId.
     *
     * @parameter expression="${sort.sortPlugins}" default-value="false"
     */
    private boolean sortPlugins;

    /**
     * Should the Maven pom properties be sorted alphabetically. Affects both
     * project/properties and project/profiles/profile/properties
     *
     * @parameter expression="${sort.sortProperties}" default-value="false"
     */
    private boolean sortProperties;

    /**
     * Encoding for the files.
     *
     * @parameter expression="${sort.encoding}" default-value="UTF-8"
     */
    private String encoding;


//    /**
//     * Should a backup copy be created for the sorted pom.
//     *
//     * @parameter expression="${sort.createBackupFile}" default-value="true"
//     */
//    private boolean createBackupFile;
//
//    /**
//     * Name of the file extension for the backup file.
//     *
//     * @parameter expression="${sort.backupFileExtension}" default-value=".bak"
//     */
//    private String backupFileExtension;
//
//    /**
//     * Line separator for sorted pom. Can be either \n, \r or \r\n
//     *
//     * @parameter expression="${sort.lineSeparator}"
//     * default-value="${line.separator}"
//     */
//    private String lineSeparator;
//
//    /**
//     * Should empty xml elements be expanded or not. Example:
//     * &lt;configuration&gt;&lt;/configuration&gt; or &lt;configuration/&gt;
//     *
//     * @parameter expression="${sort.expandEmptyElements}" default-value="true"
//     */
//    private boolean expandEmptyElements;
//
//    /**
//     * Should blank lines in the pom-file be perserved. A maximum of one line is preserved between each tag.
//     *
//     * @parameter expression="${sort.keepBlankLines}" default-value="false"
//     */
//    private boolean keepBlankLines;
//
//    /**
//     * Number of space characters to use as indentation. A value of -1 indicates
//     * that tab character should be used instead.
//     *
//     * @parameter expression="${sort.nrOfIndentSpace}" default-value="2"
//     */
//    private int nrOfIndentSpace;
//
//    /**
//     * Should blank lines (if preserved) have indentation.
//     *
//     * @parameter expression="${sort.indentBlankLines}" default-value="false"
//     */
//    private boolean indentBlankLines;


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
        setup();
        sortPom();
    }

    void setup() throws MojoFailureException {
//        String indentCharacters = new IndentCharacters(nrOfIndentSpace).getIndentCharacters();
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setPomFile(pomFile)
                .setEncoding(encoding)
//                .setBackupInfo(createBackupFile, backupFileExtension)
//                .setFormatting(lineSeparator, expandEmptyElements, keepBlankLines)
//                .setIndent(indentCharacters, indentBlankLines)
                .setSortOrder(sortOrderFile, predefinedSortOrder)
                .setSortEntities(sortDependencies, sortPlugins, sortProperties).createPluginParameters();
        sortPomImpl.setup(getLog(), pluginParameters);
    }

    private void sortPom() throws MojoFailureException {
        sortPomImpl.sortPom();
    }

}
