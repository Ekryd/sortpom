package sortpom.parameter;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class LineSeparatorParameterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void lineSeparatorWithSomehtingElseShouldThrowException() throws Exception {
        thrown.expect(MojoFailureException.class);
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [42, 42, 42]");

        new PluginParametersBuilder()
                .setEncoding("UTF-8")
                .setFormatting("***", false, false)
                .setIndent(2, false);

    }

    @Test
    public void testCharInput() throws MojoFailureException {
        PluginParametersBuilder builder = new PluginParametersBuilder();
        assertEquals("\n", builder.setFormatting("\n", true, true).createPluginParameters().lineSeparatorUtil.toString());
        assertEquals("\r", builder.setFormatting("\r", true, true).createPluginParameters().lineSeparatorUtil.toString());
        assertEquals("\r\n", builder.setFormatting("\r\n", true, true).createPluginParameters().lineSeparatorUtil.toString());
    }

    @Test
    public void testFailedInput1() throws MojoFailureException {
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 110]");

        PluginParametersBuilder builder = new PluginParametersBuilder();
        builder.setFormatting("\nn", true, true);
    }

    @Test
    public void testFailedInput2() throws MojoFailureException {
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [10, 10]");
        PluginParametersBuilder builder = new PluginParametersBuilder();
        builder.setFormatting("\n\n", true, true);
    }

    @Test
    public void testFailedInput3() throws MojoFailureException {
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [103, 117, 114, 107, 97]");
        PluginParametersBuilder builder = new PluginParametersBuilder();
        builder.setFormatting("gurka", true, true);
    }

    @Test
    public void testFailedInput4() throws MojoFailureException {
        thrown.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were []");
        PluginParametersBuilder builder = new PluginParametersBuilder();
        builder.setFormatting("", true, true);
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
