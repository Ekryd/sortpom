package sortpom.sort;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class SortDependencyExclusionsTest {

  @Test
  void deprecatedSortPluginsTrueMessageShouldWork() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("true")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            "The 'true' value in 'sortDependencyExclusions' is no longer supported. Please use value 'groupId,artifactId' instead."));
  }

  @Test
  void deprecatedSortPluginsFalseMessageShouldWork() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .customSortOrderFile("custom_1.xml")
                .sortDependencyExclusions("false")
                .lineSeparator("\n")
                .nrOfIndentSpace(4)
                .testFiles("/PluginDefaultName_input.xml", "/PluginDefaultName_expect.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            "The 'false' value in 'sortDependencyExclusions' is no longer supported. Please use empty value '' or omit sortDependencyExclusions instead."));
  }

  @Test
  void sortGroupIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("groupId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles("/SortDepExclusions_input.xml", "/SortDepExclusions_group_expected.xml");
  }

  @Test
  void sortArtifactIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles("/SortDepExclusions_input.xml", "/SortDepExclusions_artifact_expected.xml");
  }

  @Test
  void sortGroupIdAndArtifactIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles(
            "/SortDepExclusions_input.xml", "/SortDepExclusions_group_artifact_expected.xml");
  }
}
