package sortpom.sort;

import org.junit.jupiter.api.*;
import sortpom.util.*;

class SortDependencyManagementTests {

  @Test
  void scopeInSortDependencyManagementShouldSortByScope() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyManagement("scope,GROUPID,artifactId")
        .lineSeparator("\r\n")
        .testFiles(
            "/SortDepManagement_input_simpleWithScope.xml",
            "/SortDepManagement_expected_simpleWithScope2.xml");
  }

  @Test
  void defaultDependencyManagementShouldWork() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencies("scope,GROUPID,artifactId")
        .lineSeparator("\r\n")
        .testFiles(
            "/SortDepManagement_input_simpleWithScope.xml",
            "/SortDepManagement_expected_simpleWithScope2.xml");
  }

  @Test
  void differentOrderBetweenDepAndDepManagement() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencies("scope,GROUPID,artifactId")
        .sortDependencyManagement("GROUPID,artifactId")
        .lineSeparator("\r\n")
        .testFiles(
            "/SortDepManagement_input_withDependencies.xml",
            "/SortDepManagement_expected_withDependencies.xml");
  }

  @Test
  void dependencyManagementShouldBeUnaffected() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencies("scope,GROUPID,artifactId")
        .sortDependencyManagement("none")
        .lineSeparator("\n")
        .testFiles(
            "/SortDepManagement_input_withBomDeps.xml",
            "/SortDepManagement_expected_withBomDeps.xml");
  }

  @Test
  void onlySortDependencyManagementShouldNotAffectNormalDependencies() {
    SortPomImplUtil.create()
        .customSortOrderFile("custom_1.xml")
        .sortDependencyManagement("scope")
        .lineSeparator("\n")
        .testFiles(
            "/SortDepManagement_input_withBomDeps.xml",
            "/SortDepManagement_expected_onlySortDepMan.xml");
  }
}
