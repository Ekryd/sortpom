package sortpom.parameter;

import java.io.File;

/** Builder for the PluginParameters class */
public class PluginParametersBuilder {
    private File pomFile;
    private boolean createBackupFile;
    private String backupFileExtension;
    private String encoding;
    private LineSeparatorUtil lineSeparatorUtil;
    private String indentCharacters;
    private boolean indentBlankLines;
    private boolean expandEmptyElements;
    private String predefinedSortOrder;
    private String customSortOrderFile;
    private DependencySortOrder sortDependencies;
    private DependencySortOrder sortPlugins;
    private boolean sortProperties;
    private boolean keepBlankLines;
    private VerifyFailType verifyFailType;
    private boolean ignoreLineSeparators;

    /** Sets pomFile location */
    public PluginParametersBuilder setPomFile(final File pomFile) {
        this.pomFile = pomFile;
        return this;
    }

    /** Sets information regarding backup file */
    public PluginParametersBuilder setBackupInfo(final boolean createBackupFile, final String backupFileExtension) {
        this.createBackupFile = createBackupFile;
        this.backupFileExtension = backupFileExtension;
        return this;
    }

    /** Sets which encoding should be used throughout the plugin */
    public PluginParametersBuilder setEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
    }

    /** Sets formatting information that is used when the pom file is sorted */
    public PluginParametersBuilder setFormatting(final String lineSeparator,
                                                 final boolean expandEmptyElements,
                                                 final boolean keepBlankLines) {
        this.lineSeparatorUtil = new LineSeparatorUtil(lineSeparator);
        this.expandEmptyElements = expandEmptyElements;
        this.keepBlankLines = keepBlankLines;
        return this;
    }

    /** Sets indent information that is used when the pom file is sorted */
    public PluginParametersBuilder setIndent(final int nrOfIndentSpace, final boolean indentBlankLines) {
        this.indentCharacters = new IndentCharacters(nrOfIndentSpace).getIndentCharacters();
        this.indentBlankLines = indentBlankLines;
        return this;
    }

    /** Sets which sort order that should be used when sorting */
    public PluginParametersBuilder setSortOrder(final String customSortOrderFile, final String predefinedSortOrder) {
        this.customSortOrderFile = customSortOrderFile;
        this.predefinedSortOrder = predefinedSortOrder;
        return this;
    }

    /** Sets if any additional pom file elements should be sorted */
    public PluginParametersBuilder setSortEntities(final String sortDependencies,
                                                   final String sortPlugins, final boolean sortProperties) {
        this.sortDependencies = new DependencySortOrder(sortDependencies);
        this.sortPlugins = new DependencySortOrder(sortPlugins);
        this.sortProperties = sortProperties;
        return this;
    }

    /** Sets the verify operation behaviour */
    public PluginParametersBuilder setVerifyFail(String verifyFail) {
        this.verifyFailType = VerifyFailType.fromString(verifyFail);
        return this;
    }

    /** Sets triggers to decide when the pom should be sorted **/
    public PluginParametersBuilder setTriggers(boolean ignoreLineSeparators) {
        this.ignoreLineSeparators = ignoreLineSeparators;
        return this;
    }

    /** Build the PluginParameters instance */
    public PluginParameters createPluginParameters() {
        return new PluginParameters(pomFile, createBackupFile, backupFileExtension,
                encoding, lineSeparatorUtil, expandEmptyElements, keepBlankLines, indentCharacters, indentBlankLines,
                predefinedSortOrder, customSortOrderFile,
                sortDependencies, sortPlugins, sortProperties,
                verifyFailType, ignoreLineSeparators);
    }
}
