package sortpom.parameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.exception.FailureException;

import static org.junit.Assert.assertEquals;

public class VerifyFailParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void stopIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setVerifyFail("sToP")
                .createPluginParameters();

        assertEquals(VerifyFailType.STOP, pluginParameters.verifyFailType);
    }

    @Test
    public void warnIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setVerifyFail("wArN")
                .createPluginParameters();

        assertEquals(VerifyFailType.WARN, pluginParameters.verifyFailType);
    }

    @Test
    public void sortIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = new PluginParametersBuilder()
                .setVerifyFail("sOrT")
                .createPluginParameters();

        assertEquals(VerifyFailType.SORT, pluginParameters.verifyFailType);
    }

    @Test
    public void nullValueIsNotOk() {
        thrown.expectMessage("verifyFail must be either SORT, WARN or STOP. Was: null");

        new PluginParametersBuilder()
                .setVerifyFail(null)
                .createPluginParameters();
    }

    @Test
    public void emptyValueIsNotOk() {
        thrown.expectMessage("verifyFail must be either SORT, WARN or STOP. Was: ");

        new PluginParametersBuilder()
                .setVerifyFail("")
                .createPluginParameters();
    }

    @Test
    public void wrongValueIsNotOk() {
        thrown.expectMessage("verifyFail must be either SORT, WARN or STOP. Was: gurka");

        new PluginParametersBuilder()
                .setVerifyFail("gurka")
                .createPluginParameters();
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
