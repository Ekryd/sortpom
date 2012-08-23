package sortpom.parameter;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class IndentCharactersParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void zeroIndentCharactersShouldResultInEmptyIndentString() throws MojoFailureException {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(0, true)
                .createPluginParameters();

        assertEquals("", pluginParameters.indentCharacters);
    }

    @Test
    public void oneIndentCharacterShouldResultInOneSpace() throws MojoFailureException {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(1, true)
                .createPluginParameters();

        assertEquals(" ", pluginParameters.indentCharacters);
    }

    @Test
    public void test255IndentCharacterShouldResultIn255Space() throws MojoFailureException {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(255, true)
                .createPluginParameters();

        // Test for only space
        assertEquals(true, pluginParameters.indentCharacters.matches("^ *$"));
        assertEquals(255, pluginParameters.indentCharacters.length());
    }

    @Test
    public void minusOneIndentCharacterShouldResultInOneTab() throws MojoFailureException {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setIndent(-1, true)
                .createPluginParameters();

        assertEquals("\t", pluginParameters.indentCharacters);
    }

    @Test
    public void minusTwoShouldFail() throws MojoFailureException {
        thrown.expect(MojoFailureException.class);
        thrown.expectMessage("");

        new PluginParametersBuilder()
                .setIndent(-2, true)
                .createPluginParameters();
    }

    @Test
    public void moreThan255ShouldFail() throws MojoFailureException {
        thrown.expect(MojoFailureException.class);
        thrown.expectMessage("");

        new PluginParametersBuilder()
                .setIndent(256, true)
                .createPluginParameters();
    }

}
