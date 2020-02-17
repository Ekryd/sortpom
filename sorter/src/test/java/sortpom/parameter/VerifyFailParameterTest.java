package sortpom.parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VerifyFailParameterTest {

    @Test
    public void stopIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("sToP")
                .build();

        assertEquals(VerifyFailType.STOP, pluginParameters.verifyFailType);
    }

    @Test
    public void warnIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("wArN")
                .build();

        assertEquals(VerifyFailType.WARN, pluginParameters.verifyFailType);
    }

    @Test
    public void sortIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("sOrT")
                .build();

        assertEquals(VerifyFailType.SORT, pluginParameters.verifyFailType);
    }

    @Test
    public void nullValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail(null)
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: null")));
    }

    @Test
    public void emptyValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail("")
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: ")));
    }

    @Test
    public void wrongValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail("gurka")
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: gurka")));
    }

}
