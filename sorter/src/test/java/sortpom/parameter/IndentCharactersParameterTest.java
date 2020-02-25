package sortpom.parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndentCharactersParameterTest {

    @Test
    public void zeroIndentCharactersShouldResultInEmptyIndentString() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setIndent(0, true)
                .build();

        assertEquals("", pluginParameters.indentCharacters);
    }

    @Test
    public void oneIndentCharacterShouldResultInOneSpace() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setIndent(1, true)
                .build();

        assertEquals(" ", pluginParameters.indentCharacters);
    }

    @Test
    public void test255IndentCharacterShouldResultIn255Space() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setIndent(255, true)
                .build();

        // Test for only space
        assertTrue(pluginParameters.indentCharacters.matches("^ *$"));
        assertEquals(255, pluginParameters.indentCharacters.length());
    }

    @Test
    public void minusOneIndentCharacterShouldResultInOneTab() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setIndent(-1, true)
                .build();

        assertEquals("\t", pluginParameters.indentCharacters);
    }

    @Test
    public void minusTwoShouldFail() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setIndent(-2, true)
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("nrOfIndentSpace cannot be below -1 or above 255, was: -2")));
    }

    @Test
    public void moreThan255ShouldFail() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setIndent(256, true)
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("nrOfIndentSpace cannot be below -1 or above 255, was: 256")));
    }

}
