package sortpom.wrapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.wrapper.content.SingleNewlineInTextWrapper;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * All method should throw exception since the element should be throw away, except for the toString method
 *
 * @author bjorn
 * @since 2012-06-14
 */
public class SingleNewlineInTextWrapperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetContent() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        SingleNewlineInTextWrapper.INSTANCE.getContent();
    }

    @Test
    public void testIsBefore() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        SingleNewlineInTextWrapper.INSTANCE.isBefore(null);
    }

    @Test
    public void testIsContentElement() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        SingleNewlineInTextWrapper.INSTANCE.isContentElement();
    }

    @Test
    public void testIsResortable() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        SingleNewlineInTextWrapper.INSTANCE.isSortable();
    }

    @Test
    public void testToString() throws Exception {
        assertThat(SingleNewlineInTextWrapper.INSTANCE.toString("  "), is("  SingleNewlineInTextWrapper"));
    }
}
