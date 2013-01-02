package sortpom.logger;

import org.apache.maven.plugin.logging.Log;

/**
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenLogger implements SortPomLogger {
    private final Log log;

    public MavenLogger(Log log) {
        this.log = log;
    }

    @Override
    public void warn(String content) {
        log.warn(content);
    }

    @Override
    public void info(String content) {
        log.info(content);
    }

    @Override
    public void error(String content) {
        log.error(content);
    }
}
