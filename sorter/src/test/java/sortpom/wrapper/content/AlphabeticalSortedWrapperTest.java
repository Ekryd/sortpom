package sortpom.wrapper.content;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.dom4j.tree.BaseElement;
import org.junit.jupiter.api.Test;

/**
 * @author bjorn
 * @since 2016-07-30
 */
class AlphabeticalSortedWrapperTest {

  @Test
  void toStringWithIndentShouldWork() {
    assertThat(
        new AlphabeticalSortedWrapper(new BaseElement("Gurka")).toString("  "),
        is("  AlphabeticalSortedWrapper{element=[Element: <Gurka attributes: []/>]}"));
    assertThat(
        new AlphabeticalSortedWrapper(null).toString("  "),
        is("  AlphabeticalSortedWrapper{element=null}"));
  }
}
