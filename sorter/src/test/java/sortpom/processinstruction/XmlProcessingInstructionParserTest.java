package sortpom.processinstruction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.logger.SortPomLogger;

/**
 * @author bjorn
 * @since 2013-12-28
 */
class XmlProcessingInstructionParserTest {

  private XmlProcessingInstructionParser parser;
  private final SortPomLogger logger = mock(SortPomLogger.class);

  @BeforeEach
  void setUp() {
    parser = new XmlProcessingInstructionParser();
    parser.setup(logger);
  }

  @Test
  void multipleErrorsShouldBeReportedInLogger() {
    var xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
              <artifactId>sortpom</artifactId>
              <description name="pelle" id="id" other="övrigt">Här använder vi åäö</description>
              <groupId>sortpom</groupId>
              <modelVersion>4.0.0</modelVersion>
              <name>SortPom</name>
              <!-- Egenskaper för projektet -->
              <properties>
                <?sortpom resume?>\
            <compileSource>1.6</compileSource>
                <?sortpom ignore?>\
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
                <?sortpom resume?>\
              </properties>
                <?sortpom token='0'?>\
                <?sortpom gurka?>\
              <reporting />
                <?sortpom ignore?>\
              <version>1.0.0-SNAPSHOT</version>
            </project>""";

    Executable testMethod = () -> parser.scanForIgnoredSections(xml);

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
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

  @Test
  void replaceMultipleSectionShouldCreateManyTokens() {
    var xml =
        "abc<?sortpom ignore?>def0<?sortpom resume?>cbaabc<?SORTPOM Ignore?>def1<?sortPom reSUME?>cba";
    parser.scanForIgnoredSections(xml);
    var replaced = parser.replaceIgnoredSections();

    assertThat(replaced, is("abc<?sortpom token='0'?>cbaabc<?sortpom token='1'?>cba"));
    assertThat(parser.existsIgnoredSections(), is(true));
    verifyNoMoreInteractions(logger);
  }

  @Test
  void revertSectionsInRearrangedOrderShouldPlaceTextInRightOrder() {
    var xml =
        "abc<?sortpom ignore?>def0<?sortpom resume?>cbaabc<?SORTPOM Ignore?>def1<?sortPom reSUME?>cba";
    parser.scanForIgnoredSections(xml);
    var replaced = parser.replaceIgnoredSections();
    assertThat(replaced, is("abc<?sortpom token='0'?>cbaabc<?sortpom token='1'?>cba"));

    var sortedXml = "abc<?sortpom token='1'?>cbaabc<?sortpom token='0'?>cba";
    var outputXml = parser.revertIgnoredSections(sortedXml);

    assertThat(
        outputXml,
        is(
            "abc<?SORTPOM Ignore?>def1<?sortPom reSUME?>cbaabc<?sortpom ignore?>def0<?sortpom resume?>cba"));
    verifyNoMoreInteractions(logger);
  }

  @Test
  void noInstructionsShouldWork() {
    var xml =
        """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
              <artifactId>sortpom</artifactId>
              <description name="pelle" id="id" other="övrigt">Här använder vi åäö</description>
              <groupId>sortpom</groupId>
              <modelVersion>4.0.0</modelVersion>
              <name>SortPom</name>
              <!-- Egenskaper för projektet -->
              <properties>
                <compileSource>1.6</compileSource>
                <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
              </properties>
              <reporting />
              <version>1.0.0-SNAPSHOT</version>
            </project>""";
    parser.scanForIgnoredSections(xml);

    assertThat(parser.existsIgnoredSections(), is(false));

    var replaced = parser.replaceIgnoredSections();
    var outputXml = parser.revertIgnoredSections(xml);

    assertThat(replaced, is(xml));
    assertThat(outputXml, is(xml));
    verifyNoMoreInteractions(logger);
  }
}
