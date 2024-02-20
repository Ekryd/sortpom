package sortpom.sort;

import org.junit.jupiter.api.Test;
import sortpom.util.SortPomImplUtil;

class KeepTrailingCommentsTest {
  @Test
  void commentsInIgnoreSectionShouldNotBeFormatted() {
    SortPomImplUtil.create()
        .sortDependencies("scope,groupId,artifactId")
        .lineSeparator("\r\n")
        .testFiles("/TrailingComment_input.xml", "/TrailingComment_expected.xml");
  }
}
