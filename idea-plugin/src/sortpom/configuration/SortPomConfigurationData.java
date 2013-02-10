package sortpom.configuration;

import com.intellij.ide.util.PropertyName;

/**
 * These are the setting values that are going to be saved
 *
 * @author bjorn
 * @since 2013-01-02
 */
public class SortPomConfigurationData {
    @PropertyName(value = "SortPom.sortDependencies")
    public final String sortDependencies;
    @PropertyName(value = "SortPom.sortPlugins")
    public final String sortPlugins;
    @PropertyName(value = "SortPom.predefinedSortOrder")
    public final String predefinedSortOrder;
    @PropertyName(value = "SortPom.lineSeparator")
    public final String lineSeparator;
    @PropertyName(value = "SortPom.nrOfIndentSpace")
    public final int nrOfIndentSpace;
    @PropertyName(value = "SortPom.sortProperties")
    public final boolean sortProperties;
    @PropertyName(value = "SortPom.expandEmptyElements")
    public final boolean expandEmptyElements;
    @PropertyName(value = "SortPom.keepBlankLines")
    public final boolean keepBlankLines;
    @PropertyName(value = "SortPom.indentBlankLines")
    public final boolean indentBlankLines;

    public SortPomConfigurationData(String sortDependencies, String sortPlugins, String predefinedSortOrder, String lineSeparator, int nrOfIndentSpace, boolean sortProperties, boolean expandEmptyElements, boolean keepBlankLines, boolean indentBlankLines) {
        this.sortDependencies = sortDependencies;
        this.sortPlugins = sortPlugins;
        this.predefinedSortOrder = predefinedSortOrder;
        this.lineSeparator = lineSeparator;
        this.nrOfIndentSpace = nrOfIndentSpace;
        this.sortProperties = sortProperties;
        this.expandEmptyElements = expandEmptyElements;
        this.keepBlankLines = keepBlankLines;
        this.indentBlankLines = indentBlankLines;
    }
}
