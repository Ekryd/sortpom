package sortpom;

import java.io.*;

import org.apache.maven.plugin.*;

import sortpom.util.*;

/**
 * Mojo (Maven plugin) that sorts the pomfile for a maven project.
 * 
 * @author Bjorn Ekryd
 * @goal sort
 */
@SuppressWarnings({ "UnusedDeclaration" })
public class SortPomMojo extends AbstractMojo {
	/**
	 * This is the File instance that refers to the location of the POM that
	 * should be sorted.
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
	 * @parameter expression="${sort.lineSeparator}"
	 *            default-value="${line.separator}"
	 */
	private String lineSeparator;

	/**
	 * Number of space characters to use as indentation. A value of -1 indicates
	 * that tab character should be used instead.
	 * 
	 * @parameter expression="${sort.nrOfIndentSpace}" default-value="2"
	 */
	private int nrOfIndentSpace;

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

	private final SortPomImpl sortPomImpl = new SortPomImpl();

	public SortPomMojo() {}

	/**
	 * Execute plugin.
	 * 
	 * @throws MojoFailureException
	 *             exception taht will be handled by plugin framework
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	@Override
	public void execute() throws MojoFailureException {
		setup();
		sortPom();
	}

	void setup() throws MojoFailureException {
		String indentCharacters = new IndentCharacters(nrOfIndentSpace).getIndentCharacters();
		PluginParameters pluginParameters = new PluginParametersBuilder().setPomFile(pomFile)
				.setBackupInfo(createBackupFile, backupFileExtension)
				.setFormatting(encoding, lineSeparator, indentCharacters)
				.setSortOrder(sortOrderFile, predefinedSortOrder).setSortEntities(sortDependencies, sortPlugins)
				.createPluginParameters();
		sortPomImpl.setup(getLog(), pluginParameters);
	}

	private void sortPom() throws MojoFailureException {
		sortPomImpl.sortPom();
	}

}
