package sortpom.wrapper;

import org.jdom.Text;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author bjorn
 * @since 2012-06-14
 */
public class UnsortedWrapperTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testIsBefore() throws Exception {
        thrown.expect(UnsupportedOperationException.class);
        new UnsortedWrapper<Text>(null).isBefore(null);
    }
}
