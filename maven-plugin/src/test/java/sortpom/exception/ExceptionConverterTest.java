package sortpom.exception;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

/**
 * @author bjorn
 * @since 2013-10-19
 */
class ExceptionConverterTest {

  @Test
  void noExceptionShouldRunJustFine() throws MojoFailureException {
    var exceptionConverter = new ExceptionConverter(() -> {});
    exceptionConverter.executeAndConvertException();
    assertThat(exceptionConverter, is(notNullValue()));
  }

  @Test
  void failureExceptionShouldThrowMojoFailureException() {
    var failureException = new FailureException("Gurka");

    Executable testMethod =
        () ->
            new ExceptionConverter(
                    () -> {
                      throw failureException;
                    })
                .executeAndConvertException();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka")));
  }

  @Test
  void failureExceptionShouldKeepCause() {
    var cause = new IllegalArgumentException("not valid");
    var failureException = new FailureException("Gurka", cause);

    Executable testMethod =
        () ->
            new ExceptionConverter(
                    () -> {
                      throw failureException;
                    })
                .executeAndConvertException();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertAll(
        () -> assertThat("Unexpected message", thrown.getMessage(), is(equalTo("Gurka"))),
        () -> assertThat("Unexpected cause", thrown.getCause(), is(equalTo(cause))));
  }
}
