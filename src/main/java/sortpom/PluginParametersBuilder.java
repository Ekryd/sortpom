package sortpom;

import java.io.*;

public class PluginParametersBuilder {
	private File pomFile;
	private boolean createBackupFile;
	private String backupFileExtension;
	private String encoding;
	private String lineSeparator;
	private String indentCharacters;
	private boolean expandEmptyElements;
	private String predefinedSortOrder;
	private String sortOrderFile;
	private boolean sortDependencies;
	private boolean sortPlugins;

	public PluginParametersBuilder setPomFile(final File pomFile) {
		this.pomFile = pomFile;
		return this;
	}

	public PluginParametersBuilder setBackupInfo(final boolean createBackupFile, final String backupFileExtension) {
		this.createBackupFile = createBackupFile;
		this.backupFileExtension = backupFileExtension;
		return this;
	}

	public PluginParametersBuilder setFormatting(final String encoding, final String lineSeparator,
			final String indentCharacters, final boolean expandEmptyElements) {
		this.encoding = encoding;
		this.lineSeparator = lineSeparator;
		this.indentCharacters = indentCharacters;
		this.expandEmptyElements = expandEmptyElements;
		return this;
	}

	public PluginParametersBuilder setSortOrder(final String sortOrderFile, final String predefinedSortOrder) {
		this.sortOrderFile = sortOrderFile;
		this.predefinedSortOrder = predefinedSortOrder;
		return this;
	}

	public PluginParametersBuilder setSortEntities(final boolean sortDependencies, final boolean sortPlugins) {
		this.sortDependencies = sortDependencies;
		this.sortPlugins = sortPlugins;
		return this;
	}

	public PluginParameters createPluginParameters() {
		return new PluginParameters(pomFile, createBackupFile, backupFileExtension, encoding, lineSeparator,
				indentCharacters, expandEmptyElements, predefinedSortOrder, sortOrderFile, sortDependencies,
				sortPlugins);
	}
}
