package sortpom.wrapper;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * All method should throw execption since the element should be throw away
 *
 * @author bjorn
 * @since 2012-06-14
 */
public class ThrowAwayContentWrapperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testGetContent() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.getContent();
    }

    @Test
    public void testIsBefore() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.isBefore(null);
    }

    @Test
    public void testIsContentElement() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.isContentElement();
    }

    @Test
    public void testIsResortable() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.isResortable();
    }

    @Test
    public void testToString1() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.toString();
    }

    @Test
    public void testToString2() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        ThrowAwayContentWrapper.INSTANCE.toString("");
    }
}
