package sortpom.util;

import static org.junit.Assert.*;

import org.apache.maven.plugin.*;
import org.junit.*;
import org.junit.rules.*;

public class LineSeparatorTest {
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testCharInput() throws MojoFailureException {
		assertEquals("\n", new LineSeparatorUtil("\n").toString());
		assertEquals("\r", new LineSeparatorUtil("\r").toString());
		assertEquals("\r\n", new LineSeparatorUtil("\r\n").toString());
	}

	@Test
	public void testFailedInput1() throws MojoFailureException {
		thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 110]");
		new LineSeparatorUtil("\nn");
	}

	@Test
	public void testFailedInput2() throws MojoFailureException {
		thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 10]");
		new LineSeparatorUtil("\n\n").toString();
	}

	@Test
	public void testFailedInput3() throws MojoFailureException {
		thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [103, 117, 114, 107, 97]");
		new LineSeparatorUtil("gurka");
	}

	@Test
	public void testFailedInput4() throws MojoFailureException {
		thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were []");
		new LineSeparatorUtil("");
	}

	@Test
	public void testMixedInput() throws MojoFailureException {
		assertEquals("\r\n", new LineSeparatorUtil("\\r\n").toString());
		assertEquals("\r\n", new LineSeparatorUtil("\r\\n").toString());
	}

	@Test
	public void testStringInput() throws MojoFailureException {
		assertEquals("\n", new LineSeparatorUtil("\\n").toString());
		assertEquals("\r", new LineSeparatorUtil("\\r").toString());
		assertEquals("\r\n", new LineSeparatorUtil("\\r\\n").toString());
	}

}
