package sortpom.parameter;

import java.io.File;

public class PluginParametersBuilder {
    private File pomFile;
    private boolean createBackupFile;
    private String backupFileExtension;
    private String encoding;
    private String lineSeparator;
    private String indentCharacters;
    private boolean indentBlankLines;
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
                                                 final boolean expandEmptyElements,
                                                 final boolean keepBlankLines) {
        this.encoding = encoding;
        this.lineSeparator = lineSeparator;
        this.expandEmptyElements = expandEmptyElements;
        this.keepBlankLines = keepBlankLines;
        return this;
    }

    public PluginParametersBuilder setIndent(final String indentCharacters, final boolean indentBlankLines) {
        this.indentCharacters = indentCharacters;
        this.indentBlankLines = indentBlankLines;
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
                expandEmptyElements, keepBlankLines, indentCharacters, indentBlankLines, predefinedSortOrder,
                customSortOrderFile, sortDependencies, sortPlugins, sortProperties);
    }
}
