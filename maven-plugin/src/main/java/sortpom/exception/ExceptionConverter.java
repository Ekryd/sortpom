package sortpom.exception;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Converts internal runtime FailureException in a method to a MojoFailureException in order to give nice output to 
 * the Maven framework
 */
public class ExceptionConverter {
    private final Runnable method;
    private FailureException fex;

    public ExceptionConverter(Runnable method) {
        this.method = method;
    }

    public void executeAndConvertException() throws MojoFailureException {
        try {
            method.run();
        } catch (FailureException fex) {
            this.fex = fex;
        }
        if (fex != null) {
            throwMojoFailureException();
        }
    }

    private void throwMojoFailureException() throws MojoFailureException {
        if (fex.getCause() != null) {
            throw new MojoFailureException(fex.getMessage(), fex.getCause());
        } else {
            throw new MojoFailureException(fex.getMessage());
        }
    }
}
