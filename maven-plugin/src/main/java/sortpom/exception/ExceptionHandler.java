package sortpom.exception;

import org.apache.maven.plugin.MojoFailureException;

public final class ExceptionHandler {
    private ExceptionHandler() {
    }

    public static void throwMojoFailureException(FailureException fex) throws MojoFailureException {
        if (fex.getCause() != null) {
            throw new MojoFailureException(fex.getMessage(), fex.getCause());
        } else {
            throw new MojoFailureException(fex.getMessage());
        }
    }
}