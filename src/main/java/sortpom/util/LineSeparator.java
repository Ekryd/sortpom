package sortpom.util;

import java.util.Arrays;

import org.apache.maven.plugin.MojoExecutionException;

public class LineSeparator {
	private final String string;

	public LineSeparator(final String lineSeparatorString) throws MojoExecutionException {
		string = lineSeparatorString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
		if (isIllegalString()) {
			throw new MojoExecutionException(
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
