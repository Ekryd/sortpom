package sortpom.processinstruction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sortpom.logger.SortPomLogger;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class SortpomPiScannerTest {
  private SortpomPiScanner sortpomPiScanner;
  private final SortPomLogger logger = mock(SortPomLogger.class);

  @BeforeEach
  void setUp() {
    sortpomPiScanner = new SortpomPiScanner(logger);
  }

  @Test
  void scanNoInstructionsShouldWork() {
    var xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">\n"
            + "  <artifactId>sortpom</artifactId>\n"
            + "  <description name=\"pelle\" id=\"id\" other=\"övrigt\">Här använder vi åäö</description>\n"
            + "  <groupId>sortpom</groupId>\n"
            + "  <modelVersion>4.0.0</modelVersion>\n"
            + "  <name>SortPom</name>\n"
            + "  <!-- Egenskaper för projektet -->\n"
            + "  <properties>\n"
            + "    <compileSource>1.6</compileSource>\n"
            + "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n"
            + "  </properties>\n"
            + "  <reporting />\n"
            + "  <version>1.0.0-SNAPSHOT</version>\n"
            + "</project>";
    sortpomPiScanner.scan(xml);

    assertThat(sortpomPiScanner.isScanError(), is(false));
    verifyNoMoreInteractions(logger);
  }

  @Test
  void correctIgnoreShouldNotReportError() {
    var xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">\n"
            + "  <artifactId>sortpom</artifactId>\n"
            + "  <description name=\"pelle\" id=\"id\" other=\"övrigt\">Här använder vi åäö</description>\n"
            + "  <groupId>sortpom</groupId>\n"
            + "  <modelVersion>4.0.0</modelVersion>\n"
            + "  <name>SortPom</name>\n"
            + "  <!-- Egenskaper för projektet -->\n"
            + "  <properties>\n"
            + "    <?sortpom ignore?>"
            + "<compileSource>1.6</compileSource>\n"
            + "    <?sortpom resume?><project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n"
            + "  </properties>\n"
            + "  <reporting />\n"
            + "  <version>1.0.0-SNAPSHOT</version>\n"
            + "</project>";
    sortpomPiScanner.scan(xml);

    assertThat(sortpomPiScanner.isScanError(), is(false));
    verifyNoMoreInteractions(logger);
  }

  public static Stream<Arguments> illegalProcessingInstructions() {
    return Stream.of(
        Arguments.of(
            "<?sortpom ignore?>",
            "Xml processing instructions for sortpom was not properly terminated. Every <?sortpom IGNORE?> must be followed with <?sortpom RESUME?>"),
        Arguments.of(
            "<?sortpom gurka?>",
            "Xml contained unknown sortpom instruction 'gurka'. Please use <?sortpom IGNORE?> or <?sortpom RESUME?>"),
        Arguments.of(
            "<?sortpom resume?>",
            "Xml contained unexpected sortpom instruction 'resume'. Please use expected instruction <?sortpom IGNORE?>"));
  }

  @ParameterizedTest
  @MethodSource("illegalProcessingInstructions")
  void illegalProcessingInstructionsShouldReportError(
      String xmlProcessingInstruction, String expectedErrorMsg) {
    var xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">\n"
            + "  <artifactId>sortpom</artifactId>\n"
            + "  <description name=\"pelle\" id=\"id\" other=\"övrigt\">Här använder vi åäö</description>\n"
            + "  <groupId>sortpom</groupId>\n"
            + "  <modelVersion>4.0.0</modelVersion>\n"
            + "  <name>SortPom</name>\n"
            + "  <!-- Egenskaper för projektet -->\n"
            + "  <properties>\n"
            + xmlProcessingInstruction
            + "<compileSource>1.6</compileSource>\n"
            + "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n"
            + "  </properties>\n"
            + "  <reporting />\n"
            + "  <version>1.0.0-SNAPSHOT</version>\n"
            + "</project>";
    sortpomPiScanner.scan(xml);

    assertThat(sortpomPiScanner.isScanError(), is(true));
    assertThat(sortpomPiScanner.getFirstError(), is(expectedErrorMsg));
    verify(logger).error(expectedErrorMsg);
    verifyNoMoreInteractions(logger);
  }

  @Test
  void multipleErrorsShouldBeReportedInLogger() {
    var xml =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/maven-v4_0_0.xsd\">\n"
            + "  <artifactId>sortpom</artifactId>\n"
            + "  <description name=\"pelle\" id=\"id\" other=\"övrigt\">Här använder vi åäö</description>\n"
            + "  <groupId>sortpom</groupId>\n"
            + "  <modelVersion>4.0.0</modelVersion>\n"
            + "  <name>SortPom</name>\n"
            + "  <!-- Egenskaper för projektet -->\n"
            + "  <properties>\n"
            + "    <?sortpom resume?>"
            + "<compileSource>1.6</compileSource>\n"
            + "    <?sortpom ignore?>"
            + "    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>\n"
            + "    <?sortpom resume?>"
            + "  </properties>\n"
            + "    <?sortpom token='0'?>"
            + "    <?sortpom gurka?>"
            + "  <reporting />\n"
            + "    <?sortpom ignore?>"
            + "  <version>1.0.0-SNAPSHOT</version>\n"
            + "</project>";
    sortpomPiScanner.scan(xml);

    assertThat(sortpomPiScanner.isScanError(), is(true));
    assertThat(
        sortpomPiScanner.getFirstError(),
        is(
            "Xml contained unexpected sortpom instruction 'resume'. Please use expected instruction <?sortpom IGNORE?>"));
    verify(logger)
        .error(
            "Xml contained unexpected sortpom instruction 'resume'. Please use expected instruction <?sortpom IGNORE?>");
    verify(logger)
        .error(
            "Xml contained unknown sortpom instruction 'token='0''. Please use <?sortpom IGNORE?> or <?sortpom RESUME?>");
    verify(logger)
        .error(
            "Xml contained unknown sortpom instruction 'gurka'. Please use <?sortpom IGNORE?> or <?sortpom RESUME?>");
    verify(logger)
        .error(
            "Xml processing instructions for sortpom was not properly terminated. Every <?sortpom IGNORE?> must be followed with <?sortpom RESUME?>");
    verifyNoMoreInteractions(logger);
  }
}
