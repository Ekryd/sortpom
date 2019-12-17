package sortpom;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import sortpom.exception.ExceptionConverter;
import sortpom.logger.MavenLogger;
import sortpom.parameter.PluginParameters;

/**
 * Sorts the pom.xml for a Maven project.
 *
 * @author Bjorn Ekryd
 */
@Mojo(name = "sort", threadSafe = true, defaultPhase = LifecyclePhase.VALIDATE)
@SuppressWarnings({"UnusedDeclaration", "JavaDoc"})
public class SortMojo extends AbstractParentMojo {

    /**
     * Whether to keep the file timestamps of old POM file when creating new POM file or backup file.
     */
    @Parameter(property = "sort.keepTimestamp", defaultValue = "false")
    boolean keepTimestamp;

    /**
     * Ignore line separators when comparing current POM with sorted one
     */
    @Parameter(property = "sort.ignoreLineSeparators", defaultValue = "true")
    private boolean ignoreLineSeparators;

    public void setup() throws MojoFailureException {
        new ExceptionConverter(() -> {
            PluginParameters pluginParameters = PluginParameters.builder()
                    .setPomFile(pomFile)
                    .setFileOutput(createBackupFile, backupFileExtension, null)
                    .setEncoding(encoding)
                    .setFormatting(lineSeparator, expandEmptyElements, keepBlankLines)
                    .setIndent(nrOfIndentSpace, indentBlankLines)
                    .setSortOrder(sortOrderFile, predefinedSortOrder)
                    .setSortEntities(sortDependencies, sortPlugins, sortProperties, sortModules)
                    .setTriggers(ignoreLineSeparators)
                    .setKeepTimestamp(keepTimestamp)
                    .build();

            sortPomImpl.setup(new MavenLogger(getLog()), pluginParameters);
        }).executeAndConvertException();
    }

    protected void sortPom() throws MojoFailureException {
        new ExceptionConverter(sortPomImpl::sortPom).executeAndConvertException();
    }
    
}
