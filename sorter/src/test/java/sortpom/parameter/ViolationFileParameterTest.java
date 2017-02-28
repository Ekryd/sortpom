package sortpom.parameter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ViolationFileParameterTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public final void justPathNameShouldFail() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Violation file sortpom_report/ filename must be specified");
        SortPomImplUtil.create()
                .violationFile("sortpom_reports/")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void violationFileShouldNotBeOverwritten() throws Exception {
        File tempFile = File.createTempFile("violationFile", ".xml", new File("target"));
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Violation file " + tempFile.getAbsolutePath() + " already exists");
        SortPomImplUtil.create()
                .violationFile(tempFile.getAbsolutePath())
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
    }

    @Test
    public final void violationFileShouldBeCreatedOnVerificationStop() throws Exception {
        SortPomImplUtil.create()
                .verifyFail("Stop")
                .violationFile("target/violationFile.xml")
                .testVerifyFail("/full_unsorted_input.xml", FailureException.class, "[ERROR] The xml element <modelVersion> should be placed before <parent>");
        File file = new File("target/violationFile.xml");
        assertThat(file.exists(), is(true));
    }

}
