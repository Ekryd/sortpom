package sortpom;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import sortpom.logger.MavenLogger;
import sortpom.logger.SortPomLogger;

/** Common parent for both SortMojo and VerifyMojo */
abstract class AbstractParentMojo extends AbstractMojo {

  /** This is the File instance that refers to the location of the pom that should be sorted. */
  @Parameter(property = "sort.pomFile", defaultValue = "${project.file}")
  File pomFile;

  /** Should a backup copy be created for the sorted pom. */
  @Parameter(property = "sort.createBackupFile", defaultValue = "true")
  boolean createBackupFile;

  /** Name of the file extension for the backup file. */
  @Parameter(property = "sort.backupFileExtension", defaultValue = ".bak")
  String backupFileExtension;

  /** Encoding for the files. */
  @Parameter(property = "sort.encoding", defaultValue = "UTF-8")
  String encoding;

  /** Line separator for sorted pom. Can be either \n, \r or \r\n */
  @Parameter(property = "sort.lineSeparator", defaultValue = "${line.separator}")
  String lineSeparator;

  /**
   * Should an empty xml element be expanded with start and end tag, or be written as an
   * empty-element tag.
   */
  @Parameter(property = "sort.expandEmptyElements", defaultValue = "true")
  boolean expandEmptyElements;

  /** Should a non-expanded empty-element tag have a space before the closing slash. */
  @Parameter(property = "sort.spaceBeforeCloseEmptyElement", defaultValue = "false")
  boolean spaceBeforeCloseEmptyElement;

  /**
   * Should blank lines in the pom-file be preserved. A maximum of one line is preserved between
   * each tag.
   */
  @Parameter(property = "sort.keepBlankLines", defaultValue = "true")
  boolean keepBlankLines;

  /** Whether to ensure that sorted pom ends with a newline. */
  @Parameter(property = "sort.endWithNewline", defaultValue = "true")
  boolean endWithNewline;

  /**
   * Number of space characters to use as indentation. A value of -1 indicates that tab character
   * should be used instead.
   */
  @Parameter(property = "sort.nrOfIndentSpace", defaultValue = "2")
  int nrOfIndentSpace;

  /** Ignore line separators when comparing current POM with sorted one */
  @Parameter(property = "sort.ignoreLineSeparators", defaultValue = "true")
  boolean ignoreLineSeparators;

  /** Should blank lines (if preserved) have indentation. */
  @Parameter(property = "sort.indentBlankLines", defaultValue = "false")
  boolean indentBlankLines;

  /** Deprecated! Use indentAttribute=schemaLocation instead. */
  @Parameter(property = "sort.indentSchemaLocation", defaultValue = "false")
  boolean indentSchemaLocation;

  /**
   * Should the xml attributes be indented. Can be either 'schemaLocation' or 'all'. The attribute
   * will be indented (2 * nrOfIndentSpace) characters. 'schemaLocation' only indents the
   * schemaLocation attribute in the project element.
   */
  @Parameter(property = "sort.indentAttribute")
  String indentAttribute;

  /** Choose between a number of predefined sort order files. */
  @Parameter(property = "sort.predefinedSortOrder", defaultValue = "recommended_2008_06")
  String predefinedSortOrder;

  /** Custom sort order file. */
  @Parameter(property = "sort.sortOrderFile")
  String sortOrderFile;

  /**
   * Comma-separated ordered list how dependencies should be sorted. Example:
   * scope,groupId,artifactId. If scope is specified in the list then the scope ranking is IMPORT,
   * COMPILE, PROVIDED, SYSTEM, RUNTIME and TEST. The list can be separated by ",;:"
   */
  @Parameter(property = "sort.sortDependencies")
  String sortDependencies;

  /**
   * Comma-separated ordered list how exclusions, for dependencies, should be sorted. Example:
   * groupId,artifactId The list can be separated by ",;:"
   */
  @Parameter(property = "sort.sortDependencyExclusions")
  String sortDependencyExclusions;

  /**
   * Comma-separated ordered list how dependencies in dependency management should be sorted.
   * Example: scope,groupId,artifactId. If scope is specified in the list then the scope ranking is
   * IMPORT, COMPILE, PROVIDED, SYSTEM, RUNTIME and TEST. The list can be separated by ",;:". It
   * would take precedence if present and would fall back to {@link #sortDependencies} if not
   * present. The value NONE can be used to avoid sorting dependency management at all.
   */
  @Parameter(property = "sort.sortDependencyManagement")
  String sortDependencyManagement;

  /**
   * Comma-separated ordered list how plugins should be sorted. Example: groupId,artifactId The list
   * can be separated by ",;:"
   */
  @Parameter(property = "sort.sortPlugins")
  String sortPlugins;

  /**
   * Should the Maven pom properties be sorted alphabetically. Affects both project/properties and
   * project/profiles/profile/properties
   */
  @Parameter(property = "sort.sortProperties", defaultValue = "false")
  boolean sortProperties;

  /** Should the Maven pom sub modules be sorted alphabetically. */
  @Parameter(property = "sort.sortModules", defaultValue = "false")
  boolean sortModules;

  /** Should the Maven pom execution sections be sorted by phase and then alphabetically. */
  @Parameter(property = "sort.sortExecutions", defaultValue = "false")
  boolean sortExecutions;

  /** Whether to keep the file timestamps of old POM file when creating new POM file. */
  @Parameter(property = "sort.keepTimestamp", defaultValue = "false")
  boolean keepTimestamp;

  /** Set this to 'true' to bypass sortpom plugin */
  @Parameter(property = "sort.skip", defaultValue = "false")
  private boolean skip;

  /** Set this to 'true' to disable plugin info output */
  @Parameter(property = "sort.quiet", defaultValue = "false")
  private boolean quiet;

  final SortPomImpl sortPomImpl = new SortPomImpl();

  /**
   * Execute plugin.
   *
   * @throws org.apache.maven.plugin.MojoFailureException exception that will be handled by plugin
   *     framework
   * @see org.apache.maven.plugin.Mojo#execute()
   */
  @Override
  public void execute() throws MojoFailureException {
    var mavenLogger = new MavenLogger(getLog(), quiet);
    if (skip) {
      mavenLogger.info("Skipping Sortpom");
    } else {
      setup(mavenLogger);
      sortPom();
    }
  }

  protected abstract void sortPom() throws MojoFailureException;

  protected abstract void setup(SortPomLogger mavenLogger) throws MojoFailureException;
}
