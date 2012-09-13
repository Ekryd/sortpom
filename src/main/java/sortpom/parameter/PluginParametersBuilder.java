package sortpom.parameter;

import org.apache.maven.plugin.MojoFailureException;

import java.io.File;

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
    private boolean sortDependencies;
    private boolean sortDependenciesByScope;
    private boolean sortPlugins;
    private boolean sortProperties;
    private boolean keepBlankLines;
    private VerifyFailType verifyFailType;

    public PluginParametersBuilder setPomFile(final File pomFile) {
        this.pomFile = pomFile;
        return this;
    }

    public PluginParametersBuilder setBackupInfo(final boolean createBackupFile, final String backupFileExtension) {
        this.createBackupFile = createBackupFile;
        this.backupFileExtension = backupFileExtension;
        return this;
    }

    public PluginParametersBuilder setEncoding(final String encoding) {
        this.encoding = encoding;
        return this;
    }

    public PluginParametersBuilder setFormatting(final String lineSeparator,
                                                 final boolean expandEmptyElements,
                                                 final boolean keepBlankLines) throws MojoFailureException {
        this.lineSeparatorUtil = new LineSeparatorUtil(lineSeparator);
        this.expandEmptyElements = expandEmptyElements;
        this.keepBlankLines = keepBlankLines;
        return this;
    }

    public PluginParametersBuilder setIndent(final int nrOfIndentSpace, final boolean indentBlankLines) throws MojoFailureException {
        this.indentCharacters = new IndentCharacters(nrOfIndentSpace).getIndentCharacters();
        this.indentBlankLines = indentBlankLines;
        return this;
    }

    public PluginParametersBuilder setSortOrder(final String customSortOrderFile, final String predefinedSortOrder) {
        this.customSortOrderFile = customSortOrderFile;
        this.predefinedSortOrder = predefinedSortOrder;
        return this;
    }

    public PluginParametersBuilder setSortEntities(final boolean sortDependencies, boolean sortDependenciesByScope,
                                                   final boolean sortPlugins, final boolean sortProperties) {
        this.sortDependencies = sortDependencies;
        this.sortDependenciesByScope = sortDependenciesByScope;
        this.sortPlugins = sortPlugins;
        this.sortProperties = sortProperties;
        return this;
    }

    public PluginParametersBuilder setVerifyFail(String verifyFail) throws MojoFailureException {
        this.verifyFailType = VerifyFailType.fromString(verifyFail);
        return this;
    }

    public PluginParameters createPluginParameters() {
        return new PluginParameters(pomFile, createBackupFile, backupFileExtension,
                encoding, lineSeparatorUtil, expandEmptyElements, keepBlankLines, indentCharacters, indentBlankLines,
                predefinedSortOrder, customSortOrderFile,
                sortDependencies, sortDependenciesByScope, sortPlugins, sortProperties,
                verifyFailType);
    }
}
