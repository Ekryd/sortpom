package sortpom;

import java.io.*;

/** Contains all parameters that are sent to the plugin */
public class PluginParameters {
	public final File pomFile;
	public final boolean createBackupFile;
	public final String backupFileExtension;
	public final String encoding;
	public final String lineSeparator;
	public final String indentCharacters;
	public final boolean expandEmptyElements;
	public final String predefinedSortOrder;
	public final String customSortOrderFile;
	public final boolean sortDependencies;
	public final boolean sortPlugins;
	public final boolean sortProperties;
    public final boolean keepBlankLines;

	PluginParameters(File pomFile, boolean createBackupFile, String backupFileExtension, String encoding,
                     String lineSeparator, String indentCharacters, boolean expandEmptyElements, String predefinedSortOrder,
                     String customSortOrderFile, boolean sortDependencies, boolean sortPlugins, boolean sortProperties, boolean keepBlankLines) {
		this.pomFile = pomFile;
		this.createBackupFile = createBackupFile;
		this.backupFileExtension = backupFileExtension;
		this.encoding = encoding;
		this.lineSeparator = lineSeparator;
		this.indentCharacters = indentCharacters;
		this.expandEmptyElements = expandEmptyElements;
		this.predefinedSortOrder = predefinedSortOrder;
		this.customSortOrderFile = customSortOrderFile;
		this.sortDependencies = sortDependencies;
		this.sortPlugins = sortPlugins;
		this.sortProperties = sortProperties;
        this.keepBlankLines = keepBlankLines;
    }

}
