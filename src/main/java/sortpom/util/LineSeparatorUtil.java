package sortpom.util;

import org.apache.maven.plugin.MojoFailureException;

import java.util.Arrays;

/**
 * Encapsulates LineSeparation logic.
 *
 * @author bjorn
 */
public class LineSeparatorUtil {
    private final String string;

    /**
     * Creates a line separator and makes sure that it is either &#92;n, &#92;r or &#92;r&#92;n
     *
     * @param lineSeparatorString The line separator characters
     * @throws MojoFailureException
     */
    public LineSeparatorUtil(final String lineSeparatorString) throws MojoFailureException {
        string = lineSeparatorString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
        if (isIllegalString()) {
            throw new MojoFailureException(
                    "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were "
                            + Arrays.toString(lineSeparatorString.getBytes()));
        }
    }

    private boolean isIllegalString() {
        return !(string.equals("\n") || string.equals("\r") || string.equals("\r\n"));
    }

    @Override
    public String toString() {
        return string;
    }

}
