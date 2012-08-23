package sortpom;

import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import sortpom.parameter.PluginParameters;
import sortpom.util.ReflectionHelper;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author bjorn
 * @since 2012-08-23
 */
public class VerifyMojoTest {
    private SortPomImpl sortPom = mock(SortPomImpl.class);

    @Test
    public void executeShouldStartMojo() throws Exception {
        VerifyMojo sortMojo = new VerifyMojo();
        ReflectionHelper mojoHelper = new ReflectionHelper(sortMojo);
        mojoHelper.setField(sortPom);

        mojoHelper.setField("lineSeparator", "\n");
        mojoHelper.setField("verifyFail", "SORT");

        sortMojo.execute();

        verify(sortPom).setup(any(Log.class), any(PluginParameters.class));
        verify(sortPom).verifyPom();
        verifyNoMoreInteractions(sortPom);
    }
}
