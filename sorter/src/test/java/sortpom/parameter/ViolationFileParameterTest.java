package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class ViolationFileParameterTest {

  private static final String FILENAME_WITH_DIRECTORIES = "target/sortpom_reports/1/violation.xml";
  private static final String FILENAME_WITHOUT_DIRECTORIES = "target/violation.xml";

  @BeforeEach
  void setUp() {
    //noinspection ResultOfMethodCallIgnored
    new File(FILENAME_WITHOUT_DIRECTORIES).delete();
  }

  @Test
  void violationFileCanBeOverwritten() throws Exception {
    var tempFile = File.createTempFile("violation", ".xml", new File("target"));
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .violationFile(tempFile.getAbsolutePath())
        .testVerifySort(
            "/full_unsorted_input.xml",
            "/full_expected.xml",
            "[INFO] The xml element <modelVersion> should be placed before <parent>",
            true);
  }

  @Test
  void readOnlyViolationFileShouldReportError() throws Exception {
    var tempFile = File.createTempFile("violation", ".xml", new File("target"));
    assertTrue(tempFile.setReadOnly());

    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .violationFile(tempFile.getAbsolutePath())
                .testVerifySort(
                    "/full_unsorted_input.xml",
                    "/full_expected.xml",
                    "[INFO] The xml element <modelVersion> should be placed before <parent>",
                    true);

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("Could not save violation file: " + tempFile.getAbsolutePath())));
  }

  @Test
  void violationFileShouldBeCreatedOnVerificationStop() {
    SortPomImplUtil.create()
        .verifyFail("Stop")
        .violationFile(FILENAME_WITHOUT_DIRECTORIES)
        .testVerifyFail(
            "/full_unsorted_input.xml",
            FailureException.class,
            "[ERROR] The xml element <modelVersion> should be placed before <parent>",
            true);
    var file = new File(FILENAME_WITHOUT_DIRECTORIES);
    assertThat(file.exists(), is(true));
  }

  @Test
  void violationFileWithParentDirectoryShouldBeCreatedOnVerificationWarn() {
    SortPomImplUtil.create()
        .verifyFail("Warn")
        .violationFile(FILENAME_WITH_DIRECTORIES)
        .testVerifyWarn(
            "/full_unsorted_input.xml",
            "[WARNING] The xml element <modelVersion> should be placed before <parent>",
            true);
    var file = new File(FILENAME_WITH_DIRECTORIES);
    assertThat(file.exists(), is(true));
  }

  @Test
  void violationFileContentShouldBeEncodedOnVerificationSort() throws Exception {
    SortPomImplUtil.create()
        .predefinedSortOrder("default_0_4_0")
        .verifyFail("Sort")
        .violationFile(FILENAME_WITH_DIRECTORIES)
        .testVerifySort(
            "/full_unsorted_input.xml",
            "/full_expected.xml",
            "[INFO] The xml element <modelVersion> should be placed before <parent>",
            true);
    var xml = Files.readString(Paths.get(FILENAME_WITH_DIRECTORIES), Charset.defaultCharset());
    assertThat(
        xml,
        containsString(
            "The xml element &lt;modelVersion&gt; should be placed before &lt;parent&gt;"));
  }
}
