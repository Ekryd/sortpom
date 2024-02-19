package sortpom.parameter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.exception.FailureException;
import sortpom.util.SortPomImplUtil;

class BackupFileExtensionParameterTest {

  @Test
  void emptyBackupFileExtensionShouldNotWork() {
    Executable testMethod =
        () ->
            SortPomImplUtil.create()
                .backupFileExtension("")
                .customSortOrderFile("difforder/differentOrder.xml")
                .lineSeparator("\n")
                .testFiles("/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(), is(equalTo("Could not create backup file, extension name was empty")));
  }
}
