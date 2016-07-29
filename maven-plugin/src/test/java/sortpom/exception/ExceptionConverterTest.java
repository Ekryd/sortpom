package sortpom.exception;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;

/**
 * @author bjorn
 * @since 2013-10-19
 */
public class ExceptionConverterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    @Test
    public void failureExceptionShouldThrowMojoFailureException() throws MojoFailureException {
        FailureException failureException = new FailureException("Gurka");
        
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("Gurka");
        
        new ExceptionConverter(() ->{ throw failureException; }).executeAndConvertException();
    }

    @Test
    public void failureExceptionShouldKeepCause() throws MojoFailureException {
        IllegalArgumentException cause = new IllegalArgumentException("not valid");
        FailureException failureException = new FailureException("Gurka", cause);
        
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("Gurka");
        expectedException.expectCause(is(cause));
        
        new ExceptionConverter(() ->{ throw failureException; }).executeAndConvertException();
    }
}
