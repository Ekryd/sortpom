package sortpom.util;

import static org.junit.Assert.*;

import org.apache.maven.plugin.*;
import org.junit.*;

public class LineSeparatorTest {
	@Test
	public void testCharInput() throws MojoFailureException {
		assertEquals("\n", new LineSeparatorUtil("\n").toString());
		assertEquals("\r", new LineSeparatorUtil("\r").toString());
		assertEquals("\r\n", new LineSeparatorUtil("\r\n").toString());
	}

	@Test
	public void testFailedInput1() throws MojoFailureException {
		try {
			new LineSeparatorUtil("\nn").toString();
			fail();
		} catch (MojoFailureException ex) {
			assertEquals("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 110]",
					ex.getMessage());
		}
	}

	@Test
	public void testFailedInput2() throws MojoFailureException {
		try {
			new LineSeparatorUtil("\n\n").toString();
			fail();
		} catch (MojoFailureException ex) {
			assertEquals("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 10]",
					ex.getMessage());
		}
	}

	@Test
	public void testFailedInput3() throws MojoFailureException {
		try {
			new LineSeparatorUtil("gurka").toString();
			fail();
		} catch (MojoFailureException ex) {
			assertEquals(
					"LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [103, 117, 114, 107, 97]",
					ex.getMessage());
		}
	}

	@Test
	public void testFailedInput4() throws MojoFailureException {
		try {
			new LineSeparatorUtil("").toString();
			fail();
		} catch (MojoFailureException ex) {
			assertEquals("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were []",
					ex.getMessage());
		}
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
