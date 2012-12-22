package sortpom;

import org.junit.Test;
import sortpom.logger.SortPomLogger;
import sortpom.parameter.PluginParameters;
import sortpom.util.ReflectionHelper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2012-08-23
 */
public class SortMojoTest {
    private final SortPomImpl sortPom = mock(SortPomImpl.class);

    @Test
    public void executeShouldStartMojo() throws Exception {
        SortMojo sortMojo = new SortMojo();
        ReflectionHelper mojoHelper = new ReflectionHelper(sortMojo);
        mojoHelper.setField(sortPom);

        mojoHelper.setField("lineSeparator", "\n");

        sortMojo.execute();

        verify(sortPom).setup(any(SortPomLogger.class), any(PluginParameters.class));
        verify(sortPom).sortPom();
        verifyNoMoreInteractions(sortPom);
    }
}
