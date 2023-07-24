package sortpom.logger;

import org.apache.maven.plugin.logging.Log;

/**
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenLogger implements SortPomLogger {
  private final Log pluginLogger;
  private final boolean quiet;

  public MavenLogger(Log pluginLogger, boolean quiet) {
    this.pluginLogger = pluginLogger;
    this.quiet = quiet;
  }

  @Override
  public void warn(String content) {
    pluginLogger.warn(content);
  }

  @Override
  public void info(String content) {
    if (quiet) {
      pluginLogger.debug(content);
    } else {
      pluginLogger.info(content);
    }
  }

  @Override
  public void error(String content) {
    pluginLogger.error(content);
  }
}
