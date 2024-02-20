package sortpom.sort;

import org.junit.jupiter.api.*;
import sortpom.util.*;

class SortDepManagementExclusionsTest {

  @Test
  void sortGroupIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("groupId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles(
            "/SortDepManagementExclusions_input.xml",
            "/SortDepManagementExclusions_group_expected.xml");
  }

  @Test
  void sortArtifactIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles(
            "/SortDepManagementExclusions_input.xml",
            "/SortDepManagementExclusions_artifact_expected.xml");
  }

  @Test
  void sortGroupIdAndArtifactIdForExclusionsShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyExclusions("groupId,artifactId")
        .lineSeparator("\n")
        .nrOfIndentSpace(2)
        .testFiles(
            "/SortDepManagementExclusions_input.xml",
            "/SortDepManagementExclusions_group_artifact_expected.xml");
  }
}
