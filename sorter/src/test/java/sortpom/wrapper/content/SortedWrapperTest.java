package sortpom.wrapper.content;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.dom4j.tree.BaseElement;
import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2016-07-30
 */
class SortedWrapperTest {

  @Test
  void toStringWithIndentShouldWork() {
    assertThat(
        new SortedWrapper(new BaseElement("Gurka"), 123).toString("  "),
        is("  SortedWrapper{element=[Element: <Gurka attributes: []/>]}"));
    assertThat(new SortedWrapper(null, 123).toString("  "), is("  SortedWrapper{element=null}"));
  }
}
