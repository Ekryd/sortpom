package sortpom.wrapper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.dom4j.tree.DefaultText;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import sortpom.wrapper.content.UnsortedWrapper;

/**
 * @author bjorn
 * @since 2012-06-14
 */
class UnsortedWrapperTest {

  @Test
  void testIsBefore() {
    Executable testMethod = () -> new UnsortedWrapper<DefaultText>(null).isBefore(null);

    var thrown = assertThrows(UnsupportedOperationException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Cannot be sorted")));
  }
}
