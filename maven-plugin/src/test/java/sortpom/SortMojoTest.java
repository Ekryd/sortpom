package sortpom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import refutils.ReflectionHelper;
import sortpom.exception.FailureException;
import sortpom.logger.MavenLogger;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2012-08-23
 */
class SortMojoTest {
  private final SortPomImpl sortPom = mock(SortPomImpl.class);
  private final Log log = mock(Log.class);
  private SortMojo sortMojo;

  @BeforeEach
  void setup() {
    sortMojo = new SortMojo();
    var mojoHelper = new ReflectionHelper(sortMojo);
    mojoHelper.setField(sortPom);
    mojoHelper.setField("lineSeparator", "\n");
  }

  @Test
  void executeShouldStartMojo() throws Exception {
    sortMojo.execute();

    verify(sortPom).setup(any(SortPomLogger.class), any(PluginParameters.class));
    verify(sortPom).sortPom();
    verifyNoMoreInteractions(sortPom);
  }

  @Test
  void thrownExceptionShouldBeConvertedToMojoException() {
    doThrow(new FailureException("Gurka")).when(sortPom).sortPom();

    Executable testMethod = () -> sortMojo.execute();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
  }

  @Test
  void thrownExceptionShouldBeConvertedToMojoExceptionInSetup() {
    doThrow(new FailureException("Gurka"))
        .when(sortPom)
        .setup(any(SortPomLogger.class), any(PluginParameters.class));

    Executable testMethod = () -> sortMojo.setup(new MavenLogger(null, false));

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
  }

  @Test
  void skipParameterShouldSkipExecution() throws Exception {
    new ReflectionHelper(sortMojo).setField("skip", true);
    new ReflectionHelper(sortMojo).setField(log);

    sortMojo.execute();

    verify(log).info("Skipping Sortpom");
    verifyNoMoreInteractions(sortPom);
  }
}
