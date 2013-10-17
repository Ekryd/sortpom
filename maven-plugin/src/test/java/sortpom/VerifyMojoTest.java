package sortpom;

import org.junit.Test;
import refutils.ReflectionHelper;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2012-08-23
 */
public class VerifyMojoTest {
    private final SortPomImpl sortPom = mock(SortPomImpl.class);

    @Test
    public void executeShouldStartMojo() throws Exception {
        VerifyMojo sortMojo = new VerifyMojo();
        ReflectionHelper mojoHelper = new ReflectionHelper(sortMojo);
        mojoHelper.setField(sortPom);

        mojoHelper.setField("lineSeparator", "\n");
        mojoHelper.setField("verifyFail", "SORT");

        sortMojo.execute();

        verify(sortPom).setup(any(SortPomLogger.class), any(PluginParameters.class));
        verify(sortPom).verifyPom();
        verifyNoMoreInteractions(sortPom);
    }
}
