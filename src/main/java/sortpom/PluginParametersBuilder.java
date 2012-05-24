package sortpom;

import java.io.File;

class PluginParametersBuilder {
    private File pomFile;
    private boolean createBackupFile;
    private String backupFileExtension;
    private String encoding;
    private String lineSeparator;
    private String indentCharacters;
    private boolean expandEmptyElements;
    private String predefinedSortOrder;
    private String customSortOrderFile;
    private boolean sortDependencies;
    private boolean sortPlugins;
    private boolean sortProperties;
    private boolean keepBlankLines;

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
                                                 final String indentCharacters, final boolean expandEmptyElements,
                                                 final boolean keepBlankLines) {
        this.encoding = encoding;
        this.lineSeparator = lineSeparator;
        this.indentCharacters = indentCharacters;
        this.expandEmptyElements = expandEmptyElements;
        this.keepBlankLines = keepBlankLines;
        return this;
    }

    public PluginParametersBuilder setSortOrder(final String customSortOrderFile, final String predefinedSortOrder) {
        this.customSortOrderFile = customSortOrderFile;
        this.predefinedSortOrder = predefinedSortOrder;
        return this;
    }

    public PluginParametersBuilder setSortEntities(final boolean sortDependencies, final boolean sortPlugins,
                                                   final boolean sortProperties) {
        this.sortDependencies = sortDependencies;
        this.sortPlugins = sortPlugins;
        this.sortProperties = sortProperties;
        return this;
    }

    public PluginParameters createPluginParameters() {
        return new PluginParameters(pomFile, createBackupFile, backupFileExtension, encoding, lineSeparator,
                indentCharacters, expandEmptyElements, predefinedSortOrder, customSortOrderFile, sortDependencies,
                sortPlugins, sortProperties, keepBlankLines);
    }
}
