package sortpom.util;

import java.util.Arrays;

import org.apache.maven.plugin.MojoFailureException;

/**
 * Encapsulates LineSeparation logic.
 *
 * @author bjorn
 *
 */
public class LineSeparator {
	private final String string;

	/**
	 * Creates a line separator and makes sure that it is either &#92;n, &#92;r or &#92;r&#92;n
	 *
	 * @param lineSeparatorString
	 * @throws MojoFailureException
	 */
	public LineSeparator(final String lineSeparatorString) throws MojoFailureException {
		string = lineSeparatorString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
		if (isIllegalString()) {
			throw new MojoFailureException(
					"LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were "
							+ Arrays.toString(lineSeparatorString.getBytes()));
		}
	}

	@Override
	public String toString() {
		return string;
	}

	private boolean isIllegalString() {
		return !(string.equals("\n") || string.equals("\r") || string.equals("\r\n"));
	}
}
