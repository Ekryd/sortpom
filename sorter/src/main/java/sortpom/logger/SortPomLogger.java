package sortpom.logger;

/**
 * @author bjorn
 * @since 2012-12-21
 */
public interface SortPomLogger {
    /**
     * Send a message to the log in the <b>warn</b> error level.
     *
     * @param content warning message
     */
    void warn( CharSequence content );

    /**
     * Send a message to the log in the <b>info</b> error level.
     *
     * @param content info message
     */
    void info( CharSequence content );

    /**
     * Send a message to the log in the <b>error</b> error level.
     *
     * @param content error message
     */
    void error( CharSequence content );
}
