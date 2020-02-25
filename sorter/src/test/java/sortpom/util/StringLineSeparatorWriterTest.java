package sortpom.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author bjorn
 * @since 2020-01-11
 */
public class StringLineSeparatorWriterTest {

    private StringLineSeparatorWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new StringLineSeparatorWriter("separator");
    }

    @Test
    public void writeNewlineShouldBeConvertedToSeparator1() {
        writer.write("Hey\nYou!");
        assertThat(writer.toString(), is("HeyseparatorYou!"));
    }

    @Test
    public void writeNewlineShouldBeConvertedToSeparator2() {
        writer.write("Hello");
        writer.write('&');
        writer.write('\n');
        writer.write("Goodbye");
        assertThat(writer.toString(), is("Hello&separatorGoodbye"));
    }

    @Test
    public void clearExtraNewlinesShouldWork() {
        writer.write("<xml>\n");

        //The spaces should be removed
        writer.write("  ");

        writer.clearLineBuffer();
        writer.write("<moreXml>");
        assertThat(writer.toString(), is("<xml>separator<moreXml>"));
    }

    @Test
    public void testWriteDeprecated1() {

        final Executable testMethod = () -> writer.write(new char[0]);

        final UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, testMethod);

        assertThat(thrown.getMessage(), is(nullValue()));
    }

    @Test
    public void testWriteDeprecated2() {

        final Executable testMethod = () -> writer.write("", 0, 0);

        final UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, testMethod);

        assertThat(thrown.getMessage(), is(nullValue()));
    }

    @Test
    public void testWriteDeprecated3() {

        final Executable testMethod = () -> writer.write(new char[0], 0, 0);

        final UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, testMethod);

        assertThat(thrown.getMessage(), is(nullValue()));
    }

}
