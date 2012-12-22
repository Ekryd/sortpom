package sortpom.parameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.exception.FailureException;

import static org.junit.Assert.assertEquals;

public class IndentCharactersParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void zeroIndentCharactersShouldResultInEmptyIndentString() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(0, true)
                .createPluginParameters();

        assertEquals("", pluginParameters.indentCharacters);
    }

    @Test
    public void oneIndentCharacterShouldResultInOneSpace() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(1, true)
                .createPluginParameters();

        assertEquals(" ", pluginParameters.indentCharacters);
    }

    @Test
    public void test255IndentCharacterShouldResultIn255Space() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(255, true)
                .createPluginParameters();

        // Test for only space
        assertEquals(true, pluginParameters.indentCharacters.matches("^ *$"));
        assertEquals(255, pluginParameters.indentCharacters.length());
    }

    @Test
    public void minusOneIndentCharacterShouldResultInOneTab() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(-1, true)
                .createPluginParameters();

        assertEquals("\t", pluginParameters.indentCharacters);
    }

    @Test
    public void minusTwoShouldFail() {
        thrown.expect(FailureException.class);
        thrown.expectMessage("");

        new PluginParametersBuilder()
                .setIndent(-2, true)
                .createPluginParameters();
    }

    @Test
    public void moreThan255ShouldFail() {
        thrown.expect(FailureException.class);
        thrown.expectMessage("");

        new PluginParametersBuilder()
                .setIndent(256, true)
                .createPluginParameters();
    }

}
