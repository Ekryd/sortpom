package sortpom.logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2013-10-19
 */
class MavenLoggerTest {
  private final Log logMock = mock(Log.class);
  private MavenLogger mavenLogger;

  @BeforeEach
  void setUp() {
    mavenLogger = new MavenLogger(logMock, false);
  }

  @Test
  void warnShouldOutputWarnLevel() {
    mavenLogger.warn("Gurka");

    verify(logMock).warn("Gurka");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void infoShouldOutputInfoLevel() {
    mavenLogger.info("Gurka");

    verify(logMock).info("Gurka");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void quietLoggingShouldOutputInfoOnDebugLevel() {
    var quietMavenLogger = new MavenLogger(logMock, true);
    quietMavenLogger.info("Gurka");

    verify(logMock).debug("Gurka");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void quietLoggingShouldStillOutputErrorLevel() {
    var quietMavenLogger = new MavenLogger(logMock, true);
    quietMavenLogger.error("Error!");

    verify(logMock).error("Error!");
    verifyNoMoreInteractions(logMock);
  }

  @Test
  void errorShouldOutputErrorLevel() {
    mavenLogger.error("Error!");

    verify(logMock).error("Error!");
    verifyNoMoreInteractions(logMock);
  }
}
