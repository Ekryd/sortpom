package sortpom.parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VerifyFailParameterTest {

    @Test
    void stopIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("sToP")
                .build();

        assertEquals(VerifyFailType.STOP, pluginParameters.verifyFailType);
    }

    @Test
    void warnIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("wArN")
                .build();

        assertEquals(VerifyFailType.WARN, pluginParameters.verifyFailType);
    }

    @Test
    void sortIgnoreCaseValueIsOk() {
        PluginParameters pluginParameters = PluginParameters.builder()
                .setVerifyFail("sOrT")
                .build();

        assertEquals(VerifyFailType.SORT, pluginParameters.verifyFailType);
    }

    @Test
    void nullValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail(null)
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: null")));
    }

    @Test
    void emptyValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail("")
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: ")));
    }

    @Test
    void wrongValueIsNotOk() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setVerifyFail("gurka")
                .build();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("verifyFail must be either SORT, WARN or STOP. Was: gurka")));
    }

}
