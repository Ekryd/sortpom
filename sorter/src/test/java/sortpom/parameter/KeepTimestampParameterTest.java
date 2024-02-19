package sortpom.parameter;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class KeepTimestampParameterTest {

  @Test
  void whenKeepTimestampNotSetTimestampsShouldDiffer() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .lineSeparator("\n")
        .keepTimestamp(false)
        .testFilesWithTimestamp(
            "/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
  }

  @Test
  void whenKeepTimestampIsSetTimestampsShouldRemain() throws Exception {
    SortPomImplUtil.create()
        .customSortOrderFile("difforder/differentOrder.xml")
        .lineSeparator("\n")
        .keepTimestamp(true)
        .testFilesWithTimestamp(
            "/full_unsorted_input.xml", "/sortOrderFiles/sorted_differentOrder.xml");
  }
}
