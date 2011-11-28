package sortpom.util;

import static org.junit.Assert.*;

import org.apache.maven.plugin.*;
import org.junit.*;

public class IndentCharactersTest {

	@Test
	public void zeroIndentCharactersShouldResultInEmptyIndentString() throws MojoFailureException {
		assertEquals("", new IndentCharacters(0).getIndentCharacters());
	}

	@Test
	public void oneIndentCharacterShouldResultInOneSpace() throws MojoFailureException {
		assertEquals(" ", new IndentCharacters(1).getIndentCharacters());
	}

	@Test
	public void test255IndentCharacterShouldResultIn255Space() throws MojoFailureException {
		String indentCharacters = new IndentCharacters(255).getIndentCharacters();

		// Test for only space
		assertEquals(true, indentCharacters.matches("^ *$"));
		assertEquals(255, indentCharacters.length());
	}

	@Test
	public void minusOneIndentCharacterShouldResultInOneTab() throws MojoFailureException {
		assertEquals("\t", new IndentCharacters(-1).getIndentCharacters());
	}

	@Test(expected = MojoFailureException.class)
	public void minusTwoShouldFail() throws MojoFailureException {
		new IndentCharacters(-2).getIndentCharacters();
	}

	@Test(expected = MojoFailureException.class)
	public void moreThan255ShouldFail() throws MojoFailureException {
		new IndentCharacters(256).getIndentCharacters();
	}

}
