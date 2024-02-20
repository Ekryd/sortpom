package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class SortOrderFilesParameterTest {

  @Test
  void incorrectCustomSortOrderShouldThrowException() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .customSortOrderFile("difforder/VERYdifferentOrder.xml")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(thrown.getMessage(), endsWith("VERYdifferentOrder.xml in classpath"));
  }

  @Test
  void incorrectPredefinedSortOrderShouldThrowException() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .predefinedSortOrder("abbie_normal_brain")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_default_0_4_0.xml");

    var thrown = assertThrows(IllegalArgumentException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("Cannot find abbie_normal_brain.xml among the predefined plugin resources")));
  }

  @Test
  void incorrectCustomSortOrderShouldThrowException2() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .customSortOrderFile("difforder/VERYdifferentOrder.xml")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_differentOrder.xml");

    var thrown = assertThrows(RuntimeException.class, testMethod).getCause().getCause();

    assertThat(thrown, isA(FailureException.class));
    assertThat(thrown.getMessage(), endsWith("VERYdifferentOrder.xml in classpath"));
  }

  @Test
  void incorrectPredefinedSortOrderShouldThrowException2() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .predefinedSortOrder("abbie_normal_brain")
                .lineSeparator("\n")
                .testVerifyXmlIsOrdered("/sortOrderFiles/sorted_default_0_4_0.xml");

    var thrown = assertThrows(RuntimeException.class, testMethod).getCause().getCause();

    assertThat(thrown, isA(IllegalArgumentException.class));
    assertThat(
        thrown.getMessage(),
        is(equalTo("Cannot find abbie_normal_brain.xml among the predefined plugin resources")));
  }
}
