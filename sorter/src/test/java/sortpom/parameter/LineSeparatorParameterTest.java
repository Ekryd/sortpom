package sortpom.parameter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LineSeparatorParameterTest {

    @Test
    public void lineSeparatorWithSomethingElseShouldThrowException() {

        final Executable testMethod = () ->PluginParameters.builder()
                .setEncoding("UTF-8")
                .setFormatting("***", false, false)
                .setIndent(2, false);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [42, 42, 42]")));
    }

    @Test
    public void testCharInput() {
        assertEquals("\n", PluginParameters.builder().setFormatting("\n", true, true).build().lineSeparatorUtil.toString());
        assertEquals("\r", PluginParameters.builder().setFormatting("\r", true, true).build().lineSeparatorUtil.toString());
        assertEquals("\r\n", PluginParameters.builder().setFormatting("\r\n", true, true).build().lineSeparatorUtil.toString());
    }

    @Test
    public void testFailedInput1() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setFormatting("\nn", true, true);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 110]")));
    }

    @Test
    public void testFailedInput2() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setFormatting("\n\n", true, true);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 10]")));
    }

    @Test
    public void testFailedInput3() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setFormatting("gurka", true, true);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [103, 117, 114, 107, 97]")));
    }

    @Test
    public void testFailedInput4() {

        final Executable testMethod = () -> PluginParameters.builder()
                .setFormatting("", true, true);

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were []")));
    }

    @Test
    public void testMixedInput() {
        assertEquals("\r\n", new LineSeparatorUtil("\\r\n").toString());
        assertEquals("\r\n", new LineSeparatorUtil("\r\\n").toString());
    }

    @Test
    public void testStringInput() {
        assertEquals("\n", new LineSeparatorUtil("\\n").toString());
        assertEquals("\r", new LineSeparatorUtil("\\r").toString());
        assertEquals("\r\n", new LineSeparatorUtil("\\r\\n").toString());
    }

}
