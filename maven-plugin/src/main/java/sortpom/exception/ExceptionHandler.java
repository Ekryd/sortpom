package sortpom.exception;

import org.apache.maven.plugin.MojoFailureException;

public final class ExceptionHandler {
    private final FailureException fex;

    public ExceptionHandler(FailureException fex) {
        this.fex = fex;
    }

    public void throwMojoFailureException() throws MojoFailureException {
        if (fex.getCause() != null) {
            throw new MojoFailureException(fex.getMessage(), fex.getCause());
        } else {
            throw new MojoFailureException(fex.getMessage());
        }
    }
}