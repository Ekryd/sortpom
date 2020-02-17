package sortpom.logger;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author bjorn
 * @since 2013-10-19
 */
public class MavenLoggerTest {
    private final Log logMock = mock(Log.class);
    private MavenLogger mavenLogger;

    @BeforeEach
    public void setUp() {
        mavenLogger = new MavenLogger(logMock);
    }

    @Test
    public void warnShouldOutputWarnLevel() {
        mavenLogger.warn("Gurka");

        verify(logMock).warn("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void infoShouldOutputInfoLevel() {
        mavenLogger.info("Gurka");

        verify(logMock).info("Gurka");
        verifyNoMoreInteractions(logMock);
    }

    @Test
    public void errorShouldOutputErrorLevel() {
        mavenLogger.error("Gurka");

        verify(logMock).error("Gurka");
        verifyNoMoreInteractions(logMock);
    }
}
