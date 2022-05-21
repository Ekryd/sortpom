package sortpom.exception;

/**
 * An exception occurring during the execution of the sort (such as a compilation failure). Throwing
 * this exception should cause a "BUILD FAILURE" message to be displayed.
 *
 * @author bjorn
 * @since 2012-12-21
 */
public class FailureException extends RuntimeException {
  public FailureException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public FailureException(String msg) {
    super(msg);
  }
}
