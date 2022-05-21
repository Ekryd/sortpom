package sortpom.exception;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Converts internal runtime FailureException in a method to a MojoFailureException in order to give
 * nice output to the Maven framework
 */
public class ExceptionConverter {
  private final Runnable method;

  public ExceptionConverter(Runnable method) {
    this.method = method;
  }

  public void executeAndConvertException() throws MojoFailureException {
    try {
      method.run();
    } catch (FailureException fex) {
      if (fex.getCause() != null) {
        throw new MojoFailureException(fex.getMessage(), fex.getCause());
      } else {
        throw new MojoFailureException(fex.getMessage());
      }
    }
  }
}
