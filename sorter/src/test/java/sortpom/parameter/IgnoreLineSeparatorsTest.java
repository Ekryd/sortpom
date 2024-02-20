package sortpom.parameter;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class IgnoreLineSeparatorsTest {

  @Test
  void ignoringLineSeparatorsShouldNotSort() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .ignoreLineSeparators(true)
        .testNoSorting("/ignore_line_separators_input.xml");
  }

  @Test
  void doNotIgnoreLineSeparatorsShouldSort() {
    SortPomImplUtil.create()
        .lineSeparator("\n")
        .ignoreLineSeparators(false)
        .testFiles("/ignore_line_separators_input.xml", "/ignore_line_separators_output.xml");
  }
}
