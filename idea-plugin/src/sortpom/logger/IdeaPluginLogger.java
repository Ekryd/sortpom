package sortpom.logger;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Maps the SortPom logger to IntelliJ logger framework
 *
 * @author bjorn
 * @since 2013-01-02
 */
public class IdeaPluginLogger implements SortPomLogger {
    private Logger logger = Logger.getInstance("SortPom plugin");


    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }
}
